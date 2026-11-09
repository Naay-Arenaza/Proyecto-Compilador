
class NodoHoja extends NodoAST {
    private String tipoNodo; // "ID", "CTE", etc.
    private String valor;

    public NodoHoja(String tipoNodo, String valor) {
        super(valor);
        this.tipoNodo = tipoNodo;
        this.valor = valor;
    }

    @Override
    public String generarCodigo(/*TablaSimbolosScope tabla*/) {
        if (tipoNodo.equals("ID")) {
            // Devuelve el nombre único asignado a este scope (ej: "x_0" o "x_1")
            //return tablaSimbolos.buscarVariable(valor);
        }
        return valor; // Si es una constante CTE, devuelve el valor directo
    }
}