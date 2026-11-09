package TrabajoPractico3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Entrada de la tabla de símbolos.
 *
 * Una entrada representa una declaración semántica (variable, parámetro,
 * función, clase, atributo, método, etc.) o una constante reconocida por el
 * analizador léxico. Los identificadores usados pero no declarados NO deben
 * crearse como símbolos: deben resolverse contra la tabla.
 */
public class RegistroSimboloTp3 {

    private final String lexema;
    private final String tipoToken;
    private String tipo;
    private String uso;
    private String ambito;
    private String nombreCompleto;
    private int linea;
    private final long ordenDeclaracion;
    private final Map<String, Object> atributos;
    private final List<ParametroTp3> parametros;

    public RegistroSimboloTp3(String lexema, String tipoToken, int linea) {
        this(lexema, tipoToken, null, null, null, linea, -1L);
    }

    public RegistroSimboloTp3(
            String lexema,
            String tipoToken,
            String tipo,
            String uso,
            String ambito,
            int linea,
            long ordenDeclaracion) {

        if (lexema == null || lexema.trim().isEmpty()) {
            throw new IllegalArgumentException("El lexema del símbolo no puede ser vacío.");
        }

        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.tipo = tipo;
        this.uso = uso;
        this.ambito = ambito;
        this.linea = linea;
        this.ordenDeclaracion = ordenDeclaracion;
        this.nombreCompleto = ambito == null ? lexema : lexema + ":" + ambito;
        this.atributos = new LinkedHashMap<>();
        this.parametros = new ArrayList<>();
    }

    public String getLexema() { return lexema; }
    public String getTipoToken() { return tipoToken; }
    public String getTipo() { return tipo; }
    public String getUso() { return uso; }
    public String getAmbito() { return ambito; }
    public String getNombreCompleto() { return nombreCompleto; }
    public int getLinea() { return linea; }
    public long getOrdenDeclaracion() { return ordenDeclaracion; }

    /** Devuelve los atributos semánticos adicionales de esta entrada. */
    public Map<String, Object> getAtributos() { return atributos; }

    /** Devuelve una vista de solo lectura de los parámetros formales. */
    public List<ParametroTp3> getParametros() {
        return Collections.unmodifiableList(parametros);
    }

    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setUso(String uso) { this.uso = uso; }
    public void setAmbito(String ambito) {
        this.ambito = ambito;
        this.nombreCompleto = ambito == null ? lexema : lexema + ":" + ambito;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public void setLinea(int linea) { this.linea = linea; }

    public void agregarAtributo(String nombre, Object valor) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del atributo no puede ser vacío.");
        }
        atributos.put(nombre, valor);
    }

    public Object obtenerAtributo(String nombre) { return atributos.get(nombre); }
    public boolean tieneAtributo(String nombre) { return atributos.containsKey(nombre); }

    public boolean esComptime() {
        return Boolean.TRUE.equals(obtenerAtributo("COMPTIME"));
    }

    public boolean tieneValorComptime() {
        return tieneAtributo("VALOR_COMPTIME");
    }

    public String getValorComptime() {
        Object valor = obtenerAtributo("VALOR_COMPTIME");
        return valor == null ? null : String.valueOf(valor);
    }

    public void setValorComptime(String valor) {
        agregarAtributo("VALOR_COMPTIME", valor);
    }

    public void agregarParametro(String nombre, String tipoParametro, int lineaParametro) {
        parametros.add(new ParametroTp3(nombre, tipoParametro, lineaParametro));
    }

    public void agregarParametro(ParametroTp3 parametro) {
        if (parametro == null) {
            throw new IllegalArgumentException("El parámetro no puede ser null.");
        }
        parametros.add(parametro);
    }

    @Override
    public String toString() {
        return "RegistroSimboloTp3 {" +
                "\n  Lexema: " + lexema +
                "\n  Tipo de token: " + tipoToken +
                "\n  Tipo: " + tipo +
                "\n  Uso: " + uso +
                "\n  Ambito: " + ambito +
                "\n  Nombre completo: " + nombreCompleto +
                "\n  Línea de declaración: " + linea +
                "\n  Orden de declaración: " + ordenDeclaracion +
                "\n  Parámetros: " + parametros +
                "\n  Atributos: " + atributos +
                "\n}";
    }

    public static final class ParametroTp3 {
        private final String nombre;
        private final String tipo;
        private final int linea;

        public ParametroTp3(String nombre, String tipo, int linea) {
            this.nombre = nombre;
            this.tipo = tipo;
            this.linea = linea;
        }

        public String getNombre() { return nombre; }
        public String getTipo() { return tipo; }
        public int getLinea() { return linea; }

        @Override
        public String toString() {
            return nombre + ":" + tipo;
        }
    }
}