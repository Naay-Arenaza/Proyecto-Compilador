
package TrabajoPractico4.ArbolSintactico;

import TrabajoPractico4.GeneracionWebAssembly.*;
import TrabajoPractico4.TablaDeSimbolosTp4;

public class NodoReturn extends NodoAST {

    private NodoAST expresionRetorno;
    private String tipoRetornoEsperado;
    private int linea;

    public NodoReturn(
            NodoAST expresionRetorno,
            String tipoRetornoEsperado,
            int linea) {

        super("RET");

        this.expresionRetorno = expresionRetorno;
        this.tipoRetornoEsperado = tipoRetornoEsperado;
        this.linea = linea;
    }

    public NodoAST getExpresionRetorno() {
        return expresionRetorno;
    }

    @Override
    public String obtenerTipo() {
        return tipoRetornoEsperado;
    }

    @Override
    public String obtenerErrorTipo() {

        if (expresionRetorno == null) {
            return "La sentencia RET debe contener una expresión.";
        }

        if (tipoRetornoEsperado == null) {
            return "La sentencia RET debe estar dentro de una función o método.";
        }

        String tipoExpresion = expresionRetorno.obtenerTipo();

        if (tipoExpresion == null) {
            String error = expresionRetorno.obtenerErrorTipo();

            return error != null
                    ? error
                    : "No se pudo determinar el tipo de la expresión de retorno.";
        }

        TablaDeSimbolosTp4 tabla = getTablaSimbolos();

        if (tabla == null) {
            return "No se pudo acceder a la tabla de símbolos.";
        }

        if (!tabla.tipoCompatibleAsignacion(
                tipoRetornoEsperado,
                tipoExpresion,
                false)) {

            return "Tipo de retorno incompatible: la función espera "
                    + tipoRetornoEsperado
                    + ", pero la expresión devuelve "
                    + tipoExpresion + ".";
        }

        return null;
    }

    @Override
    public String generarCodigo() {

        String error = obtenerErrorTipo();

        if (error != null) {
            registrarError("Semántico", linea, error);
            return null;
        }

        String retVal = expresionRetorno.generarCodigo();

        if (retVal == null) {
            return null;
        }

        System.out.println("RETURN " + retVal);

        return retVal;
    }

    @Override
    public String generarWasm() {

        if (expresionRetorno == null) {
            registrarError(
                    "Semántico",
                    linea,
                    "La sentencia RET debe contener una expresión."
            );
            return null;
        }

        /*
         * Primero se genera la expresión.
         * Esto es importante porque si la expresión contiene una
         * invocación recursiva, todavía debe detectarse.
         */
        String auxRet = expresionRetorno.generarWasm();

        if (auxRet == null) {
            return null;
        }

        /*
         * Desactivar la bandera de la función o método actual.
         * Así evitamos que un RETURN anticipado deje la función
         * marcada como activa.
         */
        String bandera =
                GeneradorWasm.getNombreBanderaFuncionActual();

        if (bandera != null) {
            GeneradorWasm.emitir("i32.const 0");
            GeneradorWasm.emitir("global.set " + bandera);
        }

        // Recuperar el valor que debe devolver la función.
        GeneradorWasm.emitirGet(auxRet);

        // Finalizar la ejecución de la función actual.
        GeneradorWasm.emitir("return");

        return auxRet;
    }
}
