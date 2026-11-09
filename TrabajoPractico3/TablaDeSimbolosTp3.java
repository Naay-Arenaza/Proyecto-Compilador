package TrabajoPractico3;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class TablaDeSimbolosTp3 {

    private final Map<String, RegistroSimboloTp3> tabla = new HashMap<>();

    private final Stack<String> pilaAmbitos = new Stack<>();

    public void agregarIdentificador(String lexema, int linea) {
        if (!tabla.containsKey(lexema)) {
            RegistroSimboloTp3 nuevo = new RegistroSimboloTp3(lexema, "IDENTIFICADOR", linea);
            tabla.put(lexema, nuevo);
        }
    }

    public void agregarConstante(String lexema, String tipoDato, int linea) {
        if (!tabla.containsKey(lexema)) {
            RegistroSimboloTp3 nuevo = new RegistroSimboloTp3(lexema, "CONSTANTE", linea);
            nuevo.setTipo(tipoDato);
            tabla.put(lexema, nuevo);
        }
    }

    public boolean contiene(String lexema) {
        return tabla.containsKey(lexema);
    }

    public RegistroSimboloTp3 obtener(String lexema) {
        return tabla.get(lexema);
    }

    public Collection<RegistroSimboloTp3> obtenerTodos() {
        return tabla.values();
    }

    public void imprimirTabla() {
        System.out.println("\n====================== TABLA DE SÍMBOLOS ======================");
        for (RegistroSimboloTp3 reg : tabla.values()) {
            System.out.println(reg);
        }
        System.out.println("===============================================================");
    }
}