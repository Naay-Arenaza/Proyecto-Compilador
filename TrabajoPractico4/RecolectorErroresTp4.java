package TrabajoPractico4;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecolectorErroresTp4 { 
    private final List<ErrorTp4> errores = new ArrayList<>();

    public synchronized void agregarError(String tipo,int linea,  String descripcion) { 
        errores.add(new ErrorTp4(tipo, linea, descripcion));
    }

    public synchronized List<ErrorTp4> getErrores() { 
        return Collections.unmodifiableList(
                new ArrayList<>(errores)
        );
    }

    public synchronized boolean hayErrores() {  return !errores.isEmpty(); }

    public void imprimirErrores(PrintStream salida) { 
        List<ErrorTp4> copia = getErrores();
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                         Errores");
        System.out.println("------------------------------------------------------------");
        if (copia.isEmpty()) {
            System.out.println();
            salida.println("No se encontraron errores.");
            System.out.println();
            System.out.println("------------------------------------------------------------");
            return;
        }
        System.out.println();
        for (int i = 0; i < copia.size(); i++) {
            salida.println((i + 1) + ". " + copia.get(i));
        }
        System.out.println();
        System.out.println("------------------------------------------------------------");
        salida.println("Total de errores: " + copia.size());
    }
}
