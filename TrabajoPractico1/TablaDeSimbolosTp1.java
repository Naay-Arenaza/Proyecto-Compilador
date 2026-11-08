import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TablaDeSimbolosTp1 {
    private final Map<String, RegistroSimboloTp1> tabla = new HashMap<>();

    public void agregarIdentificador(String lexema, int linea) {
        if (!tabla.containsKey(lexema)) {
            RegistroSimboloTp1 nuevo = new RegistroSimboloTp1(lexema, "IDENTIFICADOR");
            nuevo.agregarAtributo("linea", linea);
            tabla.put(lexema, nuevo);
        }
    }

    public void agregarConstante(String lexema, String tipoDato, int linea) {
        if (!tabla.containsKey(lexema)) {
            RegistroSimboloTp1 nuevo = new RegistroSimboloTp1(lexema, "CONSTANTE");
            nuevo.agregarAtributo("tipoDato", tipoDato);
            nuevo.agregarAtributo("linea", linea);
            tabla.put(lexema, nuevo);
        }
    }

    public boolean contiene(String lexema) {
        return tabla.containsKey(lexema);
    }

    public RegistroSimboloTp1 obtener(String lexema) {
        return tabla.get(lexema);
    }

    public Collection<RegistroSimboloTp1> obtenerTodos() {
        return tabla.values();
    }

    public void imprimirTabla() {
        System.out.println("\n====================== TABLA DE SÍMBOLOS ======================");
        for (RegistroSimboloTp1 reg : tabla.values()) {
            System.out.println(reg);
        }
        System.out.println("===============================================================");
    }
}