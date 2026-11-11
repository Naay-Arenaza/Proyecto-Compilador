
package TrabajoPractico3;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecolectorErroresTp3 { // Acumula los errores encontrados durante el análisis.

    private final List<ErrorTp3> errores = new ArrayList<>();

    public synchronized void agregarError(String tipo,int linea,  String descripcion) { // Agrega un error al recolector.
        errores.add(new ErrorTp3(tipo, linea, descripcion));
    }

    public synchronized List<ErrorTp3> getErrores() { // Devuelve una copia no modificable de los errores.
        return Collections.unmodifiableList(
                new ArrayList<>(errores)
        );
    }

    public synchronized boolean hayErrores() { // Indica si se registraron errores.
        return !errores.isEmpty();
    }

    public synchronized int cantidadErrores() { // Devuelve la cantidad de errores registrados.
        return errores.size();
    }

    public void imprimirErrores(PrintStream salida) { // Imprime todos los errores juntos.

        List<ErrorTp3> copia = getErrores();

        salida.println();
        salida.println("========================================");
        salida.println("             ERRORES");
        salida.println("========================================");

        if (copia.isEmpty()) {
            salida.println("No se encontraron errores.");
            return;
        }

        for (int i = 0; i < copia.size(); i++) {
            salida.println((i + 1) + ". " + copia.get(i));
        }

        salida.println("----------------------------------------");
        salida.println("Total de errores: " + copia.size());
    }
}
