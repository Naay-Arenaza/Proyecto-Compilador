import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LecturaCodigo {
    private BufferedReader reader; 
    public static final char EOF = (char) 65535; 
    private int lineaActual = 1; 
    private char ultimoCaracterLeido = ' ';

    public LecturaCodigo(String rutaArchivo) throws IOException {
        this.reader = new BufferedReader(new FileReader(rutaArchivo));
    }

    //Lee un solo carácter guardando la marca previa para retroceder
    public char leerSiguienteCaracter() {
        try {
            reader.mark(1); 
            int valor = reader.read(); 
            if (valor == -1) { 
                return EOF;
            }
            char c = (char) valor;
            ultimoCaracterLeido = c;
            if (c == '\n') {
                lineaActual++;
            }
            return c; 
        } catch (IOException e) {
            return EOF;
        }
    }
    
    public void devolverCaracter() {
        try {
            if (ultimoCaracterLeido == '\n') {
                lineaActual--;
            }
            reader.reset(); 
        } catch (IOException e) {
            System.err.println("Error al retroceder puntero: " + e.getMessage());
        }
    }

    public int getLineaActual() {
        return lineaActual;
    }

    public void cerrar() { 
        try {
            if (reader != null) reader.close();
        } catch (IOException ignored) {}
    }
}
