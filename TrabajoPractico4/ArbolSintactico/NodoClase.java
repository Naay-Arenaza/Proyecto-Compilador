package TrabajoPractico4.ArbolSintactico;

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
        System.out.println("; ==========================================");
        System.out.println("; CLASE " + ambitoCompleto);
        System.out.println("; ==========================================");
        
        if (cuerpo != null) {
            cuerpo.generarCodigo();
        }

        return null;
    }

}
