package TrabajoPractico3.ArbolSintactico;

public class NodoMetodo extends NodoAST {
    private String nombre;
    private String ambitoCompleto;
    private NodoAST parametros;
    private NodoAST cuerpoEjecutable;

    public NodoMetodo(String nombre, String ambitoCompleto, NodoAST parametros, NodoAST ejecutables) {
        super("DECLARACION_METODO");
        this.nombre = nombre;
        this.ambitoCompleto = ambitoCompleto;
        this.parametros = parametros;
        this.cuerpoEjecutable = ejecutables;
    }

    public String getNombre() { return nombre; }
    public String getAmbitoCompleto() { return ambitoCompleto; }

    @Override
    public String generarCodigo() {
        System.out.println("; --- INICIO METODO " + nombre + " ---");
        String etiquetaInicio = "METHOD_" + ambitoCompleto.replace(':', '_') + ":";
        System.out.println(etiquetaInicio);
        if (cuerpoEjecutable != null) {
            cuerpoEjecutable.generarCodigo();
        }
        System.out.println("RET");
        return null;
    }
}