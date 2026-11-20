package TrabajoPractico4.ArbolSintactico;
import TrabajoPractico4.RegistroSimboloTp4;
import TrabajoPractico4.TablaDeSimbolosTp4;
import TrabajoPractico4.GeneracionWebAssembly.*;
import java.util.Collections;
import java.util.List;

public class NodoAccesoArreglo extends NodoAST {
    private final NodoAST arreglo;
    private final NodoAST indice;
    private RegistroSimboloTp4 atributoResuelto;
    private String errorTipo;
    private List<RegistroSimboloTp4> atributosPosibles = Collections.emptyList();
    private String referenciaClase;
    private boolean indiceDinamico;
    
    public NodoAccesoArreglo(NodoAST arreglo, NodoAST indice) {
        super("ACCESO_POSICIONAL");
        this.arreglo = arreglo;
        this.indice = indice;
    }

    public NodoAST getArreglo() { return arreglo;}

    public NodoAST getIndice() { return indice; }

    public String getErrorTipo() { return errorTipo;}

    @Override
    public String obtenerErrorTipo() { return errorTipo; }

    public int getLinea() {
        if (arreglo instanceof NodoHoja) {
            return ((NodoHoja) arreglo).getLinea();
        }
        if (indice instanceof NodoHoja) {
            return ((NodoHoja) indice).getLinea();
        }
        return 0;
    }

    private int obtenerLinea() { return getLinea(); }

