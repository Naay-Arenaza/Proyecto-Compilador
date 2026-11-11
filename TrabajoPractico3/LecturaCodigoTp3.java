package TrabajoPractico3;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LecturaCodigoTp3 {
    private BufferedReader reader; 
    public static final char EOF = (char) 65535; 
    private int lineaActual = 1; 
    private char ultimoCaracterLeido = ' ';
    private final RecolectorErroresTp3 recolectorErrores;
    private boolean errorLecturaRegistrado = false;

    public LecturaCodigoTp3(String rutaArchivo) throws IOException {
        this(rutaArchivo, new RecolectorErroresTp3());
    }

    public LecturaCodigoTp3(String rutaArchivo, RecolectorErroresTp3 recolectorErrores) throws IOException {
        if (recolectorErrores == null) {
            throw new IllegalArgumentException("El recolector de errores no puede ser null.");
        }
        this.recolectorErrores = recolectorErrores;
        this.reader = new BufferedReader(new FileReader(rutaArchivo));
    }

    public char leerSiguienteCaracter() { //Lee un solo carácter guardando la marca previa para retroceder
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
            registrarErrorLectura("No se pudo leer el archivo: " + e.getMessage());
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
            registrarErrorLectura("Error al retroceder puntero: " + e.getMessage());
        }
    }

    public int getLineaActual() {
        return lineaActual;
    }

    public void cerrar() { 
        try {
            if (reader != null) reader.close();
        } catch (IOException e) {
                registrarErrorLectura("No se pudo cerrar el archivo: " + e.getMessage());
        }
    }

    public RecolectorErroresTp3 getRecolectorErrores() {
        return recolectorErrores;
    }

    private void registrarErrorLectura(String descripcion) {
        if (!errorLecturaRegistrado) {
            recolectorErrores.agregarError("Lectura",lineaActual,descripcion);
            errorLecturaRegistrado = true;
        }
    }
}
