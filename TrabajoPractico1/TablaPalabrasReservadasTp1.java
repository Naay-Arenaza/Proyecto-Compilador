import java.util.HashMap;
import java.util.Map;

public class TablaPalabrasReservadasTp1 {
    private final Map<String, Integer> tablaPR = new HashMap<>();

    public TablaPalabrasReservadasTp1() {
        precargarPalabras();
    }

    private void precargarPalabras() {
        tablaPR.put("if", 100);
        tablaPR.put("else", 101);
        tablaPR.put("begin", 102);
        tablaPR.put("end_if", 103);
        tablaPR.put("end", 104);
        tablaPR.put("pout", 105);
        tablaPR.put("ret", 106);
        tablaPR.put("function", 107);
        tablaPR.put("class", 108);
        tablaPR.put("ushortint",109);
        tablaPR.put("doublef",110);
    }

    public boolean esPalabraReservada(String palabra) {
        if (palabra == null) {
            return false;
        }
        return this.tablaPR.containsKey(palabra.toLowerCase());
    }

    public int obtenerId(String palabra) {
        if (palabra == null) {
            return -1;
        }
        return this.tablaPR.getOrDefault(palabra.toLowerCase(), -1);
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