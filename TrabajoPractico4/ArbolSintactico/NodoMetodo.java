package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.RegistroSimboloTp4;
import TrabajoPractico4.GeneracionWebAssembly.*;

public class NodoMetodo extends NodoAST {
    private String nombre;
    private String ambitoCompleto;
    private NodoAST parametros;
    private NodoAST cuerpoEjecutable;

    public NodoMetodo( String nombre, String ambitoCompleto, NodoAST parametros,  NodoAST ejecutables) {
        super("DECLARACION_METODO");
        this.nombre = nombre;
        this.ambitoCompleto = ambitoCompleto;
        this.parametros = parametros;
        this.cuerpoEjecutable = ejecutables;
    }

    public String getNombre() { return nombre; }

    public String getAmbitoCompleto() { return ambitoCompleto; }

    public NodoAST getParametros() { return parametros; }

    public NodoAST getCuerpoEjecutable() { return cuerpoEjecutable; }

    @Override
    public String generarCodigo() {
        System.out.println("; --- INICIO METODO " + nombre + " ---");
        String etiquetaInicio = "METHOD_" + ambitoCompleto.replace(':', '_') + ":";
        System.out.println(etiquetaInicio);
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
            throw new IllegalStateException("No se pudo determinar la clase del método '" + nombre + "'.");
        }
        String ambitoClase = ambitoCompleto.substring(0, separador);
        RegistroSimboloTp4 regMetodo = getTablaSimbolos().buscarLocal(nombre, ambitoClase);
        if (regMetodo == null) {
            throw new IllegalStateException("No se encontró el método '" + nombre + "' en la clase '" + ambitoClase + "'.");
        }
        String identificadorMetodo = regMetodo.getAmbito().replace(':', '_') + "_" + regMetodo.getLexema();
        String nombreWasmMetodo = "$METHOD_" + identificadorMetodo;
        String banderaActiva = "$activa_METHOD_" + identificadorMetodo;
        GeneradorWasm.declararGlobalSiNoExiste(banderaActiva, "i32");
        GeneradorWasm.iniciarAmbitoFuncion(banderaActiva);
        String tipoRetornoWasm = GeneradorWasm.mapearTipo(regMetodo.getTipo());
        GeneradorWasm.declararParametroLocal("$this", "i32");
        StringBuilder firma = new StringBuilder("(func ").append(nombreWasmMetodo).append(" (param $this i32)");

        for (RegistroSimboloTp4.ParametroTp4 p : regMetodo.getParametros()) {
            String nomParamWasm = "$param_" + p.getNombre() + "_" + ambitoCompleto.replace(':', '_');
            String tipoParam = GeneradorWasm.mapearTipo(p.getTipo());
            GeneradorWasm.declararParametroLocal(nomParamWasm, tipoParam);
            firma.append(" (param ").append(nomParamWasm).append(" ").append(tipoParam).append(")");
        }
        firma.append(" (result ").append(tipoRetornoWasm).append(")");
        GeneradorWasm.emitir("global.get " + banderaActiva);
        GeneradorWasm.emitir("if");
        GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Recursion no permitida en metodo " + nombre);
        GeneradorWasm.emitir("end");
        GeneradorWasm.emitir("i32.const 1");
        GeneradorWasm.emitir("global.set " + banderaActiva);
        for (RegistroSimboloTp4.ParametroTp4 p : regMetodo.getParametros()) {
            String nomParamWasm = "$param_" + p.getNombre() + "_" + ambitoCompleto.replace(':', '_');
            String nomGlobalParam = GeneradorWasm.sanitizarNombre(p.getNombre() + ":" + ambitoCompleto);
            String tipoParam = GeneradorWasm.mapearTipo(p.getTipo());
            GeneradorWasm.registrarParametroEnGlobal(nomParamWasm, nomGlobalParam, tipoParam);
        }
        for (RegistroSimboloTp4 sim : getTablaSimbolos().obtenerSimbolosEnAmbito(ambitoCompleto)) {
            if ("VARIABLE".equals(sim.getUso()) || "VARIABLE_COMPTIME".equals(sim.getUso())) {
                GeneradorWasm.registrarVariableSimbolo(sim);
            }
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