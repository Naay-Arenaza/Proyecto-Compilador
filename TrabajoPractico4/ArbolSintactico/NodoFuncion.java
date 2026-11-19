package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.GeneracionWebAssembly.*;

public class NodoFuncion extends NodoAST {
    private String nombre;
    private String ambitoCompleto;
    private NodoAST parametros;
    private NodoAST cuerpoDeclarativo;
    private NodoAST cuerpoEjecutable;

    public NodoFuncion(String nombre,String ambitoCompleto,NodoAST parametros,NodoAST declaraciones,NodoAST ejecutables) {
        super("DECLARACION_FUNCION");
        this.nombre = nombre;
        this.ambitoCompleto = ambitoCompleto;
        this.parametros = parametros;
        this.cuerpoDeclarativo = declaraciones;
        this.cuerpoEjecutable = ejecutables;
    }

    public String getNombre() { return nombre;}

    public String getAmbitoCompleto() { return ambitoCompleto;}

    public NodoAST getParametros() {  return parametros; }

    public NodoAST getCuerpoDeclarativo() { return cuerpoDeclarativo;}

    public NodoAST getCuerpoEjecutable() { return cuerpoEjecutable;}

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
        TrabajoPractico4.RegistroSimboloTp4 regFunc = getTablaSimbolos().obtenerPorNombreCompleto(nombre + ":" + ambitoCompleto.substring(0, ambitoCompleto.lastIndexOf(':')));
        String nombreWasmFunc = "$FUNC_" + ambitoCompleto.replace(':', '_');
        String banderaActiva = "$activa_" + ambitoCompleto.replace(':', '_');
        GeneradorWasm.declararGlobalSiNoExiste(banderaActiva, "i32");
        GeneradorWasm.iniciarAmbitoFuncion(banderaActiva);
        StringBuilder firma = new StringBuilder("(func ").append(nombreWasmFunc);
        String tipoRetornoWasm = "i32";
        if (regFunc != null) {
            tipoRetornoWasm = GeneradorWasm.mapearTipo(regFunc.getTipo());
            for (TrabajoPractico4.RegistroSimboloTp4.ParametroTp4 p : regFunc.getParametros()) {
                String nomParam = GeneradorWasm.sanitizarNombre(p.getNombre() + ":" + ambitoCompleto);
                String tipoParam = GeneradorWasm.mapearTipo(p.getTipo());
                GeneradorWasm.declararParametroLocal(nomParam, tipoParam);
                firma.append(" (param ").append(nomParam).append(" ").append(tipoParam).append(")");
            }
        }
        firma.append(" (result ").append(tipoRetornoWasm).append(")");
        for (TrabajoPractico4.RegistroSimboloTp4 sim : getTablaSimbolos().obtenerSimbolosEnAmbito(ambitoCompleto)) {
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
        if (cuerpoDeclarativo != null) cuerpoDeclarativo.generarWasm();
        if (cuerpoEjecutable != null) cuerpoEjecutable.generarWasm();
        GeneradorWasm.emitir("i32.const 0");
        GeneradorWasm.emitir("global.set " + banderaActiva);
        GeneradorWasm.emitir("f64".equals(tipoRetornoWasm) ? "f64.const 0.0" : "i32.const 0");
        GeneradorWasm.emitir("return");
        GeneradorWasm.finalizarAmbitoFuncion(firma.toString());
        return null;
    }
}
