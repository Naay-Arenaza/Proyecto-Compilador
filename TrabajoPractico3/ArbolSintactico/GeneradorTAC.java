package TrabajoPractico3.ArbolSintactico;
public class GeneradorTAC {
    private static int tempCount = 0;
    private static int labelCount = 0;

    public static String nuevoTemporal() {
        return "T" + (tempCount++);
    }

    public static String nuevaEtiqueta() {
        return "L" + (labelCount++);
    }
}