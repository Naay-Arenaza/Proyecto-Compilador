package TrabajoPractico3.ArbolSintactico;
public class NodoPrint extends NodoAST {
    private NodoAST contenido;

    public NodoPrint(NodoAST contenido) {
        super("POUT");
        this.contenido = contenido;
    }

    public NodoAST getContenido() { return contenido; }

    @Override
    public String generarCodigo() {
        String val = contenido != null ? contenido.generarCodigo() : "";
        System.out.println("PRINT " + val);
        return null;
    }
}