package TrabajoPractico3;
import java.util.HashMap;
import java.util.Map;

public class RegistroSimboloTp3 {

    private String lexema;
    private String tipoToken;
    private String tipo;
    private String uso;
    private String ambito;
    private String nombreCompleto;
    private int linea;

    private Map<String, Object> atributos;

    // Constructor básico: información disponible desde el léxico.
    public RegistroSimboloTp3(String lexema, String tipoToken, int linea) {

        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.linea = linea;

        // Información que todavía no se conoce.
        this.tipo = null;
        this.uso = null;
        this.ambito = null;
        this.nombreCompleto = null;

        this.atributos = new HashMap<>();
    }

    // -------------------------
    // GETTERS
    // -------------------------

    public String getLexema() {
        return lexema;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUso() {
        return uso;
    }

    public String getAmbito() {
        return ambito;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public int getLinea() {
        return linea;
    }

    public Map<String, Object> getAtributos() {
        return atributos;
    }

    // -------------------------
    // SETTERS
    // -------------------------

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setUso(String uso) {
        this.uso = uso;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setLinea(int linea) {
        this.linea = linea;
    }

    // -------------------------
    // ATRIBUTOS ADICIONALES
    // -------------------------

    public void agregarAtributo(String nombre, Object valor) {
        atributos.put(nombre, valor);
    }

    public Object obtenerAtributo(String nombre) {
        return atributos.get(nombre);
    }

    public boolean tieneAtributo(String nombre) {
        return atributos.containsKey(nombre);
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
                "\n  Linea: " + linea +
                "\n  Atributos: " + atributos +
                "\n}";
    }
}