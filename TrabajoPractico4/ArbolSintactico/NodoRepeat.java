package TrabajoPractico4.ArbolSintactico;

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
        if (condicion instanceof NodoBinario) {
            NodoBinario binario = (NodoBinario) condicion;
            if (binario.obtenerTipo() == null) {
                String error = binario.getErrorTipo();
                registrarError("Semántico", obtenerLinea(condicion), error != null ? error : "La condición del REPEAT tiene tipos incompatibles.");
                return null;
            }
        }

        String lInicio = GeneradorTAC.nuevaEtiqueta();
        System.out.println(lInicio + ":");

        if (cuerpo != null) {
            cuerpo.generarCodigo();
        }

        if (condicion != null) {
            String condRes = condicion.generarCodigo();
            if (condRes == null) return null;
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
