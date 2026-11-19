package TrabajoPractico4.GeneracionWebAssembly;
import TrabajoPractico4.RegistroSimboloTp4;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GeneradorWasm {
    private static int auxCount = 0;
    private static int labelCount = 0;
    private static int memoriaOffset = 0;
    private static final Map<String, Integer> direccionesObjetos = new LinkedHashMap<>();
    private static final Map<String, Integer> desplazamientosAtributos =  new LinkedHashMap<>();
    private static final Map<String, Integer> tamaniosClases = new LinkedHashMap<>();
    private static boolean dentroDeFuncion = false;
    private static String nombreBanderaFuncionActual = null;
    private static final Set<String> globalesDeclaradas = new LinkedHashSet<>();
    private static final StringBuilder seccionGlobales = new StringBuilder();
    private static final Set<String> localesFuncionActual = new LinkedHashSet<>();
    private static final StringBuilder declaracionesLocalesActual = new StringBuilder();
    private static final StringBuilder cuerpoFuncionActual = new StringBuilder();
    private static final StringBuilder seccionFunciones = new StringBuilder();
    private static final Map<String, Integer> stringsData = new LinkedHashMap<>();

    public static void reiniciar() {
        auxCount = 0;
        labelCount = 0;
        memoriaOffset = 0;
        dentroDeFuncion = false;
        nombreBanderaFuncionActual = null;
        globalesDeclaradas.clear();
        seccionGlobales.setLength(0);
        localesFuncionActual.clear();
        declaracionesLocalesActual.setLength(0);
        cuerpoFuncionActual.setLength(0);
        seccionFunciones.setLength(0);
        stringsData.clear();
        direccionesObjetos.clear();
        desplazamientosAtributos.clear();
        tamaniosClases.clear();
    }

    public static String sanitizarNombre(String nombreCompleto) {
        return "$" + nombreCompleto.replace(':', '_').replace('.', '_').replace('-', '_');
    }

    public static String mapearTipo(String tipoLenguaje) {
        if (tipoLenguaje == null) return "i32";
        switch (tipoLenguaje.trim().toUpperCase(Locale.ROOT)) {
            case "DOUBLEF": return "f64";
            case "SINGLEF": return "f32";
            default: return "i32";
        }
    }

    public static boolean esTipoSinSigno(String tipoLenguaje) {
        if (tipoLenguaje == null) return false;
        String t = tipoLenguaje.trim().toUpperCase(Locale.ROOT);
        return "USHORTINT".equals(t) || "UINTEGER".equals(t) || "ULONGINT".equals(t);
    }

    public static String limpiarLiteralNumerico(String lexema, String tipoLenguaje) {
        int idx = lexema.indexOf('$');
        String num = idx >= 0 ? lexema.substring(0, idx) : lexema;
        if ("DOUBLEF".equalsIgnoreCase(tipoLenguaje) || "SINGLEF".equalsIgnoreCase(tipoLenguaje)) {
            num = num.replace('d', 'e').replace('D', 'e');
            if (num.startsWith(".")) num = "0" + num;
            if (num.endsWith(".")) num = num + "0";
        }
        return num;
    }

    public static void iniciarAmbitoFuncion(String nombreBanderaActiva) {
        dentroDeFuncion = true;
        nombreBanderaFuncionActual = nombreBanderaActiva;
        auxCount = 0;
        localesFuncionActual.clear();
        declaracionesLocalesActual.setLength(0);
        cuerpoFuncionActual.setLength(0);
    }

    public static void finalizarAmbitoFuncion(String firmaWasm) {
        seccionFunciones.append("  ").append(firmaWasm).append("\n");
        seccionFunciones.append(declaracionesLocalesActual);
        seccionFunciones.append(cuerpoFuncionActual);
        seccionFunciones.append("  )\n\n");
        dentroDeFuncion = false;
        nombreBanderaFuncionActual = null;
    }

    public static String getNombreBanderaFuncionActual() {return nombreBanderaFuncionActual;}

    public static String nuevaAuxiliar(String tipoWasm) {
        String nombre = "$aux" + (auxCount++);
        if (dentroDeFuncion) {
            declaracionesLocalesActual.append("    (local ").append(nombre).append(" ").append(tipoWasm).append(")\n");
        } else {
            String nombreGlobal = "$aux_main_" + (auxCount - 1);
            declararGlobalSiNoExiste(nombreGlobal, tipoWasm);
            return nombreGlobal;
        }
        return nombre;
    }

    public static void declararParametroLocal(String nombreWasm, String tipoWasm) { localesFuncionActual.add(nombreWasm); }

    public static void registrarVariableSimbolo(RegistroSimboloTp4 reg) {
        if (reg == null) return;
        String nombreWasm = sanitizarNombre(reg.getNombreCompleto());
        String tipoWasm = mapearTipo(reg.getTipo());
        if (dentroDeFuncion && !"main".equals(reg.getAmbito())) {
            if (!localesFuncionActual.contains(nombreWasm)) {
                localesFuncionActual.add(nombreWasm);
                declaracionesLocalesActual.append("    (local ").append(nombreWasm).append(" ").append(tipoWasm).append(")\n");
            }
        } else {
            declararGlobalSiNoExiste(nombreWasm, tipoWasm);
        }
    }

    public static void declararGlobalSiNoExiste(String nombreWasm, String tipoWasm) {
        if (globalesDeclaradas.add(nombreWasm)) {
            String cero = "f64".equals(tipoWasm) ? "0.0" : ("f32".equals(tipoWasm) ? "0.0" : "0");
            seccionGlobales.append("  (global ").append(nombreWasm).append(" (mut ").append(tipoWasm).append(") (").append(tipoWasm).append(".const ").append(cero).append("))\n");
        }
    }

    public static boolean esLocalActual(String nombreWasm) {
        return dentroDeFuncion && localesFuncionActual.contains(nombreWasm);
    }

    public static void emitirGet(String nombreWasm) {
            if (esLocalActual(nombreWasm) || (nombreWasm.startsWith("$aux") && !nombreWasm.startsWith("$aux_main_"))) {
                emitir("local.get " + nombreWasm);
            } else {
                declararGlobalSiNoExiste(nombreWasm, "i32");
                emitir("global.get " + nombreWasm);
            }
        }

    public static void emitirSet(String nombreWasm) {
        if (esLocalActual(nombreWasm) || (nombreWasm.startsWith("$aux") && !nombreWasm.startsWith("$aux_main_"))) {
            emitir("local.set " + nombreWasm);
        } else {
            declararGlobalSiNoExiste(nombreWasm, "i32");
            emitir("global.set " + nombreWasm);
        }
    }

    public static String nuevaEtiqueta() {return "$L" + (labelCount++);}

    public static void emitir(String instruccion) { cuerpoFuncionActual.append("    ").append(instruccion).append("\n");}

    public static int registrarString(String texto) {
        if (stringsData.containsKey(texto)) {
            return stringsData.get(texto);
        }
        int offset = memoriaOffset;
        stringsData.put(texto, offset);
        memoriaOffset += texto.getBytes(StandardCharsets.UTF_8).length + 1;
        return offset;
    }

    public static void emitirErrorRuntime(String mensaje) {
        int offset = registrarString(mensaje);
        emitir("i32.const " + offset);
        emitir("call $error_runtime");
        emitir("unreachable");
    }

    public static String obtenerCodigoWatCompleto() {
        StringBuilder wat = new StringBuilder();
        wat.append("(module\n");
        wat.append("  (import \"env\" \"print_i32\" (func $print_i32 (param i32)))\n");
        wat.append("  (import \"env\" \"print_f64\" (func $print_f64 (param f64)))\n");
        wat.append("  (import \"env\" \"print_str\" (func $print_str (param i32)))\n");
        wat.append("  (import \"env\" \"error_runtime\" (func $error_runtime (param i32)))\n");
        wat.append("  (memory (export \"memory\") 1)\n\n");

        for (Map.Entry<String, Integer> entry : stringsData.entrySet()) {
            String escapado = entry.getKey().replace("\\", "\\\\").replace("\"", "\\\"");
            wat.append("  (data (i32.const ").append(entry.getValue()).append(") \"").append(escapado).append("\\00\")\n");
        }
        wat.append("\n").append(seccionGlobales).append("\n");
        wat.append(seccionFunciones);
        wat.append(")\n");
        return wat.toString();
    }
}
