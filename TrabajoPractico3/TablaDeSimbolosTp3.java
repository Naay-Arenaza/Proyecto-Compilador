package TrabajoPractico3;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Tabla de símbolos del TP3.
 *
 * Todas las entradas (declaraciones y constantes) se almacenan en un único Map.
 * Las claves internas tienen prefijos para distinguir declaraciones y literales.
 * Las declaraciones usan nombre:ambito; las constantes usan tipo+lexema.
 */
public class TablaDeSimbolosTp3 {

    public static final String USO_VARIABLE = "VARIABLE";
    public static final String USO_COMPTIME = "VARIABLE_COMPTIME";
    public static final String USO_PARAMETRO = "PARAMETRO";
    public static final String USO_FUNCION = "FUNCION";
    public static final String USO_CLASE = "CLASE";
    public static final String USO_OBJETO = "OBJETO";
    public static final String USO_ATRIBUTO = "ATRIBUTO";
    public static final String USO_METODO = "METODO";
    public static final String USO_CONSTANTE = "CONSTANTE";

    private static final String ATR_EXTENDS = "CLASES_PADRE";

    /** Único almacenamiento de la tabla: declaraciones y constantes. */
    private final Map<String, RegistroSimboloTp3> tabla = new LinkedHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);
    private String ambitoActual = "main";

    public void setAmbitoActual(String ambitoActual) {
        if (ambitoActual == null || ambitoActual.trim().isEmpty()) {
            throw new IllegalArgumentException("El ámbito actual no puede ser vacío.");
        }
        this.ambitoActual = ambitoActual;
    }

    public String getAmbitoActual() { return ambitoActual; }

    /**
     * Registra una constante encontrada por el lexer.
     * Es correcto hacer esto durante el análisis léxico porque su tipo se
     * conoce a partir del sufijo o de la forma del literal.
     */
    public RegistroSimboloTp3 agregarConstante(String lexema, String tipoDato, int linea) {
        String clave = construirClaveConstante(tipoDato, lexema);
        RegistroSimboloTp3 existente = tabla.get(clave);
        if (existente != null) {
            return existente;
        }

        RegistroSimboloTp3 nueva = new RegistroSimboloTp3(
                lexema, "CONSTANTE", tipoDato, USO_CONSTANTE,
                "<constantes>", linea, secuencia.incrementAndGet());
        nueva.agregarAtributo("VALOR", lexema);
        tabla.put(clave, nueva);
        return nueva;
    }

    /**
     * Compatibilidad con el método anterior. No debe llamarse desde el lexer:
     * el lexer no conoce todavía el uso ni el ámbito de un identificador.
     */
    @Deprecated
    public void agregarIdentificador(String lexema, int linea) {
        // Intencionalmente no se inserta: una aparición no equivale a una declaración.
    }

    /** Declara un símbolo en el ámbito indicado. Devuelve null si ya existe allí. */
    public RegistroSimboloTp3 declarar(
            String nombre, String tipo, String uso, String ambito, int linea) {

        validarTexto(nombre, "nombre");
        validarTexto(uso, "uso");
        validarTexto(ambito, "ámbito");

        String clave = construirClaveDeclaracion(nombre, ambito);
        if (tabla.containsKey(clave)) {
            return null;
        }

        RegistroSimboloTp3 nuevo = new RegistroSimboloTp3(
                nombre, "IDENTIFICADOR", tipo, uso, ambito,
                linea, secuencia.incrementAndGet());
        tabla.put(clave, nuevo);
        return nuevo;
    }

    public RegistroSimboloTp3 declararEnAmbitoActual(
            String nombre, String tipo, String uso, int linea) {
        return declarar(nombre, tipo, uso, ambitoActual, linea);
    }

    public RegistroSimboloTp3 declararVariable(
            String nombre, String tipo, int linea, boolean comptime) {
        String uso = comptime ? USO_COMPTIME : USO_VARIABLE;
        RegistroSimboloTp3 registro = declararEnAmbitoActual(nombre, tipo, uso, linea);
        if (registro != null && comptime) {
            registro.agregarAtributo("COMPTIME", Boolean.TRUE);
        }
        return registro;
    }

    public RegistroSimboloTp3 declararFuncion(String nombre, String tipoRetorno, int linea) {
        return declararEnAmbitoActual(nombre, tipoRetorno, USO_FUNCION, linea);
    }

    public RegistroSimboloTp3 declararClase(String nombre, int linea) {
        RegistroSimboloTp3 clase = declararEnAmbitoActual(nombre, "CLASS", USO_CLASE, linea);
        if (clase != null) {
            clase.agregarAtributo(ATR_EXTENDS, new ArrayList<String>());
        }
        return clase;
    }

    public RegistroSimboloTp3 declararParametro(
            String nombre, String tipo, int linea) {
        return declararEnAmbitoActual(nombre, tipo, USO_PARAMETRO, linea);
    }

    public RegistroSimboloTp3 declararAtributoClase(
            String nombre, String tipo, int linea) {
        return declararEnAmbitoActual(nombre, tipo, USO_ATRIBUTO, linea);
    }

    public RegistroSimboloTp3 declararMetodoClase(
            String nombre, String tipoRetorno, int linea) {
        return declararEnAmbitoActual(nombre, tipoRetorno, USO_METODO, linea);
    }

    /** Declara un objeto cuyo tipo debe ser una clase visible. */
    public RegistroSimboloTp3 declararObjeto(String nombre, String nombreClase, int linea) {
        RegistroSimboloTp3 clase = buscarClaseVisible(nombreClase, ambitoActual);
        if (clase == null) return null;
        RegistroSimboloTp3 objeto = declararEnAmbitoActual(nombre, nombreClase, USO_OBJETO, linea);
        if (objeto != null) objeto.agregarAtributo("CLASE", clase.getNombreCompleto());
        return objeto;
    }

    /** Agrega la firma formal a la entrada de función o método. */
    public boolean agregarParametroFormal(
            String nombreFuncion, String nombreParametro, String tipoParametro,
            int lineaParametro, String ambitoDeclaracion) {
        RegistroSimboloTp3 funcion = buscarLocal(nombreFuncion, ambitoDeclaracion);
        if (funcion == null || !(USO_FUNCION.equals(funcion.getUso())
                || USO_METODO.equals(funcion.getUso()))) return false;
        funcion.agregarParametro(nombreParametro, tipoParametro, lineaParametro);
        return true;
    }

    /** Indica si ya existe una declaración en el ámbito exacto. */
    public boolean estaDeclaradoLocalmente(String nombre, String ambito) {
        return buscarLocal(nombre, ambito) != null;
    }

    /** Solo devuelve símbolos que pueden usarse como variables/valores. */
    public RegistroSimboloTp3 resolverVariable(String nombre) {
        RegistroSimboloTp3 registro = buscar(nombre);
        if (registro == null) return null;
        String uso = registro.getUso();
        return USO_VARIABLE.equals(uso) || USO_COMPTIME.equals(uso)
                || USO_PARAMETRO.equals(uso) || USO_OBJETO.equals(uso)
                || USO_ATRIBUTO.equals(uso) ? registro : null;
    }

    /** Solo devuelve símbolos invocables. */
    public RegistroSimboloTp3 resolverFuncion(String nombre) {
        RegistroSimboloTp3 registro = buscar(nombre);
        if (registro == null) return null;
        String uso = registro.getUso();
        return USO_FUNCION.equals(uso) || USO_METODO.equals(uso) ? registro : null;
    }

    /** Comprueba la asignación de objetos del tema 25. */
    public boolean tiposObjetoCompatibles(String tipoDestino, String tipoOrigen) {
        if (equalsIgnorandoMayusculas(tipoDestino, tipoOrigen)) return true;
        return estructurasCompatibles(tipoDestino, tipoOrigen);
    }

    public boolean contiene(String nombre) {
        return buscar(nombre, ambitoActual) != null || contieneConstante(nombre);
    }

    /** Busca desde el ámbito actual hacia sus padres, respetando visibilidad. */
    public RegistroSimboloTp3 buscar(String nombre, String ambito) {
        if (nombre == null || ambito == null) return null;

        String ambitoBusqueda = ambito;
        while (ambitoBusqueda != null && !ambitoBusqueda.isEmpty()) {
            RegistroSimboloTp3 encontrado = tabla.get(construirClaveDeclaracion(nombre, ambitoBusqueda));
            if (encontrado != null) return encontrado;

            int ultimoSeparador = ambitoBusqueda.lastIndexOf(':');
            if (ultimoSeparador < 0) break;
            ambitoBusqueda = ambitoBusqueda.substring(0, ultimoSeparador);
        }
        return null;
    }

    public RegistroSimboloTp3 buscar(String nombre) {
        return buscar(nombre, ambitoActual);
    }

    /** Busca exclusivamente en el ámbito indicado (sin recorrer padres). */
    public RegistroSimboloTp3 buscarLocal(String nombre, String ambito) {
        return tabla.get(construirClaveDeclaracion(nombre, ambito));
    }

    public RegistroSimboloTp3 obtener(String nombre) {
        RegistroSimboloTp3 simbolo = buscar(nombre);
        if (simbolo != null) return simbolo;
        return obtenerConstante(nombre);
    }

    public RegistroSimboloTp3 obtenerPorNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null) return null;
        return tabla.get("ID|" + nombreCompleto);
    }

    /** Devuelve todas las entradas: declaraciones y constantes. */
    public Collection<RegistroSimboloTp3> obtenerTodos() {
        return Collections.unmodifiableCollection(tabla.values());
    }

    /** Vista filtrada de las declaraciones (variables, funciones, clases, etc.). */
    public Collection<RegistroSimboloTp3> obtenerSimbolosDeclarados() {
        List<RegistroSimboloTp3> resultado = new ArrayList<>();
        for (RegistroSimboloTp3 registro : tabla.values()) {
            if (!USO_CONSTANTE.equals(registro.getUso())) resultado.add(registro);
        }
        return Collections.unmodifiableList(resultado);
    }

    /** Vista filtrada de las constantes; no es un segundo almacenamiento. */
    public Collection<RegistroSimboloTp3> obtenerConstantes() {
        List<RegistroSimboloTp3> resultado = new ArrayList<>();
        for (RegistroSimboloTp3 registro : tabla.values()) {
            if (USO_CONSTANTE.equals(registro.getUso())) resultado.add(registro);
        }
        return Collections.unmodifiableList(resultado);
    }

    public boolean contieneConstante(String lexema) {
        return obtenerConstante(lexema) != null;
    }

    public RegistroSimboloTp3 obtenerConstante(String lexema) {
        for (RegistroSimboloTp3 registro : tabla.values()) {
            if (USO_CONSTANTE.equals(registro.getUso())
                    && registro.getLexema().equals(lexema)) return registro;
        }
        return null;
    }

    public List<RegistroSimboloTp3> obtenerSimbolosEnAmbito(String ambito) {
        List<RegistroSimboloTp3> resultado = new ArrayList<>();
        for (RegistroSimboloTp3 registro : tabla.values()) {
            if (!USO_CONSTANTE.equals(registro.getUso())
                    && ambito.equals(registro.getAmbito())) resultado.add(registro);
        }
        return resultado;
    }

    // -----------------------------------------------------------------
    // Tema 22: variables comptime
    // -----------------------------------------------------------------

    public boolean actualizarValorComptime(String nombre, String valor, int linea) {
        RegistroSimboloTp3 registro = buscar(nombre);
        if (registro == null || !registro.esComptime()) return false;
        if (valor == null || valor.trim().isEmpty()) return false;
        registro.setValorComptime(valor);
        registro.agregarAtributo("LINEA_ULTIMA_ASIGNACION", linea);
        return true;
    }

    /**
     * Devuelve el literal que debe sustituir a una variable comptime.
     * Devuelve null si no existe, no es comptime o todavía no tiene valor.
     */
    public String obtenerValorComptime(String nombre) {
        RegistroSimboloTp3 registro = buscar(nombre);
        if (registro == null || !registro.esComptime() || !registro.tieneValorComptime()) {
            return null;
        }
        return registro.getValorComptime();
    }

    // -----------------------------------------------------------------
    // Temas 25, 29 y 30: clases, herencia y atributos
    // -----------------------------------------------------------------

    public boolean agregarClaseBase(String clase, String claseBase) {
        RegistroSimboloTp3 registroClase = buscarClaseVisible(clase, ambitoActual);
        RegistroSimboloTp3 registroBase = buscarClaseVisible(claseBase, ambitoActual);
        if (registroClase == null || registroBase == null) return false;

        @SuppressWarnings("unchecked")
        List<String> bases = (List<String>) registroClase.obtenerAtributo(ATR_EXTENDS);
        if (bases == null) {
            bases = new ArrayList<>();
            registroClase.agregarAtributo(ATR_EXTENDS, bases);
        }
        if (!bases.contains(claseBase)) bases.add(claseBase);
        return true;
    }

    public RegistroSimboloTp3 buscarClaseVisible(String nombreClase, String ambito) {
        RegistroSimboloTp3 registro = buscar(nombreClase, ambito);
        return registro != null && USO_CLASE.equals(registro.getUso()) ? registro : null;
    }

    /**
     * Tema 30: busca primero en la clase y luego en sus clases base en el orden
     * declarado en EXTENDS. La búsqueda de cada base es recursiva.
     */
    public RegistroSimboloTp3 resolverMiembroClase(String nombreClase, String miembro) {
        return resolverMiembroClase(nombreClase, miembro, new LinkedHashSet<String>());
    }

    private RegistroSimboloTp3 resolverMiembroClase(
            String nombreClase, String miembro, Set<String> visitadas) {
        if (nombreClase == null || !visitadas.add(nombreClase)) return null;

        RegistroSimboloTp3 clase = buscarClaseVisible(nombreClase, ambitoActual);
        if (clase == null) return null;

        String ambitoClase = clase.getAmbito() + ":" + clase.getLexema();
        RegistroSimboloTp3 propio = buscarLocal(miembro, ambitoClase);
        if (propio != null) return propio;

        Object basesObj = clase.obtenerAtributo(ATR_EXTENDS);
        if (basesObj instanceof List<?>) {
            for (Object base : (List<?>) basesObj) {
                RegistroSimboloTp3 heredado = resolverMiembroClase(String.valueOf(base), miembro, visitadas);
                if (heredado != null) return heredado;
            }
        }
        return null;
    }

    /**
     * Tema 29: obtiene el atributo de una posición (índice desde cero).
     * Se enumeran primero los atributos propios y luego los heredados siguiendo
     * el orden de EXTENDS. Este criterio queda centralizado aquí para cambiarlo
     * fácilmente si la cátedra define otro orden físico.
     */
    public RegistroSimboloTp3 obtenerAtributoPosicional(String nombreClase, int indice) {
        if (indice < 0) return null;
        List<RegistroSimboloTp3> atributosClase = obtenerAtributosEfectivos(nombreClase, new LinkedHashSet<String>());
        return indice < atributosClase.size() ? atributosClase.get(indice) : null;
    }

    private List<RegistroSimboloTp3> obtenerAtributosEfectivos(
            String nombreClase, Set<String> visitadas) {
        List<RegistroSimboloTp3> resultado = new ArrayList<>();
        if (nombreClase == null || !visitadas.add(nombreClase)) return resultado;

        RegistroSimboloTp3 clase = buscarClaseVisible(nombreClase, ambitoActual);
        if (clase == null) return resultado;

        String ambitoClase = clase.getAmbito() + ":" + clase.getLexema();
        for (RegistroSimboloTp3 miembro : obtenerSimbolosEnAmbito(ambitoClase)) {
            if (USO_ATRIBUTO.equals(miembro.getUso())) resultado.add(miembro);
        }

        Object basesObj = clase.obtenerAtributo(ATR_EXTENDS);
        if (basesObj instanceof List<?>) {
            for (Object base : (List<?>) basesObj) {
                resultado.addAll(obtenerAtributosEfectivos(String.valueOf(base), visitadas));
            }
        }
        return resultado;
    }

    /**
     * Tema 25: equivalencia estructural conservadora. Compara cantidad, orden,
     * nombre y tipo de atributos efectivos. Si la consigna de la cátedra define
     * compatibilidad sin exigir iguales nombres, retirar esa comparación.
     */
    public boolean estructurasCompatibles(String claseA, String claseB) {
        List<RegistroSimboloTp3> a = obtenerAtributosEfectivos(claseA, new LinkedHashSet<String>());
        List<RegistroSimboloTp3> b = obtenerAtributosEfectivos(claseB, new LinkedHashSet<String>());
        if (a.size() != b.size()) return false;

        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).getLexema().equals(b.get(i).getLexema())) return false;
            if (!equalsIgnorandoMayusculas(a.get(i).getTipo(), b.get(i).getTipo())) return false;
        }
        return true;
    }

    // -----------------------------------------------------------------
    // Tema 20: orden opcional de evaluación/copia de parámetros
    // -----------------------------------------------------------------

    /** Valida que el orden sea una permutación de 1..cantidadParametros. */
    public boolean validarOrdenParametros(List<Integer> orden, int cantidadParametros) {
        if (orden == null) return true; // Sin lista: orden natural.
        if (orden.size() != cantidadParametros) return false;
        boolean[] vistos = new boolean[cantidadParametros + 1];
        for (Integer posicion : orden) {
            if (posicion == null || posicion < 1 || posicion > cantidadParametros || vistos[posicion]) {
                return false;
            }
            vistos[posicion] = true;
        }
        return true;
    }

    /** Devuelve posiciones base cero en el orden en que deben copiarse. */
    public List<Integer> obtenerOrdenDeCopia(List<Integer> orden, int cantidadParametros) {
        List<Integer> resultado = new ArrayList<>();
        if (orden == null || orden.isEmpty()) {
            for (int i = 0; i < cantidadParametros; i++) resultado.add(i);
        } else {
            for (Integer posicion : orden) resultado.add(posicion - 1);
        }
        return resultado;
    }

    // -----------------------------------------------------------------
    // Tema 33: compatibilidad de tipos del lenguaje actual
    // -----------------------------------------------------------------

    /**
     * En este repositorio los tipos numéricos reconocidos actualmente son
     * USHORTINT y DOUBLEF. El tema 33 exige conversión explícita todf.
     */
    public boolean tiposCompatiblesParaOperacion(
            String tipoIzquierdo, String tipoDerecho, boolean conversionExplicitaTodf) {
        if (equalsIgnorandoMayusculas(tipoIzquierdo, tipoDerecho)) return true;
        return conversionExplicitaTodf
                && ((esEntero(tipoIzquierdo) && esFlotante(tipoDerecho))
                || (esFlotante(tipoIzquierdo) && esEntero(tipoDerecho)));
    }

    public boolean tipoCompatibleAsignacion(
            String tipoDestino, String tipoExpresion, boolean contieneTodf) {
        if (equalsIgnorandoMayusculas(tipoDestino, tipoExpresion)) return true;
        // Tema 33: no se convierte implícitamente; todf produce DOUBLEF.
        return contieneTodf && esFlotante(tipoDestino) && esEntero(tipoExpresion);
    }

    public boolean esEntero(String tipo) {
        return tipo != null && "USHORTINT".equalsIgnoreCase(tipo);
    }

    public boolean esFlotante(String tipo) {
        return tipo != null && "DOUBLEF".equalsIgnoreCase(tipo);
    }

    public void imprimirTabla() {
        System.out.println("\n====================== TABLA DE SÍMBOLOS ======================");
        System.out.printf("%-22s | %-12s | %-18s | %-20s | %-6s%n",
                "NOMBRE COMPLETO", "TIPO", "USO", "ÁMBITO", "LÍNEA");
        System.out.println("-----------------------------------------------------------------------------------------------");
        for (RegistroSimboloTp3 registro : tabla.values()) {
            boolean esConstante = USO_CONSTANTE.equals(registro.getUso());
            String nombreVisible = esConstante
                    ? registro.getLexema()
                    : registro.getNombreCompleto();
            String ambitoVisible = esConstante ? "<constantes>" : texto(registro.getAmbito());
            System.out.printf("%-22s | %-12s | %-18s | %-20s | %-6d%n",
                    nombreVisible,
                    texto(registro.getTipo()),
                    texto(registro.getUso()),
                    ambitoVisible,
                    registro.getLinea());
            if (!registro.getParametros().isEmpty()) {
                System.out.println("  Parámetros: " + registro.getParametros());
            }
            if (registro.esComptime()) {
                System.out.println("  COMPTIME: " + registro.getValorComptime());
            }
            if (registro.tieneAtributo(ATR_EXTENDS)) {
                System.out.println("  EXTENDS: " + registro.obtenerAtributo(ATR_EXTENDS));
            }
            if (registro.getUso() != null && USO_ATRIBUTO.equals(registro.getUso())) {
                System.out.println("  Atributos extra: " + registro.getAtributos());
            }
        }
        System.out.println("===============================================================\n");
    }

    private static String construirNombreCompleto(String nombre, String ambito) {
        return nombre + ":" + ambito;
    }

    /** Clave interna para declaraciones: evita confundirla con una constante. */
    private static String construirClaveDeclaracion(String nombre, String ambito) {
        return "ID|" + construirNombreCompleto(nombre, ambito);
    }

    /** Clave interna para constantes: el tipo forma parte de su identidad. */
    private static String construirClaveConstante(String tipo, String lexema) {
        String tipoNormalizado = tipo == null ? "" : tipo.toUpperCase(Locale.ROOT);
        return "CONST|" + tipoNormalizado + "|" + lexema;
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El " + campo + " no puede ser vacío.");
        }
    }

    private static String texto(String valor) { return valor == null ? "-" : valor; }

    private static boolean equalsIgnorandoMayusculas(String a, String b) {
        return a == null ? b == null : b != null && a.equalsIgnoreCase(b);
    }
}
/* 
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
}*/