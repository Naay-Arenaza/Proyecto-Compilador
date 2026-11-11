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
        String etiquetaInicio = "METHOD_" + ambitoCompleto.replace(':', '_') + ":\n";
        String ejecs = (cuerpoEjecutable != null) ? cuerpoEjecutable.generarCodigo() : "";
        
        return "; --- INICIO METODO " + nombre + " ---\n" 
                + etiquetaInicio 
                + ejecs 
                + "RET\n";
    }
}