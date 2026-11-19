package TrabajoPractico3.ArbolSintactico;

public class NodoIF extends NodoAST {
    private NodoAST condicion;
    private NodoAST cuerpoThen;
    private NodoAST cuerpoElse;

    public NodoIF(NodoAST condicion, NodoAST cuerpoThen, NodoAST cuerpoElse) {
        super("IF");
        this.condicion = condicion;
        this.cuerpoThen = cuerpoThen;
        this.cuerpoElse = cuerpoElse;
    }

    public NodoAST getCondicion() { return condicion; }
    public NodoAST getCuerpoThen() { return cuerpoThen; }
    public NodoAST getCuerpoElse() { return cuerpoElse; }

    @Override
    public String generarCodigo() {
        if (condicion instanceof NodoBinario || condicion instanceof NodoUnario) {
            if (condicion.obtenerTipo() == null) {
                String error = condicion.obtenerErrorTipo();
                registrarError( "Semántico", obtenerLinea(condicion),  error != null  ? error : "La condición del IF tiene tipos incompatibles.");
                return null;
            }
        }
        String lElse = GeneradorTAC.nuevaEtiqueta();
        String lFin = GeneradorTAC.nuevaEtiqueta();
        String condRes = condicion.generarCodigo();
        if (condRes == null) return null;
        System.out.println("IF_FALSE " + condRes + " GOTO "+ (cuerpoElse != null ? lElse : lFin));
        if (cuerpoThen != null) {
            cuerpoThen.generarCodigo();
        }
        if (cuerpoElse != null) {
            System.out.println("GOTO " + lFin);
            System.out.println(lElse + ":");
            cuerpoElse.generarCodigo();
        }
        System.out.println(lFin + ":");
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