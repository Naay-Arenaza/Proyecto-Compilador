package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.GeneracionWebAssembly.*;
public class NodoPrograma extends NodoAST {
    private String nombrePrograma;
    private NodoAST declaraciones;
    private NodoAST bloqueEjecutable;

    public NodoPrograma(String nombrePrograma,NodoAST declaraciones,NodoAST bloqueEjecutable) {
        super("PROGRAMA");
        this.nombrePrograma = nombrePrograma;
        this.declaraciones = declaraciones;
        this.bloqueEjecutable = bloqueEjecutable;
    }

    public String getNombrePrograma() { return nombrePrograma; }

    public NodoAST getDeclaraciones() { return declaraciones; }

    public NodoAST getBloqueEjecutable() { return bloqueEjecutable;}

    @Override
    public String generarCodigo() {
        System.out.println("Inicio del Programa " + nombrePrograma);
        if (declaraciones != null) {
            declaraciones.generarCodigo();
        }
        if (bloqueEjecutable != null) {
            bloqueEjecutable.generarCodigo();
        }
        System.out.println("Fin del Programa");
        return null;
    }
   
    @Override 
    public String generarWasm() {
        GeneradorWasm.reiniciar();
        if (getTablaSimbolos() != null) {
            for (TrabajoPractico4.RegistroSimboloTp4 reg : getTablaSimbolos().obtenerSimbolosDeclarados()) {
                String uso = reg.getUso();
                if ("VARIABLE".equals(uso) || "VARIABLE_COMPTIME".equals(uso)) {
                    if ("main".equals(reg.getAmbito())) {
                        GeneradorWasm.registrarVariableSimbolo(reg);
                    }
                } else if ("OBJETO".equals(uso)) {
                    String claseRef = reg.obtenerAtributo("CLASE") != null ? String.valueOf(reg.obtenerAtributo("CLASE")) : reg.getTipo();
                    for (TrabajoPractico4.RegistroSimboloTp4 atr : getTablaSimbolos().obtenerAtributosPosicionalesPorReferencia(claseRef)) {
                        String nombreCampo = GeneradorWasm.sanitizarNombre( reg.getNombreCompleto() + "__" + atr.getNombreCompleto());
                        GeneradorWasm.declararGlobalSiNoExiste( nombreCampo,  GeneradorWasm.mapearTipo(atr.getTipo()) );
                    }
                }
            }
        }
        if (declaraciones != null) {
            declaraciones.generarWasm();
        }
        GeneradorWasm.iniciarAmbitoFuncion(null);
        if (bloqueEjecutable != null) {
            bloqueEjecutable.generarWasm();
        }
        GeneradorWasm.finalizarAmbitoFuncion("(func $main (export \"main\")");
        return GeneradorWasm.obtenerCodigoWatCompleto();
    }
}
