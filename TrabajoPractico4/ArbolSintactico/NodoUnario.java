package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.GeneracionWebAssembly.*;
public class NodoUnario extends NodoAST {

    private NodoAST operando;
    private String errorTipo;

    public NodoUnario(String operador, NodoAST operando) {
        super(operador);
        this.operando = operando;
        this.errorTipo = null;
    }

    public NodoAST getOperando() { return operando; }

    public String getErrorTipo() { return errorTipo;}

    @Override
    public String obtenerErrorTipo() { return errorTipo;}

    @Override
    public String obtenerTipo() {
        errorTipo = null;
        if (getTablaSimbolos() == null || operando == null) {
            errorTipo = "No se pudo determinar el tipo del operando.";
            return null;
        }
        String tipoOperando = operando.obtenerTipo();
        if (tipoOperando == null) {
            String errorOperando = operando.obtenerErrorTipo();
            if (errorOperando != null) {
                errorTipo = errorOperando;
            } else {
                errorTipo = "No se pudo determinar el tipo del operando de TODF.";
            }
            return null;
        }
        if ("TODF".equals(getEtiqueta())) {
            if (!getTablaSimbolos().esEntero(tipoOperando)) {
                errorTipo = "TODF requiere un operando entero, pero recibió "
                    + tipoOperando + ".";
                return null;
            }
            return getTablaSimbolos().tipoResultadoTodf(tipoOperando);
        }
        errorTipo = "Operador unario no reconocido: " + getEtiqueta();
        return null;
    }

    @Override
    public String generarCodigo() {
        if (obtenerTipo() == null) {
            return null;
        }
        String res = operando.generarCodigo();
        if (res == null) {
            return null;
        }
        String temp = GeneradorTAC.nuevoTemporal();
        System.out.println( temp + " = " + getEtiqueta() + "(" + res + ")");
        return temp;
    }
    @Override
    public String generarWasm() {
        if (obtenerTipo() == null) return null;
        String auxOp = operando.generarWasm();
        if (auxOp == null) return null;
        String auxRes = GeneradorWasm.nuevaAuxiliar("f64");
        String tipoOriginal = operando.obtenerTipo();
        String instr = GeneradorWasm.esTipoSinSigno(tipoOriginal) ? "f64.convert_i32_u" : "f64.convert_i32_s";
        GeneradorWasm.emitirGet(auxOp);
        GeneradorWasm.emitir(instr);
        GeneradorWasm.emitirSet(auxRes);
        return auxRes;
    }
}


