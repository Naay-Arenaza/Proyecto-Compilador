package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.TablaDeSimbolosTp4;
import TrabajoPractico4.GeneracionWebAssembly.*;

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

    public NodoAST getIzq() { return izq; }

    public NodoAST getDer() { return der; }

    public String getErrorTipo() { return errorTipo; }

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
            String errorIzq = izq.obtenerErrorTipo();
            if (errorIzq != null) {
                errorTipo = errorIzq;
                return null;
            }
            String errorDer = der.obtenerErrorTipo();
            if (errorDer != null) {
                errorTipo = errorDer;
                return null;
            }
            errorTipo = "No se pudo determinar el tipo de uno de los operandos.";
            return null;
        }
        TablaDeSimbolosTp4 tabla = getTablaSimbolos();
        boolean numericos = (tabla.esEntero(tipoIzq) || tabla.esFlotante(tipoIzq))
                && (tabla.esEntero(tipoDer) || tabla.esFlotante(tipoDer));

        if (!numericos) {
            errorTipo = "La operación " + getEtiqueta() + " requiere operandos numéricos, pero recibió " + tipoIzq + " y " + tipoDer + ".";
            return null;
        }
        if (!tabla.tiposCompatiblesParaOperacion(tipoIzq, tipoDer, false)) {
            errorTipo = "Tipos incompatibles en la operación " + getEtiqueta() + ": " + tipoIzq + " y " + tipoDer + ". Se requiere una conversión explícita TODF.";
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

    @Override
    public String obtenerErrorTipo() { return errorTipo; }

    @Override
    public String generarCodigo() {
        String tIzq = izq.generarCodigo();
        String tDer = der.generarCodigo();
        if (tIzq == null || tDer == null) {
            return null;
        }
        String temp = GeneradorTAC.nuevoTemporal();
        System.out.println(temp + " = " + tIzq + " " + getEtiqueta() + " " + tDer);
        return temp;
    }

    @Override
    public String generarWasm() {
        String tipoRes = obtenerTipo();
        if (tipoRes == null) {
            return null;
        }
        String tipoIzq = izq.obtenerTipo();
        String tipoDer = der.obtenerTipo();
        if (tipoIzq == null || tipoDer == null) {
            return null;
        }
        String auxIzq = izq.generarWasm();
        String auxDer = der.generarWasm();
        if (auxIzq == null || auxDer == null) {
            return null;
        }
        String op = getEtiqueta();
        boolean esComparacion = "==".equals(op) || "!=".equals(op) || ">".equals(op)
                || "<".equals(op) || ">=".equals(op) || "<=".equals(op);
        boolean esFlotante = getTablaSimbolos().esFlotante(tipoIzq) || getTablaSimbolos().esFlotante(tipoDer);
        String tipoWasmOp = esFlotante ? "f64" : "i32";
        String tipoResultadoWasm = esComparacion ? "i32" : tipoWasmOp;
        String tipoEnteroResultado = esComparacion
                ? (getTablaSimbolos().esEntero(tipoIzq) ? tipoIzq : tipoDer)
                : tipoRes;
        boolean sinSigno = GeneradorWasm.esTipoSinSigno(tipoEnteroResultado);
        String auxRes = GeneradorWasm.nuevaAuxiliar(tipoResultadoWasm);

        switch (op) {
            case "+":
            case "-":
            case "*":
            case "/":
                if ("/".equals(op)) {
                    GeneradorWasm.emitirGet(auxDer);
                    emitirConversionSiCorresponde(tipoDer, esFlotante);
                    if (esFlotante) {
                        GeneradorWasm.emitir("f64.const 0.0");
                        GeneradorWasm.emitir("f64.eq");
                    } else {
                        GeneradorWasm.emitir("i32.eqz");
                    }
                    GeneradorWasm.emitir("if");
                    GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Division por cero");
                    GeneradorWasm.emitir("end");
                }

                if ("*".equals(op) && !esFlotante) {
                    String aux64 = GeneradorWasm.nuevaAuxiliar("i64");
                    String ext = sinSigno ? "i64.extend_i32_u" : "i64.extend_i32_s";
                    GeneradorWasm.emitirGet(auxIzq);
                    GeneradorWasm.emitir(ext);
                    GeneradorWasm.emitirGet(auxDer);
                    GeneradorWasm.emitir(ext);
                    GeneradorWasm.emitir("i64.mul");
                    GeneradorWasm.emitirSet(aux64);
                    emitirChequeoRangoEntero64(aux64, tipoEnteroResultado);
                    GeneradorWasm.emitirGet(aux64);
                    GeneradorWasm.emitir("i32.wrap_i64");
                    GeneradorWasm.emitirSet(auxRes);
                    break;
                }

                GeneradorWasm.emitirGet(auxIzq);
                emitirConversionSiCorresponde(tipoIzq, esFlotante);
                GeneradorWasm.emitirGet(auxDer);
                emitirConversionSiCorresponde(tipoDer, esFlotante);
                String sufijo = esFlotante ? "f64" : "i32";
                String instruccion = sufijo + ("+".equals(op) ? ".add"
                        : "-".equals(op) ? ".sub"
                        : "*".equals(op) ? ".mul"
                        : (esFlotante ? ".div" : (sinSigno ? ".div_u" : ".div_s")));
                GeneradorWasm.emitir(instruccion);
                GeneradorWasm.emitirSet(auxRes);

                if ("+".equals(op) && "DOUBLEF".equalsIgnoreCase(tipoRes)) {
                    GeneradorWasm.emitirGet(auxRes);
                    GeneradorWasm.emitir("f64.abs");
                    GeneradorWasm.emitir("f64.const 1.7976931348623157e308");
                    GeneradorWasm.emitir("f64.gt");
                    GeneradorWasm.emitir("if");
                    GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Overflow en suma de punto flotante (DOUBLEF)");
                    GeneradorWasm.emitir("end");
                }
                break;

            case "==":
            case "!=":
            case ">":
            case "<":
            case ">=":
            case "<=":
                GeneradorWasm.emitirGet(auxIzq);
                emitirConversionSiCorresponde(tipoIzq, esFlotante);
                GeneradorWasm.emitirGet(auxDer);
                emitirConversionSiCorresponde(tipoDer, esFlotante);
                GeneradorWasm.emitir(mapearComparacionWasm(op, esFlotante, sinSigno));
                GeneradorWasm.emitirSet(auxRes);
                break;

            default:
                registrarError("Interno", 0, "Operador binario no soportado por WebAssembly: " + op);
                return null;
        }
        return auxRes;
    }

    private void emitirConversionSiCorresponde(String tipoOperando, boolean operacionFlotante) {
        if (!operacionFlotante || tipoOperando == null || !getTablaSimbolos().esEntero(tipoOperando)) {
            return;
        }
        GeneradorWasm.emitir(GeneradorWasm.esTipoSinSigno(tipoOperando) ? "f64.convert_i32_u" : "f64.convert_i32_s");
    }

    private void emitirChequeoRangoEntero64(String aux64, String tipo) {
        long min = 0;
        long max = 0;
        boolean unsigned = false;
        switch (tipo.toUpperCase()) {
            case "SHORTINT":  min = -128L; max = 127L; break;
            case "USHORTINT": max = 255L; unsigned = true; break;
            case "INTEGER":   min = -32768L; max = 32767L; break;
            case "UINTEGER":  max = 65535L; unsigned = true; break;
            case "LONGINT":   min = -2147483648L; max = 2147483647L; break;
            case "ULONGINT":  max = 4294967295L; unsigned = true; break;
        }
        if (unsigned) {
            GeneradorWasm.emitirGet(aux64);
            GeneradorWasm.emitir("i64.const " + max);
            GeneradorWasm.emitir("i64.gt_u");
        } else {
            GeneradorWasm.emitirGet(aux64);
            GeneradorWasm.emitir("i64.const " + min);
            GeneradorWasm.emitir("i64.lt_s");
            GeneradorWasm.emitirGet(aux64);
            GeneradorWasm.emitir("i64.const " + max);
            GeneradorWasm.emitir("i64.gt_s");
            GeneradorWasm.emitir("i32.or");
        }
        GeneradorWasm.emitir("if");
        GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Overflow en producto de enteros (" + tipo + ")");
        GeneradorWasm.emitir("end");
    }

    private String mapearComparacionWasm(String op, boolean flotante, boolean sinSigno) {
        String pref = flotante ? "f64." : "i32.";
        String suf = flotante ? "" : (sinSigno ? "_u" : "_s");
        switch (op) {
            case "==": return pref + "eq";
            case "!=": return pref + "ne";
            case ">":  return pref + "gt" + suf;
            case "<":  return pref + "lt" + suf;
            case ">=": return pref + "ge" + suf;
            case "<=": return pref + "le" + suf;
            default:   return "i32.eq";
        }
    }
}