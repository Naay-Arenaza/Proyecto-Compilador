package TrabajoPractico3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegistroSimboloTp3 { // Representa una entrada de la tabla de simbolo
    private final String lexema;
    private final String tipoToken;
    private String tipo;
    private String uso;
    private String ambito;
    private String nombreCompleto;
    private int linea;
    private final long ordenDeclaracion;

    private final Map<String, Object> atributos; // Informacion semantica adicional del simbolo.
    private final List<ParametroTp3> parametros; // Firma formal de una funcion o metodo, en el orden declarado.

    public RegistroSimboloTp3(String lexema, String tipoToken, int linea) {//Constructor de compatibilidad para registros simples
        this(lexema, tipoToken, null, null, null, linea, -1L);
    }

    public RegistroSimboloTp3(String lexema, String tipoToken,String tipo, String uso, String ambito,int linea,long ordenDeclaracion) {//Constructor completo utilizado por la tabla de simbolos.
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

    public String getTipoToken() {return tipoToken; }

    public String getTipo() {return tipo;}

    public String getUso() {return uso;}

    public String getAmbito() {return ambito;}

    public String getNombreCompleto() {return nombreCompleto;}

    public int getLinea() {return linea;}

    public long getOrdenDeclaracion() {return ordenDeclaracion;}

    public Map<String, Object> getAtributos() {return Collections.unmodifiableMap(atributos);}    // Devuelve una vista de solo lectura de los atributos adicionales.
  
    public List<ParametroTp3> getParametros() {return Collections.unmodifiableList(parametros);}  // Devuelve los parametros formales en el orden de declaracion. 

    public void setTipo(String tipo) { this.tipo = tipo;}

    public void setUso(String uso) {this.uso = uso;}

    public void setAmbito(String ambito) {
        this.ambito = ambito;
        this.nombreCompleto = construirNombreCompleto(lexema, ambito);
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre completo no puede ser vacio.");
        }
        this.nombreCompleto = nombreCompleto;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    public void agregarAtributo(String nombre, Object valor) {    // ATRIBUTOS ADICIONALES
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
    // Agrega un parametro si no existe otro parametro formal con el mismo nombre. Retorna false ante un duplicado.
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del parametro no puede ser vacio.");
        }
        if (tieneParametro(nombre)) {
            return false;
        }
        parametros.add(new ParametroTp3(nombre, tipoParametro, lineaParametro));
        return true;
    }

    public boolean agregarParametro(ParametroTp3 parametro) {    // Agrega un parametro ya construido, evitando nombres repetidos.
        if (parametro == null) {
            throw new IllegalArgumentException("El parametro no puede ser null.");
        }
        return agregarParametro(parametro.getNombre(),parametro.getTipo(),parametro.getLinea());
    }

    public boolean tieneParametro(String nombre) {
        if (nombre == null) {
            return false;
        }
        for (ParametroTp3 parametro : parametros) {
            if (nombre.equals(parametro.getNombre())) {
                return true;
            }
        }
        return false;
    }

    public int getCantidadParametros() {
        return parametros.size();
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder();
        texto.append("RegistroSimboloTp3 {");
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

    private static String valorONoDefinido(String valor) {
        return valor == null ? "-" : valor;
    }

    public static final class ParametroTp3 {//Elemento de la firma de una funcion o metodo.
        private final String nombre;
        private final String tipo;
        private final int linea;

        public ParametroTp3(String nombre, String tipo, int linea) {
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del parametro no puede ser vacio.");
            }
            this.nombre = nombre;
            this.tipo = tipo;
            this.linea = linea;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }

        public int getLinea() {
            return linea;
        }

        @Override
        public String toString() {
            return nombre + ":" + tipo;
        }
    }
}
