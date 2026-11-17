
package TrabajoPractico3.ArbolSintactico;

import TrabajoPractico3.TablaDeSimbolosTp3;

public class NodoBinario extends NodoAST {

    private NodoAST izq;
    private NodoAST der;
    private String errorTipo;

    public NodoBinario(String operador, NodoAST izq, NodoAST der) {
        super(operador);
        this.izq = izq;
        this.der = der;
        this.errorTipo = null;
    }

    public NodoAST getIzq() {
        return izq;
    }

    public NodoAST getDer() {
        return der;
    }

    public String getErrorTipo() {
        return errorTipo;
    }

    @Override
    public String obtenerTipo() {
        errorTipo = null;

        if (getTablaSimbolos() == null || izq == null || der == null) {
            errorTipo = "No se pudieron determinar los tipos de los operandos.";
            return null;
        }

        String tipoIzq = izq.obtenerTipo();
        String tipoDer = der.obtenerTipo();

        
if (tipoIzq == null || tipoDer == null) {

    String errorIzq = obtenerErrorTipo(izq);

    if (errorIzq != null) {
        errorTipo = errorIzq;
        return null;
    }

    String errorDer = obtenerErrorTipo(der);

    if (errorDer != null) {
        errorTipo = errorDer;
        return null;
    }

    errorTipo = "No se pudo determinar el tipo de uno de los operandos.";
    return null;
}


        TablaDeSimbolosTp3 tabla = getTablaSimbolos();

        boolean numericos =
            (tabla.esEntero(tipoIzq) || tabla.esFlotante(tipoIzq))
            &&
            (tabla.esEntero(tipoDer) || tabla.esFlotante(tipoDer));

        if (!numericos) {
            errorTipo = "La operación " + getEtiqueta()
                + " requiere operandos numéricos, pero recibió "
                + tipoIzq + " y " + tipoDer + ".";
            return null;
        }

        boolean conversionExplicita =
            esConversionTodf(izq) || esConversionTodf(der);

        if (!tabla.tiposCompatiblesParaOperacion(
                tipoIzq, tipoDer, conversionExplicita)) {

            errorTipo = "Tipos incompatibles en la operación "
                + getEtiqueta() + ": " + tipoIzq + " y " + tipoDer
                + ". Se requiere una conversión explícita TODF.";

            return null;
        }

        switch (getEtiqueta()) {
            case "==":
            case "!=":
            case ">":
            case "<":
            case ">=":
            case "<=":
                return "BOOLEAN";
        }

        if (tabla.esFlotante(tipoIzq)) {
            return tipoIzq;
        }

        if (tabla.esFlotante(tipoDer)) {
            return tipoDer;
        }

        return tipoIzq;
    }

    private boolean esConversionTodf(NodoAST nodo) {
        return nodo instanceof NodoUnario
            && "TODF".equals(nodo.getEtiqueta())
            && nodo.obtenerTipo() != null;
    }


private String obtenerErrorTipo(NodoAST nodo) {

    if (nodo == null) {
        return null;
    }

    if (nodo instanceof NodoUnario) {
        NodoUnario unario = (NodoUnario) nodo;

        if (unario.getErrorTipo() != null) {
            return unario.getErrorTipo();
        }

        return obtenerErrorTipo(unario.getOperando());
    }

    if (nodo instanceof NodoBinario) {
        NodoBinario binario = (NodoBinario) nodo;

        if (binario.getErrorTipo() != null) {
            return binario.getErrorTipo();
        }

        String errorIzq = obtenerErrorTipo(binario.getIzq());

        if (errorIzq != null) {
            return errorIzq;
        }

        return obtenerErrorTipo(binario.getDer());
    }

    return null;
}


    @Override
    public String generarCodigo() {
        String tIzq = izq.generarCodigo();
        String tDer = der.generarCodigo();

        if (tIzq == null || tDer == null) {
            return null;
        }

        String temp = GeneradorTAC.nuevoTemporal();

        System.out.println(
            temp + " = " + tIzq + " " + getEtiqueta() + " " + tDer
        );

        return temp;
    }
}
