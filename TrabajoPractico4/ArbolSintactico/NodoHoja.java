package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.RegistroSimboloTp4;
import TrabajoPractico4.TablaDeSimbolosTp4;
import TrabajoPractico4.GeneracionWebAssembly.*;

public class NodoHoja extends NodoAST {
    private String tipoNodo;
    private String valor;
    private int linea;
    private final RegistroSimboloTp4 registroSimbolos;
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

    public RegistroSimboloTp4 getSimboloVinculado() { return registroSimbolos;}

    public String getValorComptimeAlCrear() { return valorComptimeAlCrear;}

    public boolean teniaValorComptimeAlCrear() {  return valorComptimeConocidoAlCrear;}
    
    public void invalidarValorComptime() { this.valorComptimeAlCrear = null;}

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
            RegistroSimboloTp4 constante = getTablaSimbolos().obtenerConstante(valor);
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

    @Override
    public String generarWasm() {
        String tipoLeng = obtenerTipo();
        String tipoWasm = GeneradorWasm.mapearTipo(tipoLeng);
        if ("CTE".equals(tipoNodo)) {
            if ("CADENA".equalsIgnoreCase(tipoLeng) || valor.startsWith("\"") || valor.startsWith("'")) {
                String limpio = valor.substring(1, valor.length() - 1);
                int offset = GeneradorWasm.registrarString(limpio);
                String aux = GeneradorWasm.nuevaAuxiliar("i32");
                GeneradorWasm.emitir("i32.const " + offset);
                GeneradorWasm.emitirSet(aux);
                return aux;
            }
            String num = GeneradorWasm.limpiarLiteralNumerico(valor, tipoLeng);
            String aux = GeneradorWasm.nuevaAuxiliar(tipoWasm);
            GeneradorWasm.emitir(tipoWasm + ".const " + num);
            GeneradorWasm.emitirSet(aux);
            return aux;
        }
        if ("ID".equals(tipoNodo)) {
                if (valorComptimeAlCrear != null) {
                    String num = GeneradorWasm.limpiarLiteralNumerico(valorComptimeAlCrear, tipoLeng);
                    String aux = GeneradorWasm.nuevaAuxiliar(tipoWasm);
                    GeneradorWasm.emitir(tipoWasm + ".const " + num);
                    GeneradorWasm.emitirSet(aux);
                    return aux;
                }
                String aux = GeneradorWasm.nuevaAuxiliar(tipoWasm);
                if (registroSimbolos == null){
                    return null;
                }
                    if (GeneradorWasm.esAtributo(registroSimbolos)) {
                        GeneradorWasm.emitirGetAtributo(registroSimbolos);
                    } else {
                        String refWasm = GeneradorWasm.sanitizarNombre(registroSimbolos.getNombreCompleto());
                        if ((TablaDeSimbolosTp4.USO_VARIABLE.equals(registroSimbolos.getUso()) 
                                || TablaDeSimbolosTp4.USO_COMPTIME.equals(registroSimbolos.getUso()) 
                                || TablaDeSimbolosTp4.USO_PARAMETRO.equals(registroSimbolos.getUso()))) {
                            GeneradorWasm.declararGlobalSiNoExiste(refWasm, tipoWasm);
                        }
                        GeneradorWasm.emitirGet(refWasm);
                    }
                GeneradorWasm.emitirSet(aux);
                return aux;
            }
        if ("CADENA".equalsIgnoreCase(tipoNodo) || (valor != null && (valor.startsWith("{") || valor.startsWith("\"")))) {
            String texto = valor != null ? valor : "";
            if ((texto.startsWith("{") && texto.endsWith("}")) || (texto.startsWith("\"") && texto.endsWith("\""))) {
                texto = texto.substring(1, texto.length() - 1);
            }
            int offset = GeneradorWasm.registrarString(texto);
            String aux = GeneradorWasm.nuevaAuxiliar("i32");
            GeneradorWasm.emitir("i32.const " + offset);
            GeneradorWasm.emitirSet(aux);
            return aux;
        }
        return null;
    }
}