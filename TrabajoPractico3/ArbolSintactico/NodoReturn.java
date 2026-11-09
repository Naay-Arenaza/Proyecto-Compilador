public class NodoReturn extends NodoAST {
    private NodoAST expresionRetorno;

    public NodoReturn(NodoAST expresionRetorno) {
        super("RET");
        this.expresionRetorno = expresionRetorno;
    }

    public NodoAST getExpresionRetorno() { return expresionRetorno; }

    @Override
    public String generarCodigo() {
        String retVal = expresionRetorno != null ? expresionRetorno.generarCodigo() : "";
        System.out.println("RETURN " + retVal);
        return retVal;
    }
}