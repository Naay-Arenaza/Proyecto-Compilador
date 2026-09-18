import java.util.HashMap;
import java.util.Map;

public class TablaPalabrasReservadas {
    private final Map<String, Integer> tablaPR = new HashMap<>();

    public TablaPalabrasReservadas() {
        precargarPalabras();
    }

    private void precargarPalabras() {
        tablaPR.put("IF", 59);
        tablaPR.put("else", 61);
        tablaPR.put("begin", 62);
        tablaPR.put("END_if", 63);
        tablaPR.put("end", 63);
        tablaPR.put("POUT", 65);
        tablaPR.put("RET", 66);
        tablaPR.put("function", 67);
        tablaPR.put("class", 68);
        tablaPR.put("USHORTINT",69);
        tablaPR.put("DOUBLEF",70);
    }

    public boolean esPalabraReservada(String palabra) {
        if (palabra == null) {
            return false;
        }
        return this.tablaPR.containsKey(palabra);
    }

    public int obtenerId(String palabra) {
        if (palabra == null) {
            return -1;
        }
        return this.tablaPR.getOrDefault(palabra, -1);
    }

    public void imprimirTabla() {
        System.out.println("\n================ TABLA DE PALABRAS RESERVADAS ================");
        System.out.printf("%-15s | %-10s%n", "PALABRA", "TOKEN ID");
        System.out.println("-------------------------------------------------------------");

        for (Map.Entry<String, Integer> entrada : this.tablaPR.entrySet()) {
            System.out.printf("%-15s | %-10d%n", entrada.getKey(), entrada.getValue());
        }

        System.out.println("=============================================================\n");
    }
}