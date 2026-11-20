package TrabajoPractico4;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegistroSimboloTp4 {
    private final String lexema;
    private final String tipoToken;
    private final String tipo;
    private final String uso;
    private final String ambito;
    private final String nombreCompleto;
    private final int linea;
    private final long ordenDeclaracion;
    private final Map<String, Object> atributos; 
    private final List<ParametroTp4> parametros; 

    public RegistroSimboloTp4(String lexema, String tipoToken, int linea) {
        this(lexema, tipoToken, null, null, null, linea, -1L);
    }

    public RegistroSimboloTp4(String lexema, String tipoToken,String tipo, String uso, String ambito,int linea,long ordenDeclaracion) {
        if (lexema == null || lexema.trim().isEmpty()) {
            throw new IllegalArgumentException("El lexema no puede ser vacio.");
        }
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.tipo = tipo;
        this.uso = uso;
        this.ambito = ambito;
        this.linea = linea;
        this.ordenDeclaracion = ordenDeclaracion;
        this.nombreCompleto = construirNombreCompleto(lexema, ambito);
        this.atributos = new LinkedHashMap<>();
        this.parametros = new ArrayList<>();
    }

    public String getLexema() {return lexema;}

    public String getTipo() {return tipo;}

    public String getUso() {return uso;}

    public String getAmbito() {return ambito;}

    public String getNombreCompleto() {return nombreCompleto;}

    public int getLinea() {return linea;}

    public List<ParametroTp4> getParametros() {return Collections.unmodifiableList(parametros);} 

    public void agregarAtributo(String nombre, Object valor) { 
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del atributo no puede ser vacio.");
        }
        atributos.put(nombre, valor);
    }

    public Object obtenerAtributo(String nombre) {return atributos.get(nombre);}

    public boolean tieneAtributo(String nombre) {return atributos.containsKey(nombre);}

    public Object quitarAtributo(String nombre) {return atributos.remove(nombre); }

    public boolean esComptime() { return Boolean.TRUE.equals(obtenerAtributo("COMPTIME"));}

    public boolean tieneValorComptime() {return tieneAtributo("VALOR_COMPTIME");}

    public String getValorComptime() {
        Object valor = obtenerAtributo("VALOR_COMPTIME");
        return valor == null ? null : String.valueOf(valor);
    }

    public void setValorComptime(String valor) {agregarAtributo("VALOR_COMPTIME", valor);}

    public boolean agregarParametro(String nombre, String tipoParametro, int lineaParametro) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del parametro no puede ser vacio.");
        }
        if (tieneParametro(nombre)) {
            return false;
        }
        parametros.add(new ParametroTp4(nombre, tipoParametro, lineaParametro));
        return true;
    }

    public boolean tieneParametro(String nombre) {
        if (nombre == null) {
            return false;
        }
        for (ParametroTp4 parametro : parametros) {
            if (nombre.equals(parametro.getNombre())) {
                return true;
            }
        }
        return false;
    }

    public int getCantidadParametros() { return parametros.size(); }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append("RegistroSimboloTp4 {");
        texto.append("\n  Lexema: ").append(lexema);
        texto.append("\n  Tipo de token: ").append(valorONoDefinido(tipoToken));
        texto.append("\n  Tipo: ").append(valorONoDefinido(tipo));
        texto.append("\n  Uso: ").append(valorONoDefinido(uso));
        texto.append("\n  Ambito: ").append(valorONoDefinido(ambito));
        texto.append("\n  Nombre completo: ").append(valorONoDefinido(nombreCompleto));
        texto.append("\n  Linea: ").append(linea);
        texto.append("\n  Orden de declaracion: ").append(ordenDeclaracion);
        texto.append("\n  Parametros: ").append(parametros);
        texto.append("\n  Atributos: ").append(atributos);
        texto.append("\n}");
        return texto.toString();
    }

    private static String construirNombreCompleto(String lexema, String ambito) {
        if (ambito == null || ambito.trim().isEmpty()) {
            return lexema;
        }
        return lexema + ":" + ambito;
    }

    private static String valorONoDefinido(String valor) { return valor == null ? "-" : valor;}

    public static final class ParametroTp4 {
        private final String nombre;
        private final String tipo;
        private final int linea;

        public ParametroTp4(String nombre, String tipo, int linea) {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del parametro no puede ser vacio.");
            }

            if (tipo == null || tipo.trim().isEmpty()) {
                throw new IllegalArgumentException("El tipo del parametro no puede ser vacio.");
            }
            this.nombre = nombre;
            this.tipo = tipo;
            this.linea = linea;
        }

        public String getNombre() { return nombre;}

        public String getTipo() { return tipo; }

        public int getLinea() { return linea; }

        @Override
        public String toString() { return nombre + ":" + tipo; }
    }
}
