
package TrabajoPractico3;

public class ErrorTp3 { // Representa un error detectado durante el análisis del programa.

    private final String tipo;
    private final int linea;
    private final String descripcion;

    public ErrorTp3(String tipo, int linea, String descripcion) {
        this.tipo = tipo;
        this.linea = linea;
        this.descripcion = descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public int getLinea() {
        return linea;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        String ubicacion = linea > 0
                ? "Línea " + linea + ": "
                : "";

        return "[Error " + tipo + "] " + ubicacion + descripcion;
    }
}

