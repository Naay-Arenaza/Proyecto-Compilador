package TrabajoPractico4.ArbolSintactico;

import TrabajoPractico4.RecolectorErroresTp4;
import TrabajoPractico4.TablaDeSimbolosTp4;

public abstract class NodoAST {
    private String etiqueta;
    private static TablaDeSimbolosTp4 tablaSimbolos;
    private static RecolectorErroresTp4 recolectorErrores;

    public NodoAST(String etiqueta) {this.etiqueta = etiqueta; }
    
    public String getEtiqueta() {return etiqueta; }

    public static void setTablaSimbolos(TablaDeSimbolosTp4 tabla) { NodoAST.tablaSimbolos = tabla; }

    protected static TablaDeSimbolosTp4 getTablaSimbolos() { return tablaSimbolos; }

    public String obtenerTipo() { return null;}

    public String obtenerErrorTipo() { return null;}

    public abstract String generarCodigo();

    public static void setRecolectorErrores(RecolectorErroresTp4 recolector) { NodoAST.recolectorErrores = recolector;}

    protected static void registrarError(String tipo, int linea, String descripcion) {
        if (recolectorErrores != null) {
            recolectorErrores.agregarError(tipo,linea ,descripcion);
        }
    }
}