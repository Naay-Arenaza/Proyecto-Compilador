import java.util.HashMap;
import java.util.Map;

public class TablaPalabrasReservadas {
    private final Map<String, Short> tablaPR = new HashMap<>();

    public TablaPalabrasReservadas() {
        precargarPalabras();
    }

    private void precargarPalabras() {
        // IDs asignados a cada palabra reservada según la gramática
        tablaPR.put("if", Parser.IF);
        tablaPR.put("else", Parser.ELSE);
        tablaPR.put("begin", Parser.BEGIN);
        tablaPR.put("end_if",Parser.END_IF);
        tablaPR.put("end", Parser.END);
        tablaPR.put("pout", Parser.POUT);
        tablaPR.put("ret", Parser.RET);
        tablaPR.put("function", Parser.FUNCTION);
        tablaPR.put("class", Parser.CLASS);
        tablaPR.put("ushortint",Parser.USHORTINT);
        tablaPR.put("doublef",Parser.DOUBLEF);
        tablaPR.put("todf",Parser.TODF);
        tablaPR.put("comptime",Parser.COMPTIME);
        tablaPR.put("REPEAT",Parser.REPEAT);
        tablaPR.put("while",Parser.WHILE);
        tablaPR.put("extends",Parser.EXTENDS);
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
        return this.tablaPR.getOrDefault(palabra.toLowerCase(),(short) -1);
    }

    public void imprimirTabla() {
        System.out.println("\n================ TABLA DE PALABRAS RESERVADAS ================");
        System.out.printf("%-15s | %-10s%n", "PALABRA", "TOKEN ID");
        System.out.println("-------------------------------------------------------------");

        for (Map.Entry<String, Short> entrada : this.tablaPR.entrySet()) {
            System.out.printf("%-15s | %-10d%n", entrada.getKey(), entrada.getValue());
        }

        System.out.println("=============================================================\n");
    }
}