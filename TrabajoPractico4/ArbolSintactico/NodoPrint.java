package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.GeneracionWebAssembly.*;
public class NodoPrint extends NodoAST {
    private NodoAST contenido;

    public NodoPrint(NodoAST contenido) {
        super("POUT");
        this.contenido = contenido;
    }

    public NodoAST getContenido() { return contenido;}

    @Override
    public String generarCodigo() {
        if (contenido == null) { return null;}
        if (contenido instanceof NodoBinario || contenido instanceof NodoUnario) {// Validar expresiones binarias y conversiones TODF.
            if (contenido.obtenerTipo() == null) {
                String error = contenido.obtenerErrorTipo();
                registrarError( "Semántico",obtenerLinea(contenido),error != null ? error : "No se pudo determinar el tipo de la expresión de POUT.");
                return null;
            }
        }
        String val = contenido.generarCodigo();
        if (val == null) { return null; }
        System.out.println("PRINT " + val);
        return null;
    }

    private int obtenerLinea(NodoAST nodo) {
        if (nodo instanceof NodoHoja) {
            return ((NodoHoja) nodo).getLinea();
        }
        if (nodo instanceof NodoBinario) {
            NodoBinario binario = (NodoBinario) nodo;
            int linea = obtenerLinea(binario.getIzq());
            return linea != 0 ? linea : obtenerLinea(binario.getDer());
        }
        if (nodo instanceof NodoUnario) {
            return obtenerLinea( ((NodoUnario) nodo).getOperando() );
        }
        return 0;
    }

    @Override
    public String generarWasm() {
        if (contenido == null) return null;
        String auxVal = contenido.generarWasm();
        if (auxVal == null) return null;
        String tipo = contenido.obtenerTipo();
        boolean esCadena = "CADENA".equalsIgnoreCase(tipo) || (contenido instanceof NodoHoja && ("CADENA".equalsIgnoreCase(((NodoHoja) contenido).getTipoNodo())  || ((NodoHoja) contenido).getValor().startsWith("{")  || ((NodoHoja) contenido).getValor().startsWith("\"")));
        GeneradorWasm.emitirGet(auxVal);
        if ("DOUBLEF".equalsIgnoreCase(tipo) || "SINGLEF".equalsIgnoreCase(tipo)) {
            GeneradorWasm.emitir("call $print_f64");
        } else if (esCadena) {
            GeneradorWasm.emitir("call $print_str");
        } else {
            GeneradorWasm.emitir("call $print_i32");
        }
        return null;
    }
}
