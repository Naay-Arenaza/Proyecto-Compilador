package TrabajoPractico3.ArbolSintactico;

import TrabajoPractico3.TablaDeSimbolosTp3;

public abstract class NodoAST {
    private String etiqueta;
    private static TablaDeSimbolosTp3 tablaSimbolos;

    public NodoAST(String etiqueta) {
        this.etiqueta = etiqueta;
    }
    

    public String getEtiqueta() {
        return etiqueta;
    }

    public static void setTablaSimbolos(TablaDeSimbolosTp3 tabla) {
        NodoAST.tablaSimbolos = tabla;
    }

    protected static TablaDeSimbolosTp3 getTablaSimbolos() {
        return tablaSimbolos;
    }

    public abstract String generarCodigo();
}