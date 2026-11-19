package TrabajoPractico3.ArbolSintactico;
import TrabajoPractico3.RegistroSimboloTp3;
import TrabajoPractico3.TablaDeSimbolosTp3;

public class NodoAccesoArreglo extends NodoAST {
    private final NodoAST arreglo;
    private final NodoAST indice;
    private RegistroSimboloTp3 atributoResuelto;
    private String errorTipo;

    public NodoAccesoArreglo(NodoAST arreglo, NodoAST indice) {
        super("ACCESO_POSICIONAL");
        this.arreglo = arreglo;
        this.indice = indice;
    }

    public NodoAST getArreglo() { return arreglo; }

    public NodoAST getIndice() {  return indice; }

    public String getErrorTipo() { return errorTipo;}

    @Override
    public String obtenerErrorTipo() { return errorTipo; }

    public RegistroSimboloTp3 getAtributoResuelto() {  return atributoResuelto;  }

    @Override
    public String obtenerTipo() {
        errorTipo = null;
        atributoResuelto = null;
        TablaDeSimbolosTp3 tabla = getTablaSimbolos();
        if (tabla == null) {
            errorTipo =  "No se configuró la tabla de símbolos para resolver el acceso posicional.";
            return null;
        }
        if (!(arreglo instanceof NodoHoja)  || !(indice instanceof NodoHoja)) {
            errorTipo = "El acceso posicional requiere un objeto y un índice identificable.";
            return null;
        }
        NodoHoja hojaObjeto = (NodoHoja) arreglo;
        RegistroSimboloTp3 simboloObjeto = hojaObjeto.getSimboloVinculado();
        if (simboloObjeto == null || !TablaDeSimbolosTp3.USO_OBJETO.equals(simboloObjeto.getUso())) {
            errorTipo =  "El identificador '" + hojaObjeto.getValor() + "' no es un objeto declarado.";
            return null;
        }
        Integer posicion =  obtenerIndiceConstante((NodoHoja) indice, tabla);
        if (posicion == null) {
            errorTipo =  "El índice del acceso posicional debe ser una constante " + "entera o una variable COMPTIME con valor conocido.";
            return null;
        }
        if (posicion < 0) {
            errorTipo =  "El índice del acceso posicional no puede ser negativo.";
            return null;
        }
        Object referenciaClase = simboloObjeto.obtenerAtributo("CLASE");
        String claseCompleta = referenciaClase == null  ? simboloObjeto.getTipo() : String.valueOf(referenciaClase);
        atributoResuelto = tabla.obtenerAtributoPosicionalPorReferencia( claseCompleta,  posicion );
        if (atributoResuelto == null) {
            errorTipo = "La posición " + posicion   + " no existe en la estructura de la clase '"  + simboloObjeto.getTipo() + "'.";
            return null;
        }
        return atributoResuelto.getTipo();
    }

    private Integer obtenerIndiceConstante( NodoHoja hoja,  TablaDeSimbolosTp3 tabla) {
        String valor;
        if ("CTE".equals(hoja.getTipoNodo())) {
            valor = hoja.getValor();
        } else if ("ID".equals(hoja.getTipoNodo())) {
            RegistroSimboloTp3 registro = hoja.getSimboloVinculado();
            if (registro == null || !registro.esComptime()   || !hoja.teniaValorComptimeAlCrear()) {
                return null;
            }
            valor = hoja.getValorComptimeAlCrear();
            if (valor == null) {
                return null;
            }
        } else { return null; }
        String tipoIndice = hoja.obtenerTipo();
        if (tipoIndice == null || !tabla.esEntero(tipoIndice)) { return null;}
        int separador = valor.indexOf('$');
        String numero = separador >= 0   ? valor.substring(0, separador) : valor;
        try {
            return Integer.valueOf(numero);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String generarCodigo() {
        if (obtenerTipo() == null) {
            registrarError("Semántico", obtenerLinea(), errorTipo != null ? errorTipo : "No se pudo resolver el acceso posicional.");
            return null;
        }
        String objeto = ((NodoHoja) arreglo).generarReferencia();
        if (objeto == null || atributoResuelto == null) { return null; }
        return objeto + "." + atributoResuelto.getNombreCompleto();
    }

    private int obtenerLinea() {
        if (arreglo instanceof NodoHoja) {
            return ((NodoHoja) arreglo).getLinea();
        }
        return 0;
    }
}
