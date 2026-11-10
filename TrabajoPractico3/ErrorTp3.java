package TrabajoPractico3;

public class ErrorTp3 {

    private final String etapa;
    private final int linea;
    private final String descripcion;

    public ErrorTp3(String etapa,int linea,String descripcion) {
        this.etapa = etapa;
        this.linea = linea;
        this.descripcion = descripcion;
    }

    public String getEtapa() {
        return etapa;
    }

    public int getLinea() {
        return linea;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
