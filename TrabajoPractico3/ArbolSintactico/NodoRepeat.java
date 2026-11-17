
package TrabajoPractico3.ArbolSintactico;

public class NodoRepeat extends NodoAST {
    private NodoAST cuerpo;
    private NodoAST condicion;

    public NodoRepeat(NodoAST cuerpo, NodoAST condicion) {
        super("REPEAT");
        this.cuerpo = cuerpo;
        this.condicion = condicion;
    }

    public NodoAST getCuerpo() { return cuerpo; }
    public NodoAST getCondicion() { return condicion; }

    @Override
    public String generarCodigo() {

        // 1. Validar semánticamente la condición.
        if (condicion instanceof NodoBinario) {
            NodoBinario binario = (NodoBinario) condicion;

            if (binario.obtenerTipo() == null) {
                String error = binario.getErrorTipo();

                registrarError(
                    "Semántico",
                    obtenerLinea(condicion),
                    error != null
                        ? error
                        : "La condición del REPEAT tiene tipos incompatibles."
                );

                return null;
            }
        }

        // 2. Etiqueta de inicio de iteración.
        String lInicio = GeneradorTAC.nuevaEtiqueta();
        System.out.println(lInicio + ":");

        // 3. Generar el código del cuerpo.
        if (cuerpo != null) {
            cuerpo.generarCodigo();
        }

        // 4. Evaluar condición.
        if (condicion != null) {
            String condRes = condicion.generarCodigo();

            if (condRes == null) return null;

            // Volver al inicio si la condición es verdadera.
            System.out.println("IF " + condRes + " GOTO " + lInicio);
        }

        return null;
    }

    private int obtenerLinea(NodoAST nodo) {
        if (nodo instanceof NodoHoja) {
            return ((NodoHoja) nodo).getLinea();
        }

        if (nodo instanceof NodoBinario) {
            NodoBinario binario = (NodoBinario) nodo;

            int linea = obtenerLinea(binario.getIzq());
            return linea != 0 ? linea : obtenerLinea(binario.getDer());
        }

        if (nodo instanceof NodoUnario) {
            return obtenerLinea(((NodoUnario) nodo).getOperando());
        }

        return 0;
    }
}
