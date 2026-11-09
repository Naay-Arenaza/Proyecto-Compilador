// NodoAST.java (Clase Base)
public abstract class NodoAST {
    private String etiqueta;

    public NodoAST(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }

    // Método fundamental para la etapa de Generación de Código Intermedio
    public abstract String generarCodigo();
}