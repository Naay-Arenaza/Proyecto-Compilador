class NodoAsignacion extends NodoAST {
    private NodoAST variable;
    private NodoAST expresion;

    public NodoAsignacion(NodoAST variable, NodoAST expresion) {
        super(":=");
        this.variable = variable;
        this.expresion = expresion;
    }

    public NodoAST getVariable() { return variable; }
    public NodoAST getExpresion() { return expresion; }

    @Override
    public String generarCodigo() {
        String der = expresion.generarCodigo();
        String izq = variable.generarCodigo();
        System.out.println(izq + " := " + der);
        return izq;
    }
}