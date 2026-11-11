package TrabajoPractico3.ArbolSintactico;

import TrabajoPractico3.RecolectorErroresTp3;
import TrabajoPractico3.TablaDeSimbolosTp3;

public abstract class NodoAST {
    private String etiqueta;
    private static TablaDeSimbolosTp3 tablaSimbolos;
    private static RecolectorErroresTp3 recolectorErrores;

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

    public static void setRecolectorErrores(RecolectorErroresTp3 recolector) {
        NodoAST.recolectorErrores = recolector;
    }

    protected static void registrarError(String tipo, int linea, String descripcion) {
        if (recolectorErrores != null) {
            recolectorErrores.agregarError(tipo,linea ,descripcion);
        }
    }
}