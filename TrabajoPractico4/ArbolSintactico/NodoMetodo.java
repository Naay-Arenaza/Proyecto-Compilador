package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.GeneracionWebAssembly.*;

public class NodoMetodo extends NodoAST {
    private String nombre;
    private String ambitoCompleto;
    private NodoAST parametros;
    private NodoAST cuerpoEjecutable;

    public NodoMetodo(String nombre, String ambitoCompleto, NodoAST parametros, NodoAST ejecutables) {
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
        String nombreWasmMetodo = "$METHOD_" + ambitoCompleto.replace(':', '_');
        String banderaActiva = "$activa_" + ambitoCompleto.replace(':', '_');
        GeneradorWasm.declararGlobalSiNoExiste(banderaActiva, "i32");
        GeneradorWasm.iniciarAmbitoFuncion(banderaActiva);
        int sep = ambitoCompleto.lastIndexOf(':');
        String ambitoClase = ambitoCompleto.substring(0, sep);
        TrabajoPractico4.RegistroSimboloTp4 regMetodo = getTablaSimbolos().buscarLocal(nombre, ambitoClase);
        GeneradorWasm.declararParametroLocal("$this", "i32");
        StringBuilder firma = new StringBuilder("(func ").append(nombreWasmMetodo).append(" (param $this i32)");

        String tipoRetornoWasm = "i32";
        if (regMetodo != null) {
            tipoRetornoWasm = GeneradorWasm.mapearTipo(regMetodo.getTipo());
            for (TrabajoPractico4.RegistroSimboloTp4.ParametroTp4 p : regMetodo.getParametros()) {
                String nomParam = GeneradorWasm.sanitizarNombre(p.getNombre() + ":" + ambitoCompleto);
                String tipoParam = GeneradorWasm.mapearTipo(p.getTipo());
                GeneradorWasm.declararParametroLocal(nomParam, tipoParam);
                firma.append(" (param ").append(nomParam).append(" ").append(tipoParam).append(")");
            }
        }
        firma.append(" (result ").append(tipoRetornoWasm).append(")");
        GeneradorWasm.emitir("global.get " + banderaActiva);
        GeneradorWasm.emitir("if");
        GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Recursion no permitida en metodo " + nombre);
        GeneradorWasm.emitir("end");
        GeneradorWasm.emitir("i32.const 1");
        GeneradorWasm.emitir("global.set " + banderaActiva);

        if (cuerpoEjecutable != null) {
            cuerpoEjecutable.generarWasm();
        }

        GeneradorWasm.emitir("i32.const 0");
        GeneradorWasm.emitir("global.set " + banderaActiva);
        GeneradorWasm.emitir("f64".equals(tipoRetornoWasm) ? "f64.const 0.0" : "i32.const 0");
        GeneradorWasm.emitir("return");
        GeneradorWasm.finalizarAmbitoFuncion(firma.toString());
        return null;
    }
}