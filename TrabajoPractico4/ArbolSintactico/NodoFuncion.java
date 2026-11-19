package TrabajoPractico4.ArbolSintactico;

import TrabajoPractico4.GeneracionWebAssembly.*;
import TrabajoPractico4.RegistroSimboloTp4;

public class NodoFuncion extends NodoAST {

    private String nombre;
    private String ambitoCompleto;
    private NodoAST parametros;
    private NodoAST cuerpoDeclarativo;
    private NodoAST cuerpoEjecutable;

    public NodoFuncion(
            String nombre,
            String ambitoCompleto,
            NodoAST parametros,
            NodoAST declaraciones,
            NodoAST ejecutables) {
        super("DECLARACION_FUNCION");
        this.nombre = nombre;
        this.ambitoCompleto = ambitoCompleto;
        this.parametros = parametros;
        this.cuerpoDeclarativo = declaraciones;
        this.cuerpoEjecutable = ejecutables;
    }

    public String getNombre() { return nombre; }

    public String getAmbitoCompleto() { return ambitoCompleto; }

    public NodoAST getParametros() { return parametros; }

    public NodoAST getCuerpoDeclarativo() { return cuerpoDeclarativo; }

    public NodoAST getCuerpoEjecutable() { return cuerpoEjecutable; }

    @Override
    public String generarCodigo() {
        System.out.println("; --- INICIO FUNCION " + nombre + " ---");
        String etiquetaInicio = "FUNC_" + ambitoCompleto.replace(':', '_') + ":";
        System.out.println(etiquetaInicio);

        if (cuerpoDeclarativo != null) {
            cuerpoDeclarativo.generarCodigo();
        }
        if (cuerpoEjecutable != null) {
            cuerpoEjecutable.generarCodigo();
        }
        System.out.println("RET");
        return null;
    }

    @Override
    public String generarWasm() {
        int separador = ambitoCompleto.lastIndexOf(':');
        if (separador < 0) {
            throw new IllegalStateException("No se pudo determinar el ámbito de la función '" + nombre + "'.");
        }
        String ambitoPadre = ambitoCompleto.substring(0, separador);
        RegistroSimboloTp4 regFunc = getTablaSimbolos().obtenerPorNombreCompleto(nombre + ":" + ambitoPadre);
        if (regFunc == null) {
            throw new IllegalStateException("No se encontró la función '" + nombre + "' en el ámbito '" + ambitoPadre + "'.");
        }
        String identificadorFuncion = regFunc.getAmbito().replace(':', '_') + "_" + regFunc.getLexema();
        String nombreWasmFunc = "$FUNC_" + identificadorFuncion;
        String banderaActiva = "$activa_FUNC_" + identificadorFuncion;
        GeneradorWasm.declararGlobalSiNoExiste(banderaActiva, "i32");
        GeneradorWasm.iniciarAmbitoFuncion(banderaActiva);
        StringBuilder firma = new StringBuilder("(func ").append(nombreWasmFunc);
        String tipoRetornoWasm = GeneradorWasm.mapearTipo(regFunc.getTipo());
        for (RegistroSimboloTp4.ParametroTp4 p : regFunc.getParametros()) {
            String nomParamWasm = "$param_" + p.getNombre() + "_" + ambitoCompleto.replace(':', '_');
            String tipoParam = GeneradorWasm.mapearTipo(p.getTipo());
            GeneradorWasm.declararParametroLocal(nomParamWasm, tipoParam);
            firma.append(" (param ").append(nomParamWasm).append(" ").append(tipoParam).append(")");
        }
        firma.append(" (result ").append(tipoRetornoWasm).append(")");
        for (RegistroSimboloTp4 sim : getTablaSimbolos().obtenerSimbolosEnAmbito(ambitoCompleto)) {
            if ("VARIABLE".equals(sim.getUso()) || "VARIABLE_COMPTIME".equals(sim.getUso())) {
                GeneradorWasm.registrarVariableSimbolo(sim);
            }
        }
        GeneradorWasm.emitir("global.get " + banderaActiva);
        GeneradorWasm.emitir("if");
        GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Recursion no permitida en funcion " + nombre);
        GeneradorWasm.emitir("end");
        GeneradorWasm.emitir("i32.const 1");
        GeneradorWasm.emitir("global.set " + banderaActiva);
        for (RegistroSimboloTp4.ParametroTp4 p : regFunc.getParametros()) {
            String nomParamWasm = "$param_" + p.getNombre() + "_" + ambitoCompleto.replace(':', '_');
            String nomGlobalParam = GeneradorWasm.sanitizarNombre(p.getNombre() + ":" + ambitoCompleto);
            String tipoParam = GeneradorWasm.mapearTipo(p.getTipo());
            GeneradorWasm.registrarParametroEnGlobal(nomParamWasm, nomGlobalParam, tipoParam);
        }
        if (cuerpoDeclarativo != null) {
            cuerpoDeclarativo.generarWasm();
        }
        if (cuerpoEjecutable != null) {
            cuerpoEjecutable.generarWasm();
        }
        GeneradorWasm.emitir("i32.const 0");
        GeneradorWasm.emitir("global.set " + banderaActiva);
        if ("f64".equals(tipoRetornoWasm)) {
            GeneradorWasm.emitir("f64.const 0.0");
        } else if ("f32".equals(tipoRetornoWasm)) {
            GeneradorWasm.emitir("f32.const 0.0");
        } else {
            GeneradorWasm.emitir("i32.const 0");
        }
        GeneradorWasm.emitir("return");
        GeneradorWasm.finalizarAmbitoFuncion(firma.toString());
        return null;
    }
}