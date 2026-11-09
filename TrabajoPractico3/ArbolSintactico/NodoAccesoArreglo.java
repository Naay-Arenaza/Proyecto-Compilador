public class NodoAccesoArreglo extends NodoAST {
    private NodoAST arreglo;
    private NodoAST indice;

    public NodoAccesoArreglo(NodoAST arreglo, NodoAST indice) {
        super("ACCESO_ARREGLO");
        this.arreglo = arreglo;
        this.indice = indice;
    }

    public NodoAST getArreglo() { return arreglo; }
    public NodoAST getIndice() { return indice; }

    @Override
    public String generarCodigo() {
        String arr = arreglo.generarCodigo();
        String idx = indice.generarCodigo();
        return arr + "[" + idx + "]";
    }
}