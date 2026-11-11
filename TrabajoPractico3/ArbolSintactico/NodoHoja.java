package TrabajoPractico3.ArbolSintactico;

import java.util.Collections;
import java.util.List;
import TrabajoPractico3.RegistroSimboloTp3;

public class NodoHoja extends NodoAST {
    private String tipoNodo;
    private String valor;
    private int linea;

    public NodoHoja(String tipoNodo, String valor, int linea) {
        super(valor);
        this.tipoNodo = tipoNodo;
        this.valor = valor;
        this.linea = linea;
    }
    public String getTipoNodo() {
        return tipoNodo;
    }

    public String getValor() {
        return valor;
    }

    public List<NodoAST> getHijos() {
        return Collections.emptyList();
    }

    @Override
    public String generarCodigo() {
        if (!"ID".equals(tipoNodo)) {
            return valor;
        }

        if (super.getTablaSimbolos() == null) {
            registrarError("Interno", -1, "No se configuró la tabla de símbolos para generar código.");
            return valor;
        }

        RegistroSimboloTp3 reg = super.getTablaSimbolos().resolverVariable(valor);

        if (reg == null) {
            registrarError( "Semántico", linea , "Identificador no declarado o no utilizable como variable: " + valor);
            return valor;
        }
        return reg.getNombreCompleto();
    }
}