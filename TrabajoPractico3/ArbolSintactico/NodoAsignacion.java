
package TrabajoPractico3.ArbolSintactico;

import TrabajoPractico3.TablaDeSimbolosTp3;

public class NodoAsignacion extends NodoAST {

    private NodoAST variable;
    private NodoAST expresion;

    public NodoAsignacion(NodoAST variable, NodoAST expresion) {
        super(":=");
        this.variable = variable;
        this.expresion = expresion;
    }

    public NodoAST getVariable() {
        return variable;
    }

    public NodoAST getExpresion() {
        return expresion;
    }

    private int obtenerLinea() {
        if (variable instanceof NodoHoja) {
            return ((NodoHoja) variable).getLinea();
        }
        return 0;
    }

    
private boolean validarTipos() {
    TablaDeSimbolosTp3 tabla = getTablaSimbolos();

    if (tabla == null || variable == null || expresion == null) {
        return false;
    }

        String tipoDestino = variable.obtenerTipo();
        String tipoExpresion = expresion.obtenerTipo();

        if (tipoDestino == null || tipoExpresion == null) {

            // Primero comprobamos si el error proviene de una operación binaria.
            if (expresion instanceof NodoBinario) {
                NodoBinario binario = (NodoBinario) expresion;

                if (binario.getErrorTipo() != null) {
                    registrarError(
                        "Semántico",
                        obtenerLinea(),
                        binario.getErrorTipo()
                    );
                    return false;
                }
            }

            // Después comprobamos si el error proviene de una conversión TODF.
            if (expresion instanceof NodoUnario) {
                NodoUnario unario = (NodoUnario) expresion;

                if (unario.getErrorTipo() != null) {
                    registrarError(
                        "Semántico",
                        obtenerLinea(),
                        unario.getErrorTipo()
                    );
                    return false;
                }
            }

            registrarError(
                "Semántico",
                obtenerLinea(),
                "No se pudo determinar el tipo de la variable o de la expresión."
            );

            return false;
        }

        // Asignación entre tipos iguales.
        if (tabla.tipoCompatibleAsignacion(tipoDestino, tipoExpresion, false)) {
            return true;
        }

        // Compatibilidad estructural de objetos (Tema 25).
        if (tabla.tiposObjetoCompatibles(tipoDestino, tipoExpresion)) {
            return true;
        }

        registrarError(
            "Semántico",
            obtenerLinea(),
            "Tipos incompatibles en la asignación: no se puede asignar "
                + tipoExpresion + " a " + tipoDestino + "."
        );

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

        if (!validarTipos()) {
            return null;
        }

        String der = expresion.generarCodigo();

        String izq;

        if (variable instanceof NodoHoja) {
            izq = ((NodoHoja) variable).generarReferencia();
        } else {
            izq = variable.generarCodigo();
        }

        if (der == null || izq == null) {
            return null;
        }

        System.out.println(izq + " := " + der);
        return izq;
    }
}
