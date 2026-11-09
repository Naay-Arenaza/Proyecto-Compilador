package TrabajoPractico3.ArbolSintactico;
public class NodoUnario extends NodoAST {
    private NodoAST operando;

    public NodoUnario(String operador, NodoAST operando) {
        super(operador);
        this.operando = operando;
    }

    public NodoAST getOperando() { return operando; }

    @Override
    public String generarCodigo() {
        String res = operando.generarCodigo();
        System.out.println("T_temp = " + getEtiqueta() + "(" + res + ")");
        return "T_temp";
    }
}