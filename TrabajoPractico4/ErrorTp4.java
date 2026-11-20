package TrabajoPractico4;

public class ErrorTp4 { 
    private final String tipo;
    private final int linea;
    private final String descripcion;

    public ErrorTp4(String tipo, int linea, String descripcion) {
        this.tipo = tipo;
        this.linea = linea;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        String ubicacion = linea > 0 ? "Línea " + linea + ": " : "";
        return "[Error " + tipo + "] " + ubicacion + descripcion;
    }
}

