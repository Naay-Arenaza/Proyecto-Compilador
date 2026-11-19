package TrabajoPractico3.ArbolSintactico;
import java.util.Collections;
import java.util.List;
import TrabajoPractico3.RegistroSimboloTp3;

public class NodoHoja extends NodoAST {
    private String tipoNodo;
    private String valor;
    private int linea;
    private final RegistroSimboloTp3 registroSimbolos; 
    private String valorComptimeAlCrear;
    private final boolean valorComptimeConocidoAlCrear;

    public NodoHoja(String tipoNodo, String valor, int linea) {
        super(valor);
        this.tipoNodo = tipoNodo;
        this.valor = valor;
        this.linea = linea;
        if ("ID".equals(tipoNodo) && getTablaSimbolos() != null) {
            this.registroSimbolos = getTablaSimbolos().resolverVariable(valor);
        } else {
            this.registroSimbolos = null;
        }
        if ("ID".equals(tipoNodo)&& registroSimbolos != null&& registroSimbolos.esComptime()&& registroSimbolos.tieneValorComptime()) {
            this.valorComptimeAlCrear = registroSimbolos.getValorComptime();
        } else {
            this.valorComptimeAlCrear = null;
        }
        this.valorComptimeConocidoAlCrear = this.valorComptimeAlCrear != null;
    }
    public String getTipoNodo() { return tipoNodo; }

    public String getValor() { return valor;}

    public RegistroSimboloTp3 getSimboloVinculado() { return registroSimbolos;}

    public String getValorComptimeAlCrear() { return valorComptimeAlCrear;}

    public boolean teniaValorComptimeAlCrear() {  return valorComptimeConocidoAlCrear;}
    
    public void invalidarValorComptime() { this.valorComptimeAlCrear = null;}

    public List<NodoAST> getHijos() { return Collections.emptyList(); }

    public int getLinea() { return linea; }

    @Override
    public String obtenerTipo() {
        if (getTablaSimbolos() == null) {
            return null;
        }
        if ("ID".equals(tipoNodo)) {
            if (registroSimbolos == null) {
                return null;
            }
            return registroSimbolos.getTipo();
        }
        if ("CTE".equals(tipoNodo)) {
            RegistroSimboloTp3 constante = getTablaSimbolos().obtenerConstante(valor);
            if (constante != null) {
                return constante.getTipo();
            }
        }
        return null;
    }

    public String generarReferencia() {
        if (!"ID".equals(tipoNodo)) {
            return valor;
        }
        if (super.getTablaSimbolos() == null) {
            registrarError("Interno",linea, "No se configuró la tabla de símbolos para generar código.");
            return valor;
        }
        if (registroSimbolos == null) {
            registrarError("Semántico",linea,"Identificador no declarado o no utilizable como variable: " + valor);
            return valor;
        }
        return registroSimbolos.getNombreCompleto();
    }

    @Override
    public String obtenerErrorTipo() {
        if ("ID".equals(tipoNodo) && registroSimbolos == null) {
            return "Identificador no declarado o no utilizable como variable: " + valor;
        }
        if ("CTE".equals(tipoNodo)) {
            if (getTablaSimbolos() == null || getTablaSimbolos().obtenerConstante(valor) == null) {
                return "No se pudo resolver la constante: " + valor;
            }
        }
        return null;
    }

    @Override
    public String generarCodigo() {
        if (!"ID".equals(tipoNodo)) {
            return valor;
        }
        if (valorComptimeAlCrear != null) { 
            return valorComptimeAlCrear;
        }
        return generarReferencia();
    }
}