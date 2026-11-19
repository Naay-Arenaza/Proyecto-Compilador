package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.TablaDeSimbolosTp4;
import TrabajoPractico4.GeneracionWebAssembly.*;
import TrabajoPractico4.RegistroSimboloTp4;
import java.util.List;

public class NodoAsignacion extends NodoAST {
    private NodoAST variable;
    private NodoAST expresion;

    public NodoAsignacion(NodoAST variable, NodoAST expresion) {
        super(":=");
        this.variable = variable;
        this.expresion = expresion;
    }

    public NodoAST getVariable() { return variable;}

    public NodoAST getExpresion() { return expresion;}

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
            registrarError("Semántico",obtenerLinea(), error != null ? error : "No se pudo determinar el tipo de la variable o de la expresión." );
            return false;
        }

        if (tabla.tipoCompatibleAsignacion(tipoDestino, tipoExpresion, false)) { return true; } 
    
        if (tabla.tiposObjetoCompatibles(tipoDestino, tipoExpresion)) { return true;}

        registrarError("Semántico",obtenerLinea(),"Tipos incompatibles en la asignación: no se puede asignar " + tipoExpresion + " a " + tipoDestino + "." );
        return false;
    }

    private boolean generarAsignacionObjetoWasm() {
        if (!(variable instanceof NodoHoja) || !(expresion instanceof NodoHoja)) {
            return false;
        }
        NodoHoja destino = (NodoHoja) variable;
        NodoHoja origen = (NodoHoja) expresion;
        RegistroSimboloTp4 regDestino = destino.getSimboloVinculado();
        RegistroSimboloTp4 regOrigen = origen.getSimboloVinculado();
        if (regDestino == null || regOrigen == null) {
            return false;
        }
        if (!TablaDeSimbolosTp4.USO_OBJETO.equals(regDestino.getUso()) || !TablaDeSimbolosTp4.USO_OBJETO.equals(regOrigen.getUso())) {
            return false;
        }
        TablaDeSimbolosTp4 tabla = getTablaSimbolos();
        String claseDestino = String.valueOf(regDestino.obtenerAtributo("CLASE"));
        String claseOrigen = String.valueOf(regOrigen.obtenerAtributo("CLASE"));
        List<RegistroSimboloTp4> atributosDestino = tabla.obtenerAtributosPosicionalesPorReferencia(claseDestino);
        List<RegistroSimboloTp4> atributosOrigen = tabla.obtenerAtributosPosicionalesPorReferencia(claseOrigen);
        if (atributosDestino.size() != atributosOrigen.size()) {
            registrarError( "Semántico", obtenerLinea(), "Los objetos no tienen la misma estructura.");
            return false;
        }
        for (int i = 0; i < atributosDestino.size(); i++) {
            RegistroSimboloTp4 atrDestino = atributosDestino.get(i);
            RegistroSimboloTp4 atrOrigen = atributosOrigen.get(i);
            if (!atrDestino.getTipo().equalsIgnoreCase(atrOrigen.getTipo())) {
                registrarError(  "Semántico",obtenerLinea(), "Los atributos de los objetos no son compatibles.");
                return false;
            }
        }
        String nombreDestino = regDestino.getNombreCompleto();
        String nombreOrigen = regOrigen.getNombreCompleto();
        for (int i = 0; i < atributosDestino.size(); i++) {
            String refOrigen = GeneradorWasm.sanitizarNombre( nombreOrigen + "__" +  atributosOrigen.get(i).getNombreCompleto());
            String refDestino = GeneradorWasm.sanitizarNombre( nombreDestino + "__" + atributosDestino.get(i).getNombreCompleto() );
            GeneradorWasm.emitirGet(refOrigen);
            GeneradorWasm.emitirSet(refDestino);
        }
        return true;
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
            TrabajoPractico4.RegistroSimboloTp4 regDest = ((NodoHoja) variable).getSimboloVinculado();
            TrabajoPractico4.RegistroSimboloTp4 regOrig = ((NodoHoja) expresion).getSimboloVinculado();
            if (regDest != null && regOrig != null && TrabajoPractico4.TablaDeSimbolosTp4.USO_OBJETO.equals(regDest.getUso()) && TrabajoPractico4.TablaDeSimbolosTp4.USO_OBJETO.equals(regOrig.getUso())) {
                TablaDeSimbolosTp4 tabla = getTablaSimbolos();
                String claseDest = regDest.obtenerAtributo("CLASE") != null ? String.valueOf(regDest.obtenerAtributo("CLASE")) : regDest.getTipo();
                String claseOrig = regOrig.obtenerAtributo("CLASE") != null  ? String.valueOf(regOrig.obtenerAtributo("CLASE")) : regOrig.getTipo();

                int pos = 0;
                while (true) {
                    TrabajoPractico4.RegistroSimboloTp4 attrDest = tabla.obtenerAtributoPosicionalPorReferencia(claseDest, pos);
                    TrabajoPractico4.RegistroSimboloTp4 attrOrig = tabla.obtenerAtributoPosicionalPorReferencia(claseOrig, pos);
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

        String destinoWasm;
        if (variable instanceof NodoHoja) {
            destinoWasm = GeneradorWasm.sanitizarNombre(((NodoHoja) variable).generarReferencia());
            GeneradorWasm.declararGlobalSiNoExiste(destinoWasm, GeneradorWasm.mapearTipo(variable.obtenerTipo()));
        } else if (variable instanceof NodoAccesoArreglo) {
            destinoWasm = ((NodoAccesoArreglo) variable).generarWasmReferencia();
        } else {
            return null;
        }
        GeneradorWasm.emitirGet(auxDer);
        GeneradorWasm.emitirSet(destinoWasm);
        return auxDer;
    }
}