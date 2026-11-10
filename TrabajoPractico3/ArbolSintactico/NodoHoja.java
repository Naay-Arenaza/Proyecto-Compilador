package TrabajoPractico3.ArbolSintactico;

import java.util.Collections;
import java.util.List;

//import TrabajoPractico3.RegistroSimboloTp3;

public class NodoHoja extends NodoAST {
    private String tipoNodo; // "ID", "CTE", etc.
    private String valor;
    //private RegistroSimboloTp3 referencia;

    public NodoHoja(String tipoNodo, String valor/* , RegistroSimboloTp3 referencia*/) {
        super(valor);
        this.tipoNodo = tipoNodo;
        this.valor = valor;
        //this.referencia = referencia;
    }

    public String getTipoNodo() {
    return tipoNodo;
    }

    public String getValor() {
        return valor;
    }

    /*public RegistroSimboloTp3 getReferencia() {
        return referencia;
    }*/

        // Devuelve los hijos del nodo.
    public List<NodoAST> getHijos() {
        return Collections.emptyList();
    }

    @Override
    public String generarCodigo(/*TablaSimbolosScope tabla*/) {
        if (tipoNodo.equals("ID")) {
            // Devuelve el nombre único asignado a este scope (ej: "x_0" o "x_1")
            //return tablaSimbolos.buscarVariable(valor);
        }
        return valor; // Si es una constante CTE, devuelve el valor directo
    }
}