package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.TablaDeSimbolosTp4;

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
}
