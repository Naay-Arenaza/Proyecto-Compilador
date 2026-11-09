class NodoInvocacionMetodo extends NodoAST {
    private String objeto;
    private String metodo;
    private NodoAST parametros;

    public NodoInvocacionMetodo(String objeto, String metodo, NodoAST parametros) {
        super("INVOCACION_METODO");
        this.objeto = objeto;
        this.metodo = metodo;
        this.parametros = parametros;
    }

    public String getObjeto() { return objeto; }
    public String getMetodo() { return metodo; }
    public NodoAST getParametros() { return parametros; }

    @Override
    public String generarCodigo() {
        if (parametros != null) parametros.generarCodigo();
        System.out.println("CALL_METHOD " + objeto + "." + metodo);
        return "T_ret";
    }
}