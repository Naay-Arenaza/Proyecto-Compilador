import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TablaDeSimbolos {
    private final Map<String, RegistroSimbolo> tabla = new HashMap<>();

    public void agregarIdentificador(String lexema, int linea) {
        if (!tabla.containsKey(lexema)) {
            RegistroSimbolo nuevo = new RegistroSimbolo(lexema, "IDENTIFICADOR");
            nuevo.agregarAtributo("linea", linea);
            tabla.put(lexema, nuevo);
        }
    }

    public void agregarConstante(String lexema, String tipoDato, int linea) {
        if (!tabla.containsKey(lexema)) {
            RegistroSimbolo nuevo = new RegistroSimbolo(lexema, "CONSTANTE");
            nuevo.agregarAtributo("tipoDato", tipoDato);
            nuevo.agregarAtributo("linea", linea);
            tabla.put(lexema, nuevo);
        }
    }

    public boolean contiene(String lexema) {
        return tabla.containsKey(lexema);
    }

    public RegistroSimbolo obtener(String lexema) {
        return tabla.get(lexema);
    }

    public Collection<RegistroSimbolo> obtenerTodos() {
        return tabla.values();
    }

    public void imprimirTabla() {
        System.out.println("\n====================== TABLA DE SÍMBOLOS ======================");
        for (RegistroSimbolo reg : tabla.values()) {
            System.out.println(reg);
        }
        System.out.println("===============================================================");
    }
}