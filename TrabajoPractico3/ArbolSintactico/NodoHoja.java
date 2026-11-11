package TrabajoPractico3.ArbolSintactico;

import java.util.Collections;
import java.util.List;
import TrabajoPractico3.RegistroSimboloTp3;

public class NodoHoja extends NodoAST {
    private String tipoNodo;
    private String valor;

    public NodoHoja(String tipoNodo, String valor) {
        super(valor);
        this.tipoNodo = tipoNodo;
        this.valor = valor;
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
        System.err.println(
                "HOLAAAAAAAAAAA"
            );
        if (!"ID".equals(tipoNodo)) {
            return valor;
        }


        if (super.getTablaSimbolos() == null) {
            System.err.println(
                "Error interno: no se configuró la tabla de símbolos para generar código"
            );
            return valor;
        }

        RegistroSimboloTp3 reg = super.getTablaSimbolos().resolverVariable(valor);

        if (reg == null) {
            System.err.println(
                "Error semántico: identificador no declarado o no utilizable como variable: " + valor
            );
            return valor;
        }

        return reg.getNombreCompleto().replace(':', '_');
    }
}