    private Integer obtenerIndiceConstante( NodoHoja hoja, TablaDeSimbolosTp4 tabla) {
        String valor;
        if ("CTE".equals(hoja.getTipoNodo())) {
            valor = hoja.getValor();
        } else if ("ID".equals(hoja.getTipoNodo())) {
            RegistroSimboloTp4 registro = hoja.getSimboloVinculado();
            if (registro == null || !registro.esComptime() || !hoja.teniaValorComptimeAlCrear()) {
                return null;
            }
            valor = hoja.getValorComptimeAlCrear();
            if (valor == null) {
                return null;
            }
        } else {
            return null;
        }
        String tipoIndice = hoja.obtenerTipo();
        if (tipoIndice == null || !tabla.esEntero(tipoIndice)) {
            return null;
        }
        int separador = valor.indexOf('$');
        String numero = separador >= 0 ? valor.substring(0, separador) : valor;
        try {
            return Integer.valueOf(numero);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String obtenerTipo() {
        errorTipo = null;
        atributoResuelto = null;
        atributosPosibles = Collections.emptyList();
        referenciaClase = null;
        indiceDinamico = false;
        TablaDeSimbolosTp4 tabla = getTablaSimbolos();
        if (tabla == null) {
            errorTipo = "No se configuró la tabla de símbolos.";
            return null;
        }
        if (!(arreglo instanceof NodoHoja) || !(indice instanceof NodoHoja)) {
            errorTipo = "El acceso posicional requiere un objeto " + "y un índice identificable.";
            return null;
        }
        NodoHoja hojaObjeto = (NodoHoja) arreglo;
        RegistroSimboloTp4 simboloObjeto = hojaObjeto.getSimboloVinculado();
        if (simboloObjeto == null || !TablaDeSimbolosTp4.USO_OBJETO.equals( simboloObjeto.getUso())) {
            errorTipo = "El identificador '" + hojaObjeto.getValor() + "' no es un objeto declarado.";
            return null;
        }
        NodoHoja hojaIndice = (NodoHoja) indice;
        String tipoIndice = hojaIndice.obtenerTipo();
        if (tipoIndice == null || !tabla.esEntero(tipoIndice)) {
            errorTipo = "El índice debe ser de tipo entero.";
            return null;
        }
        Object referenciaClaseObjeto = simboloObjeto.obtenerAtributo("CLASE");
        referenciaClase = referenciaClaseObjeto == null ? simboloObjeto.getTipo() : String.valueOf(referenciaClaseObjeto);
        Integer posicionConstante = obtenerIndiceConstante(hojaIndice, tabla);
        if (posicionConstante != null) {
            if (posicionConstante < 0) {
                errorTipo = "El índice no puede ser negativo.";
                return null;
            }
            atributoResuelto = tabla.obtenerAtributoPosicionalPorReferencia( referenciaClase, posicionConstante);
            if (atributoResuelto == null) {
                errorTipo = "La posición " + posicionConstante + " no existe en la clase '" + simboloObjeto.getTipo() + "'.";
                return null;
            }
            atributosPosibles =  Collections.singletonList(atributoResuelto);
            return atributoResuelto.getTipo();
        }
        if ("CTE".equals(hojaIndice.getTipoNodo())) {
            errorTipo = "No se pudo interpretar el índice posicional.";
            return null;
        }
        RegistroSimboloTp4 registroIndice = hojaIndice.getSimboloVinculado();
        if (registroIndice == null) {
            errorTipo = "La variable utilizada como índice " + "no está declarada o no es visible.";
            return null;
        }
        if (registroIndice.esComptime()) {
            errorTipo = "La variable COMPTIME utilizada como índice "  + "no tiene un valor conocido en este punto.";
            return null;
        }
        atributosPosibles =  tabla.obtenerAtributosPosicionalesPorReferencia( referenciaClase );
        if (atributosPosibles.isEmpty()) {
            errorTipo = "La clase no tiene atributos accesibles " + "por posición.";
            return null;
        }
        String tipoComun = atributosPosibles.get(0).getTipo();
        for (RegistroSimboloTp4 atributo : atributosPosibles) {
            if (atributo.getTipo() == null || !atributo.getTipo().equalsIgnoreCase(tipoComun)) {
                errorTipo = "No se puede determinar un tipo único "  + "para el acceso posicional con índice variable.";
                atributosPosibles = Collections.emptyList();
                return null;
            }
        }
        indiceDinamico = true;
        return tipoComun;
    }

    @Override
    public String generarCodigo() {
        if (obtenerTipo() == null) {
            registrarError("Semántico", obtenerLinea(), errorTipo != null  ? errorTipo : "No se pudo resolver el acceso posicional." );
            return null;
        }
        String objeto = ((NodoHoja) arreglo).generarReferencia();
        if (objeto == null) {
            return null;
        }
        if (!indiceDinamico) {
            if (atributoResuelto == null) {
                return null;
            }
            return objeto + "." + atributoResuelto.getNombreCompleto();
        }
        String codigoIndice = indice.generarCodigo();
        if (codigoIndice == null) {
            return null;
        }
        return objeto + "[" + codigoIndice + "]";
    }

    private String obtenerReferenciaWasm(RegistroSimboloTp4 atributo) {
        String objCompleto =((NodoHoja) arreglo).generarReferencia();
        return GeneradorWasm.sanitizarNombre(objCompleto + "__" + atributo.getNombreCompleto());
    }

    public String generarWasmReferencia() {
            if (obtenerTipo() == null || atributoResuelto == null) return null;
            String objCompleto = ((NodoHoja) arreglo).generarReferencia();
            String refWasm = GeneradorWasm.sanitizarNombre(objCompleto + "__" + atributoResuelto.getNombreCompleto());
            GeneradorWasm.declararGlobalSiNoExiste(refWasm, GeneradorWasm.mapearTipo(atributoResuelto.getTipo()));
            return refWasm;
        }

    @Override
    public String generarWasm() {
        String tipo = obtenerTipo();
        if (tipo == null) {
            return null;
        }
        String auxResultado =  GeneradorWasm.nuevaAuxiliar( GeneradorWasm.mapearTipo(tipo) );
        if (!indiceDinamico) {
            String referencia = generarWasmReferencia();
            if (referencia == null) {
                return null;
            }
            GeneradorWasm.emitirGet(referencia);
            GeneradorWasm.emitirSet(auxResultado);
            return auxResultado;
        }
        String auxIndice = indice.generarWasm();
        if (auxIndice == null) {
            return null;
        }
        String tipoWasm = GeneradorWasm.mapearTipo(tipo);
        for (int i = 0; i < atributosPosibles.size(); i++) {
            GeneradorWasm.emitirGet(auxIndice);
            GeneradorWasm.emitir("i32.const " + i);
            GeneradorWasm.emitir("i32.eq");
            GeneradorWasm.emitir( "if (result " + tipoWasm + ")");
            GeneradorWasm.emitirGet( obtenerReferenciaWasm(atributosPosibles.get(i) ));
            GeneradorWasm.emitir("else");
        }
        GeneradorWasm.emitirErrorRuntime("Error en ejecucion: Indice posicional fuera de rango.");
        for (int i = 0; i < atributosPosibles.size(); i++) {
            GeneradorWasm.emitir("end");
        }
        GeneradorWasm.emitirSet(auxResultado);
        return auxResultado;
    }

    public boolean generarWasmAsignacion(String auxiliarValor) {
        String tipo = obtenerTipo();
        if (tipo == null || auxiliarValor == null) {
            return false;
        }
        if (!indiceDinamico) {
            String referencia = generarWasmReferencia();
            if (referencia == null) {
                return false;
            }
            GeneradorWasm.emitirGet(auxiliarValor);
            GeneradorWasm.emitirSet(referencia);
            return true;
        }
        String auxIndice = indice.generarWasm();
        if (auxIndice == null) {
            return false;
        }
        for (int i = 0; i < atributosPosibles.size(); i++) {
            GeneradorWasm.emitirGet(auxIndice);
            GeneradorWasm.emitir("i32.const " + i);
            GeneradorWasm.emitir("i32.eq");
            GeneradorWasm.emitir("if");
            GeneradorWasm.emitirGet(auxiliarValor);
            GeneradorWasm.emitirSet(obtenerReferenciaWasm( atributosPosibles.get(i) ));
            GeneradorWasm.emitir("else");
        }
        GeneradorWasm.emitirErrorRuntime( "Error en ejecucion: Indice posicional fuera de rango." );
        for (int i = 0; i < atributosPosibles.size(); i++) {
            GeneradorWasm.emitir("end");
        }
        return true;
    }
}
