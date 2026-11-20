package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.TablaDeSimbolosTp4;
import TrabajoPractico4.GeneracionWebAssembly.*;
import TrabajoPractico4.RegistroSimboloTp4;

public class NodoAsignacion extends NodoAST {
    private NodoAST variable;
    private NodoAST expresion;

    public NodoAsignacion(NodoAST variable, NodoAST expresion) {
        super(":=");
        this.variable = variable;
        this.expresion = expresion;
    }

    public NodoAST getVariable() { return variable; }

    public NodoAST getExpresion() { return expresion; }

    private int obtenerLinea() {
        if (variable instanceof NodoHoja) {
            return ((NodoHoja) variable).getLinea();
        }
        return 0;
    }

    private boolean validarTipos() {
        TablaDeSimbolosTp4 tabla = getTablaSimbolos();
        if (tabla == null || variable == null || expresion == null) {
            return false;
        }
        String tipoDestino = variable.obtenerTipo();
        String tipoExpresion = expresion.obtenerTipo();
        if (tipoDestino == null || tipoExpresion == null) {
            String error = null;
            if (tipoDestino == null) {
                error = variable.obtenerErrorTipo();
            }
            if (error == null && tipoExpresion == null) {
                error = expresion.obtenerErrorTipo();
            }
            registrarError("Semántico", obtenerLinea(), error != null ? error : "No se pudo determinar el tipo de la variable o de la expresión.");
            return false;
        }

        if (tabla.tipoCompatibleAsignacion(tipoDestino, tipoExpresion, false)) {
            return true;
        }

        if (tabla.tiposObjetoCompatibles(tipoDestino, tipoExpresion)) {
            return true;
        }

        registrarError("Semántico", obtenerLinea(), "Tipos incompatibles en la asignación: no se puede asignar " + tipoExpresion + " a " + tipoDestino + ".");
        return false;
    }

    @Override
    public String obtenerTipo() {
        if (variable == null) {
            return null;
        }
        return variable.obtenerTipo();
    }

    @Override
    public String generarCodigo() {
        if (!validarTipos()) { return null; }
        String der = expresion.generarCodigo();
        String izq;
        if (variable instanceof NodoHoja) {
            izq = ((NodoHoja) variable).generarReferencia();
        } else {
            izq = variable.generarCodigo();
        }
        if (der == null || izq == null) { return null; }
        System.out.println(izq + " := " + der);
        return izq;
    }

    @Override
    public String generarWasm() {
        if (!validarTipos()) return null;

        if (variable instanceof NodoHoja && expresion instanceof NodoHoja) {
            RegistroSimboloTp4 regDest = ((NodoHoja) variable).getSimboloVinculado();
            RegistroSimboloTp4 regOrig = ((NodoHoja) expresion).getSimboloVinculado();
            if (regDest != null && regOrig != null
                    && TablaDeSimbolosTp4.USO_OBJETO.equals(regDest.getUso())
                    && TablaDeSimbolosTp4.USO_OBJETO.equals(regOrig.getUso())) {
                TablaDeSimbolosTp4 tabla = getTablaSimbolos();
                String claseDest = regDest.obtenerAtributo(TablaDeSimbolosTp4.ATR_CLASE) != null
                        ? String.valueOf(regDest.obtenerAtributo(TablaDeSimbolosTp4.ATR_CLASE))
                        : regDest.getTipo();
                String claseOrig = regOrig.obtenerAtributo(TablaDeSimbolosTp4.ATR_CLASE) != null
                        ? String.valueOf(regOrig.obtenerAtributo(TablaDeSimbolosTp4.ATR_CLASE))
                        : regOrig.getTipo();

                int pos = 0;
                while (true) {
                    RegistroSimboloTp4 attrDest = tabla.obtenerAtributoPosicionalPorReferencia(claseDest, pos);
                    RegistroSimboloTp4 attrOrig = tabla.obtenerAtributoPosicionalPorReferencia(claseOrig, pos);
                    if (attrDest == null || attrOrig == null) break;
                    String tipoWasm = GeneradorWasm.mapearTipo(attrDest.getTipo());
                    String refDest = GeneradorWasm.sanitizarNombre(regDest.getNombreCompleto() + "__" + attrDest.getNombreCompleto());
                    String refOrig = GeneradorWasm.sanitizarNombre(regOrig.getNombreCompleto() + "__" + attrOrig.getNombreCompleto());
                    GeneradorWasm.declararGlobalSiNoExiste(refDest, tipoWasm);
                    GeneradorWasm.declararGlobalSiNoExiste(refOrig, tipoWasm);
                    GeneradorWasm.emitirGet(refOrig);
                    GeneradorWasm.emitirSet(refDest);
                    pos++;
                }
                String objDestWasm = GeneradorWasm.sanitizarNombre(regDest.getNombreCompleto());
                GeneradorWasm.declararGlobalSiNoExiste(objDestWasm, "i32");
                return objDestWasm;
            }
        }
        String auxDer = expresion.generarWasm();
        if (auxDer == null) return null;
        if (variable instanceof NodoHoja) {
            RegistroSimboloTp4 registroDestino = ((NodoHoja) variable).getSimboloVinculado();
            if (GeneradorWasm.esAtributo(registroDestino)) {
                GeneradorWasm.emitirSetAtributo(registroDestino, auxDer);
                return auxDer;
            }
            String destinoWasm = GeneradorWasm.sanitizarNombre(((NodoHoja) variable).generarReferencia());
            GeneradorWasm.declararGlobalSiNoExiste(destinoWasm, GeneradorWasm.mapearTipo(variable.obtenerTipo()));
            GeneradorWasm.emitirGet(auxDer);
            GeneradorWasm.emitirSet(destinoWasm);
            return auxDer;
        } else if (variable instanceof NodoAccesoArreglo) {
            if (!((NodoAccesoArreglo) variable).generarWasmAsignacion(auxDer)) {
                return null;
            }
            return auxDer;
        }
        return null;
    }
}