package TrabajoPractico3;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Mantiene la pila de ámbitos léxicos y sincroniza el ámbito activo con la
 * tabla de símbolos. El formato coincide con el TP3: main:funcion:subfuncion.
 */
public class GestionAmbitoTp3 {

    private final TablaDeSimbolosTp3 tabla;
    private final Deque<String> pila = new ArrayDeque<>();

    public GestionAmbitoTp3(TablaDeSimbolosTp3 tabla) {
        if (tabla == null) throw new IllegalArgumentException("La tabla de símbolos es obligatoria.");
        this.tabla = tabla;
        pila.push("main");
        tabla.setAmbitoActual("main");
    }

    public final void iniciarPrograma(String nombrePrograma) {
        if (nombrePrograma == null || nombrePrograma.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del programa no puede ser vacío.");
        }
        pila.clear();
        pila.push(nombrePrograma);
        sincronizarTabla();
    }

    /** Entra a una función, método o clase anidada en el ámbito actual. */
    public final String entrarAmbito(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del nuevo ámbito no puede ser vacío.");
        }
        pila.push(getAmbitoActual() + ":" + nombre);
        sincronizarTabla();
        return getAmbitoActual();
    }

    /** Sale del ámbito actual. El ámbito raíz no puede quitarse. */
    public final String salirAmbito() {
        if (pila.size() <= 1) {
            throw new IllegalStateException("No se puede salir del ámbito raíz del programa.");
        }
        pila.pop();
        sincronizarTabla();
        return getAmbitoActual();
    }

    public final String getAmbitoActual() {
        return pila.isEmpty() ? "main" : pila.peek();
    }

    public String getAmbitoPadre() {
        String actual = getAmbitoActual();
        int separador = actual.lastIndexOf(':');
        return separador < 0 ? null : actual.substring(0, separador);
    }

    public boolean esAmbitoRaiz() { return pila.size() <= 1; }

    public RegistroSimboloTp3 declararVariable(String nombre, String tipo, int linea, boolean comptime) {
        return tabla.declararVariable(nombre, tipo, linea, comptime);
    }

    public RegistroSimboloTp3 declararFuncion(String nombre, String tipoRetorno, int linea) {
        return tabla.declararFuncion(nombre, tipoRetorno, linea);
    }

    public RegistroSimboloTp3 declararClase(String nombre, int linea) {
        return tabla.declararClase(nombre, linea);
    }

    public RegistroSimboloTp3 declararParametro(String nombre, String tipo, int linea) {
        return tabla.declararParametro(nombre, tipo, linea);
    }

    public RegistroSimboloTp3 resolver(String nombre) {
        return tabla.buscar(nombre, getAmbitoActual());
    }

    private final void sincronizarTabla() {
        tabla.setAmbitoActual(getAmbitoActual());
    }
}