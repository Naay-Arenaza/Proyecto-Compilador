// (Para Operaciones Binarias: +, -, *, /, asignaciones)
public class NodoBinario extends NodoAST {
    private NodoAST izq;
    private NodoAST der;

    public NodoBinario(String operador, NodoAST izq, NodoAST der) {
        super(operador);
        this.izq = izq;
        this.der = der;
    }

    public NodoAST getIzq() { return izq; }
    public NodoAST getDer() { return der; }

    @Override
    public String generarCodigo() {
        String tIzq = izq.generarCodigo();
        String tDer = der.generarCodigo();
        String temp = GeneradorTAC.nuevoTemporal();
        System.out.println(temp + " = " + tIzq + " " + getEtiqueta() + " " + tDer);
        return temp;
    }
}