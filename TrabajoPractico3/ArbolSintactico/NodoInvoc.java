class NodoInvocacion extends NodoAST {
    private String nombreFuncion;
    private NodoAST parametros;

    public NodoInvocacion(String nombreFuncion, NodoAST parametros) {
        super("INVOCACION_FUNCION");
        this.nombreFuncion = nombreFuncion;
        this.parametros = parametros;
    }

    public String getNombreFuncion() { return nombreFuncion; }
    public NodoAST getParametros() { return parametros; }

    @Override
    public String generarCodigo() {
        if (parametros != null) parametros.generarCodigo();
        System.out.println("CALL " + nombreFuncion);
        return "T_ret";
    }
}