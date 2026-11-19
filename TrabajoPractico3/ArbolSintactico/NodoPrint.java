package TrabajoPractico3.ArbolSintactico;

public class NodoPrint extends NodoAST {
    private NodoAST contenido;

    public NodoPrint(NodoAST contenido) {
        super("POUT");
        this.contenido = contenido;
    }

    public NodoAST getContenido() { return contenido;}

    @Override
    public String generarCodigo() {
        if (contenido == null) { return null;}
        if (contenido instanceof NodoBinario || contenido instanceof NodoUnario) {
            if (contenido.obtenerTipo() == null) {
                String error = contenido.obtenerErrorTipo();
                registrarError( "Semántico",obtenerLinea(contenido),error != null ? error : "No se pudo determinar el tipo de la expresión de POUT.");
                return null;
            }
        }
        String val = contenido.generarCodigo();
        if (val == null) { return null; }
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
            return linea != 0 ? linea : obtenerLinea(binario.getDer());
        }
        if (nodo instanceof NodoUnario) {
            return obtenerLinea( ((NodoUnario) nodo).getOperando() );
        }
        return 0;
    }
}
