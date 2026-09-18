import java.util.HashMap;
import java.util.Map;

public class RegistroSimbolo {
    private String lexema;                
    private String tipoToken;              
    private Map<String, Object> atributos; 

    public RegistroSimbolo(String lexema, String tipoToken) {
        this.lexema = lexema;
        this.tipoToken = tipoToken;
        this.atributos = new HashMap<>();
    }

    public String getLexema() {
        return lexema;
    }

    public String getTipoToken() {
        return tipoToken;
    }

    public void agregarAtributo(String clave, Object valor) {
        atributos.put(clave, valor);
    }

    public Object obtenerAtributo(String clave) {
        return atributos.get(clave);
    }

    @Override
    public String toString() {
        return "Simbolo { Lexema = '" + lexema + "', Tipo = '" + tipoToken + "', Atributos = " + atributos + " }";
    }
}
