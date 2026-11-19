package TrabajoPractico4.GeneracionWebAssembly;

import TrabajoPractico4.RegistroSimboloTp4;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class GeneradorWasm {
    private static int auxCount = 0;
    private static int labelCount = 0;
    private static int memoriaOffset = 0;

    private static final Map<String, Integer> direccionesObjetos =
            new LinkedHashMap<>();

    private static final Map<String, Integer> desplazamientosAtributos =
            new LinkedHashMap<>();

    private static final Map<String, Integer> tamaniosClases =
            new LinkedHashMap<>();

    private static boolean dentroDeFuncion = false;
    private static String nombreBanderaFuncionActual = null;

    private static final Set<String> globalesDeclaradas =
            new LinkedHashSet<>();

    private static final StringBuilder seccionGlobales =
            new StringBuilder();

    private static final Set<String> localesFuncionActual =
            new LinkedHashSet<>();

    private static final StringBuilder declaracionesLocalesActual =
            new StringBuilder();

    private static final StringBuilder cuerpoFuncionActual =
            new StringBuilder();

    private static final StringBuilder seccionFunciones =
            new StringBuilder();

    private static final Map<String, Integer> stringsData =
            new LinkedHashMap<>();

    private static final Map<String, String> tiposGlobales =
            new LinkedHashMap<>();

    private static final Deque<ContextoFuncion> pilaContextosFuncion =
            new ArrayDeque<>();

    private static final Map<Integer, Map<String, String>> camposPorObjeto =
            new LinkedHashMap<>();

    private static final Map<String, Integer> idsPorReferenciaObjeto =
            new LinkedHashMap<>();

    private static final class ContextoFuncion {
        final int contadorAuxiliares;
        final String banderaFuncion;
        final Set<String> locales;
        final String declaracionesLocales;
        final String cuerpo;

        ContextoFuncion(
                int contadorAuxiliares,
                String banderaFuncion,
                Set<String> locales,
                String declaracionesLocales,
                String cuerpo) {
            this.contadorAuxiliares = contadorAuxiliares;
            this.banderaFuncion = banderaFuncion;
            this.locales = new LinkedHashSet<>(locales);
            this.declaracionesLocales = declaracionesLocales;
            this.cuerpo = cuerpo;
        }
    }

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

        tiposGlobales.clear();
        pilaContextosFuncion.clear();

        direccionesObjetos.clear();
        desplazamientosAtributos.clear();
        tamaniosClases.clear();

        camposPorObjeto.clear();
        idsPorReferenciaObjeto.clear();
    }

    public static String sanitizarNombre(String nombreCompleto) {
        return "$" + nombreCompleto
                .replace(':', '_')
                .replace('.', '_')
                .replace('-', '_');
    }

    public static String mapearTipo(String tipoLenguaje) {
        if (tipoLenguaje == null) {
            return "i32";
        }
        switch (tipoLenguaje.trim().toUpperCase(Locale.ROOT)) {
            case "DOUBLEF":
                return "f64";
            case "SINGLEF":
                return "f32";
            default:
                return "i32";
        }
    }

    public static boolean esTipoSinSigno(String tipoLenguaje) {
        if (tipoLenguaje == null) {
            return false;
        }
        String t = tipoLenguaje.trim().toUpperCase(Locale.ROOT);
        return "USHORTINT".equals(t)
                || "UINTEGER".equals(t)
                || "ULONGINT".equals(t);
    }

    public static String limpiarLiteralNumerico(
            String lexema,
            String tipoLenguaje) {
        int idx = lexema.indexOf('$');
        String num = idx >= 0
                ? lexema.substring(0, idx)
                : lexema;

        if ("DOUBLEF".equalsIgnoreCase(tipoLenguaje) || "SINGLEF".equalsIgnoreCase(tipoLenguaje)) {
            num = num.replace('d', 'e').replace('D', 'e');
            if (num.startsWith("-.")) num = "-0." + num.substring(2);
            else if (num.startsWith(".")) num = "0" + num;
            if (num.endsWith(".")) num = num + "0";
        }
        return num;
    }

    public static void iniciarAmbitoFuncion(String nombreBanderaActiva) {
        if (dentroDeFuncion) {
            pilaContextosFuncion.push(
                    new ContextoFuncion(
                            auxCount,
                            nombreBanderaFuncionActual,
                            localesFuncionActual,
                            declaracionesLocalesActual.toString(),
                            cuerpoFuncionActual.toString()
                    )
            );
        }

        dentroDeFuncion = true;
        nombreBanderaFuncionActual = nombreBanderaActiva;
        auxCount = 0;

        localesFuncionActual.clear();
        declaracionesLocalesActual.setLength(0);
        cuerpoFuncionActual.setLength(0);
    }

    public static void finalizarAmbitoFuncion(String firmaWasm) {
        seccionFunciones
                .append("  ")
                .append(firmaWasm)
                .append("\n");

        seccionFunciones.append(declaracionesLocalesActual);
        seccionFunciones.append(cuerpoFuncionActual);
        seccionFunciones.append("  )\n\n");

        if (!pilaContextosFuncion.isEmpty()) {
            ContextoFuncion anterior = pilaContextosFuncion.pop();
            dentroDeFuncion = true;
            nombreBanderaFuncionActual = anterior.banderaFuncion;
            auxCount = anterior.contadorAuxiliares;

            localesFuncionActual.clear();
            localesFuncionActual.addAll(anterior.locales);

            declaracionesLocalesActual.setLength(0);
            declaracionesLocalesActual.append(anterior.declaracionesLocales);

            cuerpoFuncionActual.setLength(0);
            cuerpoFuncionActual.append(anterior.cuerpo);
        } else {
            dentroDeFuncion = false;
            nombreBanderaFuncionActual = null;
            auxCount = 0;

            localesFuncionActual.clear();
            declaracionesLocalesActual.setLength(0);
            cuerpoFuncionActual.setLength(0);
        }
    }

    public static String getNombreBanderaFuncionActual() {
        return nombreBanderaFuncionActual;
    }

    public static String nuevaAuxiliar(String tipoWasm) {
        String nombre = "$aux" + (auxCount++);
        if (dentroDeFuncion) {
            declaracionesLocalesActual
                    .append("    (local ")
                    .append(nombre)
                    .append(" ")
                    .append(tipoWasm)
                    .append(")\n");
        } else {
            String nombreGlobal = "$aux_main_" + (auxCount - 1);
            declararGlobalSiNoExiste(nombreGlobal, tipoWasm);
            return nombreGlobal;
        }
        return nombre;
    }

    public static void declararParametroLocal(
            String nombreWasm,
            String tipoWasm) {
        localesFuncionActual.add(nombreWasm);
    }

    public static void registrarParametroEnGlobal(
            String nombreParamWasm,
            String nombreGlobalWasm,
            String tipoWasm) {
        declararGlobalSiNoExiste(nombreGlobalWasm, tipoWasm);
        emitir("local.get " + nombreParamWasm);
        emitir("global.set " + nombreGlobalWasm);
    }

    public static void registrarVariableSimbolo(
            RegistroSimboloTp4 reg) {
        if (reg == null) {
            return;
        }
        String nombreWasm = sanitizarNombre(reg.getNombreCompleto());
        String tipoWasm = mapearTipo(reg.getTipo());
        declararGlobalSiNoExiste(nombreWasm, tipoWasm);
    }

    public static void declararGlobalSiNoExiste(
            String nombreWasm,
            String tipoWasm) {
        if (nombreWasm == null || nombreWasm.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de una global WebAssembly no puede estar vacío."
            );
        }

        String tipoNormalizado =
                (tipoWasm == null || tipoWasm.isBlank())
                        ? "i32"
                        : tipoWasm;

        String tipoAnterior =
                tiposGlobales.putIfAbsent(nombreWasm, tipoNormalizado);

        if (tipoAnterior != null && !tipoAnterior.equals(tipoNormalizado)) {
            throw new IllegalStateException(
                    "La global " + nombreWasm
                            + " fue declarada con tipos incompatibles: "
                            + tipoAnterior + " y " + tipoNormalizado + "."
            );
        }

        if (globalesDeclaradas.add(nombreWasm)) {
            String cero =
                    "f64".equals(tipoNormalizado) || "f32".equals(tipoNormalizado)
                            ? "0.0"
                            : "0";

            seccionGlobales
                    .append("  (global ")
                    .append(nombreWasm)
                    .append(" (mut ")
                    .append(tipoNormalizado)
                    .append(") (")
                    .append(tipoNormalizado)
                    .append(".const ")
                    .append(cero)
                    .append("))\n");
        }
    }

    public static boolean esLocalActual(String nombreWasm) {
        return dentroDeFuncion && localesFuncionActual.contains(nombreWasm);
    }

    public static void emitirGet(String nombreWasm) {
        if (nombreWasm == null || nombreWasm.isBlank()) {
            throw new IllegalArgumentException(
                    "No se puede leer una referencia WebAssembly vacía."
            );
        }

        if (esLocalActual(nombreWasm) || esAuxiliarLocal(nombreWasm)) {
            emitir("local.get " + nombreWasm);
        } else {
            if (!tiposGlobales.containsKey(nombreWasm)) {
                throw new IllegalStateException(
                        "Se intentó leer una global WebAssembly no declarada: "
                                + nombreWasm
                );
            }
            emitir("global.get " + nombreWasm);
        }
    }

    public static void emitirSet(String nombreWasm) {
        if (nombreWasm == null || nombreWasm.isBlank()) {
            throw new IllegalArgumentException(
                    "No se puede escribir una referencia WebAssembly vacía."
            );
        }

        if (esLocalActual(nombreWasm) || esAuxiliarLocal(nombreWasm)) {
            emitir("local.set " + nombreWasm);
        } else {
            if (!tiposGlobales.containsKey(nombreWasm)) {
                throw new IllegalStateException(
                        "Se intentó escribir una global WebAssembly no declarada: "
                                + nombreWasm
                );
            }
            emitir("global.set " + nombreWasm);
        }
    }

    public static String nuevaEtiqueta() {
        return "$L" + (labelCount++);
    }

    public static void emitir(String instruccion) {
        cuerpoFuncionActual
                .append("    ")
                .append(instruccion)
                .append("\n");
    }

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

    private static String escaparBytesWat(String texto) {
        StringBuilder salida = new StringBuilder();
        for (byte b : texto.getBytes(StandardCharsets.UTF_8)) {
            salida.append('\\');
            String hex = Integer.toHexString(b & 0xFF).toUpperCase(Locale.ROOT);
            if (hex.length() == 1) {
                salida.append('0');
            }
            salida.append(hex);
        }
        return salida.toString();
    }

    public static String obtenerCodigoWatCompleto() {
        StringBuilder wat = new StringBuilder();

        wat.append("(module\n");
        wat.append("  (import \"env\" \"print_i32\" (func $print_i32 (param i32)))\n");
        wat.append("  (import \"env\" \"print_f64\" (func $print_f64 (param f64)))\n");
        wat.append("  (import \"env\" \"print_str\" (func $print_str (param i32)))\n");
        wat.append("  (import \"env\" \"error_runtime\" (func $error_runtime (param i32)))\n\n");

        int paginas = Math.max(1, (memoriaOffset + 65535) / 65536);
        wat.append("  (memory (export \"memory\") ")
                .append(paginas)
                .append(")\n\n");

        for (Map.Entry<String, Integer> entry : stringsData.entrySet()) {
            wat.append("  (data (i32.const ")
                    .append(entry.getValue())
                    .append(") \"");
            wat.append(escaparBytesWat(entry.getKey()));
            wat.append("\\00\")\n");
        }

        wat.append("\n")
                .append(seccionGlobales)
                .append("\n");

        wat.append(seccionFunciones);
        wat.append(")\n");

        return wat.toString();
    }

    public static void registrarObjeto(
            int id,
            String referenciaObjeto,
            Map<String, String> campos) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID de objeto debe ser positivo.");
        }
        idsPorReferenciaObjeto.put(referenciaObjeto, id);
        camposPorObjeto.put(id, new LinkedHashMap<>(campos));
        declararGlobalSiNoExiste(referenciaObjeto, "i32");
    }

    public static void emitirInicializacionObjetos() {
        for (Map.Entry<String, Integer> e : idsPorReferenciaObjeto.entrySet()) {
            emitir("i32.const " + e.getValue());
            emitirSet(e.getKey());
        }
    }

    public static boolean esAtributo(RegistroSimboloTp4 registro) {
        return registro != null && "ATRIBUTO".equals(registro.getUso());
    }

    public static void emitirGetAtributo(RegistroSimboloTp4 atributo) {
        String clave = atributo.getNombreCompleto();
        String tipo = mapearTipo(atributo.getTipo());
        List<Integer> ids = new ArrayList<>();

        for (Map.Entry<Integer, Map<String, String>> e : camposPorObjeto.entrySet()) {
            if (e.getValue().containsKey(clave)) {
                ids.add(e.getKey());
            }
        }

        if (ids.isEmpty()) {
            throw new IllegalStateException("No hay instancias con el atributo " + clave);
        }

        for (int id : ids) {
            emitir("local.get $this");
            emitir("i32.const " + id);
            emitir("i32.eq");
            emitir("if (result " + tipo + ")");
            emitirGet(camposPorObjeto.get(id).get(clave));
            emitir("else");
        }

        emitirErrorRuntime("Error en ejecucion: atributo no disponible para el objeto.");

        for (int ignored : ids) {
            emitir("end");
        }
    }

    public static void emitirSetAtributo(
            RegistroSimboloTp4 atributo,
            String auxiliarValor) {
        String clave = atributo.getNombreCompleto();
        List<Integer> ids = new ArrayList<>();

        for (Map.Entry<Integer, Map<String, String>> e : camposPorObjeto.entrySet()) {
            if (e.getValue().containsKey(clave)) {
                ids.add(e.getKey());
            }
        }

        if (ids.isEmpty()) {
            throw new IllegalStateException("No hay instancias con el atributo " + clave);
        }

        for (int id : ids) {
            emitir("local.get $this");
            emitir("i32.const " + id);
            emitir("i32.eq");
            emitir("if");
            emitirGet(auxiliarValor);
            emitirSet(camposPorObjeto.get(id).get(clave));
            emitir("else");
        }

        emitirErrorRuntime("Error en ejecucion: atributo no disponible para el objeto.");

        for (int ignored : ids) {
            emitir("end");
        }
    }

    private static boolean esAuxiliarLocal(String nombreWasm) {
        if (nombreWasm == null) return false;
        if (nombreWasm.startsWith("$aux_main_")) return false;
        if (!nombreWasm.startsWith("$aux")) return false;
        if (nombreWasm.length() > 4) {
            char c = nombreWasm.charAt(4);
            return Character.isDigit(c);
        }
        return false;
    }
}