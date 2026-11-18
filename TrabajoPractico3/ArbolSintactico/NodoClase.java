package TrabajoPractico3.ArbolSintactico;

public class NodoClase extends NodoAST {
    private String nombre;
    private String ambitoCompleto;
    private NodoAST cuerpo;

    public NodoClase(String nombre, String ambitoCompleto, NodoAST cuerpo) {
        super("DECLARACION_CLASE");
        this.nombre = nombre;
        this.ambitoCompleto = ambitoCompleto;
        this.cuerpo = cuerpo;
    }

    public String getNombre() { return nombre; }

    public String getAmbitoCompleto() { return ambitoCompleto; }

    @Override
    public String generarCodigo() {
        System.out.println("; ==========================================");// Encabezado de la clase
        System.out.println("; CLASE " + ambitoCompleto);
        System.out.println("; ==========================================");
        
        if (cuerpo != null) {// Generar el código de los miembros de la clase
            cuerpo.generarCodigo();
        }

        return null;
    }

}
