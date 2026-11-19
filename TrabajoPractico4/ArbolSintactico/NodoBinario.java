package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.TablaDeSimbolosTp4;
import TrabajoPractico4.GeneracionWebAssembly.*;;

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

    public NodoAST getIzq() {return izq;}

    public NodoAST getDer() { return der;}

    public String getErrorTipo() {  return errorTipo;}

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
        boolean numericos = (tabla.esEntero(tipoIzq) || tabla.esFlotante(tipoIzq)) && (tabla.esEntero(tipoDer) || tabla.esFlotante(tipoDer));

        if (!numericos) {
            errorTipo = "La operación " + getEtiqueta() + " requiere operandos numéricos, pero recibió "  + tipoIzq + " y " + tipoDer + ".";
            return null;
        }

        boolean conversionExplicita = esConversionTodf(izq) || esConversionTodf(der);

        if (!tabla.tiposCompatiblesParaOperacion(tipoIzq, tipoDer, conversionExplicita)) {
            errorTipo = "Tipos incompatibles en la operación " + getEtiqueta() + ": " + tipoIzq + " y " + tipoDer  + ". Se requiere una conversión explícita TODF.";
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

        if (tabla.esFlotante(tipoIzq)) { return tipoIzq; }

        if (tabla.esFlotante(tipoDer)) { return tipoDer; }
        return tipoIzq;
    }

    private boolean esConversionTodf(NodoAST nodo) {
        return nodo instanceof NodoUnario && "TODF".equals(nodo.getEtiqueta())  && nodo.obtenerTipo() != null;
    }

    @Override
    public String obtenerErrorTipo() { return errorTipo;}

    @Override
    public String generarCodigo() {
        String tIzq = izq.generarCodigo();
        String tDer = der.generarCodigo();
        if (tIzq == null || tDer == null) {
            return null;
        }
        String temp = GeneradorTAC.nuevoTemporal();
        System.out.println( temp + " = " + tIzq + " " + getEtiqueta() + " " + tDer );
        return temp;
    }
    @Override
    public String generarWasm() {
        String tipoRes = obtenerTipo();
        if (tipoRes == null) return null;
        String auxIzq = izq.generarWasm();
        String auxDer = der.generarWasm();
        if (auxIzq == null || auxDer == null) return null;
        String op = getEtiqueta();
        String tipoOperando = izq.obtenerTipo();
        boolean esFlotante = "DOUBLEF".equalsIgnoreCase(tipoOperando) || "SINGLEF".equalsIgnoreCase(tipoOperando);
        boolean sinSigno = GeneradorWasm.esTipoSinSigno(tipoOperando);
        String tipoWasmOp = esFlotante ? "f64" : "i32";
        String auxRes = GeneradorWasm.nuevaAuxiliar("BOOLEAN".equals(tipoRes) ? "i32" : tipoWasmOp);

        switch (op) {
            case "+":
                GeneradorWasm.emitirGet(auxIzq);
                GeneradorWasm.emitirGet(auxDer);
                GeneradorWasm.emitir(tipoWasmOp + ".add");
                GeneradorWasm.emitirSet(auxRes);
                if (esFlotante) {
                    GeneradorWasm.emitirGet(auxRes);
                    GeneradorWasm.emitir("f64.abs");
                    GeneradorWasm.emitir("f64.const 1.7976931348623157e308");
                    GeneradorWasm.emitir("f64.gt");
                    GeneradorWasm.emitir("if");
                    GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Overflow en suma de punto flotante");
                    GeneradorWasm.emitir("end");
                }
                break;

            case "-":
                GeneradorWasm.emitirGet(auxIzq);
                GeneradorWasm.emitirGet(auxDer);
                GeneradorWasm.emitir(tipoWasmOp + ".sub");
                GeneradorWasm.emitirSet(auxRes);
                break;

            case "*":
                if (esFlotante) {
                    GeneradorWasm.emitirGet(auxIzq);
                    GeneradorWasm.emitirGet(auxDer);
                    GeneradorWasm.emitir("f64.mul");
                    GeneradorWasm.emitirSet(auxRes);
                } else {
                    String aux64 = GeneradorWasm.nuevaAuxiliar("i64");
                    String ext = sinSigno ? "i64.extend_i32_u" : "i64.extend_i32_s";
                    GeneradorWasm.emitirGet(auxIzq);
                    GeneradorWasm.emitir(ext);
                    GeneradorWasm.emitirGet(auxDer);
                    GeneradorWasm.emitir(ext);
                    GeneradorWasm.emitir("i64.mul");
                    GeneradorWasm.emitirSet(aux64);
                    emitirChequeoRangoEntero64(aux64, tipoOperando);
                    GeneradorWasm.emitirGet(aux64);
                    GeneradorWasm.emitir("i32.wrap_i64");
                    GeneradorWasm.emitirSet(auxRes);
                }
                break;

            case "/":
                GeneradorWasm.emitirGet(auxDer);
                if (esFlotante) {
                    GeneradorWasm.emitir("f64.const 0.0");
                    GeneradorWasm.emitir("f64.eq");
                } else {
                    GeneradorWasm.emitir("i32.eqz");
                }
                GeneradorWasm.emitir("if");
                GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Division por cero");
                GeneradorWasm.emitir("end");
                GeneradorWasm.emitirGet(auxIzq);
                GeneradorWasm.emitirGet(auxDer);
                GeneradorWasm.emitir(esFlotante ? "f64.div" : (sinSigno ? "i32.div_u" : "i32.div_s"));
                GeneradorWasm.emitirSet(auxRes);
                break;

            case "==": case "!=": case ">": case "<": case ">=": case "<=":
                GeneradorWasm.emitirGet(auxIzq);
                GeneradorWasm.emitirGet(auxDer);
                GeneradorWasm.emitir(mapearComparacionWasm(op, esFlotante, sinSigno));
                GeneradorWasm.emitirSet(auxRes);
                break;
        }
        return auxRes;
    }

    private void emitirChequeoRangoEntero64(String aux64, String tipo) {
        long min = 0, max = 0;
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

