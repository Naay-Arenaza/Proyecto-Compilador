package TrabajoPractico4.ArbolSintactico;

import TrabajoPractico4.TablaDeSimbolosTp4;

public class NodoReturn extends NodoAST {
    private NodoAST expresionRetorno;
    private String tipoRetornoEsperado;
    private int linea;

    public NodoReturn(NodoAST expresionRetorno,String tipoRetornoEsperado,int linea) {
        super("RET");
        this.expresionRetorno = expresionRetorno;
        this.tipoRetornoEsperado = tipoRetornoEsperado;
        this.linea = linea;
    }

    public NodoAST getExpresionRetorno() {
        return expresionRetorno;
    }

    @Override
    public String generarCodigo() {
        if (expresionRetorno == null) {
            registrarError("Semántico",linea,"La sentencia RET debe contener una expresión.");
            return null;
        }
        if (tipoRetornoEsperado == null) {
            registrarError("Semántico", linea,"No se pudo determinar el tipo de retorno de la función.");
            return null;
        }
        String tipoExpresion = expresionRetorno.obtenerTipo();
        if (tipoExpresion == null) {
            String errorExpresion = expresionRetorno.obtenerErrorTipo();
            if (errorExpresion != null) {
                registrarError("Semántico", linea, errorExpresion);
            } else {
                registrarError( "Semántico",linea,    "No se pudo determinar el tipo de la expresión de retorno.");
            }
            return null;
        }
        TablaDeSimbolosTp4 tabla = getTablaSimbolos();
        if (!tabla.tipoCompatibleAsignacion(tipoRetornoEsperado,tipoExpresion,false)) {
            registrarError( "Semántico",  linea,"Tipo de retorno incompatible: la función espera "    + tipoRetornoEsperado + ", pero la expresión devuelve "  + tipoExpresion + "." );
            return null;
        }
        String retVal = expresionRetorno.generarCodigo();
        if (retVal == null) {
            return null;
        }
        System.out.println("RETURN " + retVal);
        return retVal;
    }
}
