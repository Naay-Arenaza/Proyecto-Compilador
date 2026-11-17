
package TrabajoPractico3.ArbolSintactico;

public class NodoPrint extends NodoAST {
    private NodoAST contenido;

    public NodoPrint(NodoAST contenido) {
        super("POUT");
        this.contenido = contenido;
    }

    public NodoAST getContenido() {
        return contenido;
    }

    @Override
    public String generarCodigo() {

        if (contenido == null) {
            return null;
        }

        // Validar expresiones binarias antes de generar código.
        if (contenido instanceof NodoBinario) {
            NodoBinario binario = (NodoBinario) contenido;

            if (binario.obtenerTipo() == null) {
                String error = binario.getErrorTipo();

                registrarError(
                    "Semántico",
                    obtenerLinea(contenido),
                    error != null
                        ? error
                        : "La expresión de POUT tiene tipos incompatibles."
                );

                return null;
            }
        }

        // Validar conversiones TODF directas.
        if (contenido instanceof NodoUnario) {
            NodoUnario unario = (NodoUnario) contenido;

            if (unario.obtenerTipo() == null) {
                registrarError(
                    "Semántico",
                    obtenerLinea(contenido),
                    unario.getErrorTipo() != null
                        ? unario.getErrorTipo()
                        : "No se pudo determinar el tipo de la conversión."
                );

                return null;
            }
        }

        String val = contenido.generarCodigo();

        if (val == null) {
            return null;
        }

        System.out.println("PRINT " + val);
        return null;
    }

    private int obtenerLinea(NodoAST nodo) {

        if (nodo instanceof NodoHoja) {
            return ((NodoHoja) nodo).getLinea();
        }

        if (nodo instanceof NodoBinario) {
            NodoBinario binario = (NodoBinario) nodo;

            int linea = obtenerLinea(binario.getIzq());

            return linea != 0
                ? linea
                : obtenerLinea(binario.getDer());
        }

        if (nodo instanceof NodoUnario) {
            return obtenerLinea(
                ((NodoUnario) nodo).getOperando()
            );
        }

        return 0;
    }
}
