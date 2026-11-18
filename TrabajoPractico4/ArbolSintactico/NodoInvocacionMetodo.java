package TrabajoPractico4.ArbolSintactico;
import java.util.List;
import TrabajoPractico4.RegistroSimboloTp4;
import TrabajoPractico4.TablaDeSimbolosTp4;

public class NodoInvocacionMetodo extends NodoAST {
    private final NodoHoja objeto;
    private final String metodo;
    private final NodoListaParametros parametros;
    private final List<Integer> ordenParametros;
    private final int linea;
    private String errorTipo;

    public NodoInvocacionMetodo(NodoHoja objeto, String metodo, NodoListaParametros parametros,  List<Integer> ordenParametros, int linea) {
        super("INVOCACION_METODO");
        this.objeto = objeto;
        this.metodo = metodo;
        this.parametros = parametros;
        this.ordenParametros = ordenParametros;
        this.linea = linea;
        this.errorTipo = null;
    }

    public NodoHoja getObjeto() { return objeto;}

    public String getMetodo() { return metodo;}

    public NodoListaParametros getParametros() { return parametros;}

    public List<Integer> getOrdenParametros() { return ordenParametros;}

    public String getErrorTipo() { return errorTipo;}

    @Override
    public String obtenerErrorTipo() {
        return errorTipo;
    }

    @Override
    public String obtenerTipo() {
        errorTipo = null;
        TablaDeSimbolosTp4 tabla = getTablaSimbolos();

        if (tabla == null) {
            errorTipo = "No se pudo acceder a la tabla de símbolos.";
            return null;
        }

        if (objeto == null) {
            errorTipo = "No se especificó el objeto de la invocación.";
            return null;
        }

        RegistroSimboloTp4 registroObjeto = objeto.getSimboloVinculado();

        if (registroObjeto == null || !TablaDeSimbolosTp4.USO_OBJETO.equals( registroObjeto.getUso())) {
            String errorObjeto = objeto.obtenerErrorTipo();
            errorTipo = errorObjeto != null ? errorObjeto : "'" + objeto.getValor() + "' no es un objeto declarado.";
            return null;
        }

        RegistroSimboloTp4 registroMetodo = tabla.resolverMiembroClase( registroObjeto.getTipo(),  metodo);

        if (registroMetodo == null || !TablaDeSimbolosTp4.USO_METODO.equals(registroMetodo.getUso())) {
            errorTipo = "El método '" + metodo + "' no existe en la clase '" + registroObjeto.getTipo() + "'.";
            return null;
        }
        return registroMetodo.getTipo();
    }

    @Override
    public String generarCodigo() {
        TablaDeSimbolosTp4 tabla = getTablaSimbolos();

        if (tabla == null) {
            registrarError( "Interno",    linea,"No se configuró la tabla de símbolos para generar la invocación del método.");
            return "";
        }

        if (objeto == null) {
            registrarError("Semántico",linea, "No se especificó el objeto de la invocación.");
            return "";
        }

        RegistroSimboloTp4 registroObjeto = objeto.getSimboloVinculado();

        if (registroObjeto == null || !TablaDeSimbolosTp4.USO_OBJETO.equals( registroObjeto.getUso())) {
            String error = objeto.obtenerErrorTipo();
            registrarError( "Semántico", linea,error != null ? error: "'" + objeto.getValor()  + "' no es un objeto declarado.");
            return "";
        }

        RegistroSimboloTp4 registroMetodo = tabla.resolverMiembroClase(registroObjeto.getTipo(), metodo);

        if (registroMetodo == null || !TablaDeSimbolosTp4.USO_METODO.equals( registroMetodo.getUso())) {
            registrarError("Semántico", linea, "El método '" + metodo + "' no existe en la clase '"  + registroObjeto.getTipo() + "'." );
            return "";
        }

        if (parametros == null || parametros.getParametros() == null) {// Validar la lista de argumentos.
            registrarError( "Sintáctico", linea,   "La invocación del método '" + metodo + "' tiene una lista de argumentos inválida." );
            return "";
        }

        List<NodoAST> argumentos =  parametros.getParametros();
        List<RegistroSimboloTp4.ParametroTp4> formales = registroMetodo.getParametros();

        if (!tabla.validarCantidadParametros( metodo, registroMetodo.getAmbito(), argumentos.size())) {// Validar cantidad de argumentos.
            registrarError( "Semántico", linea,"Cantidad de argumentos incorrecta al invocar '"  + metodo + "'. Se recibieron "  + argumentos.size() + " y se esperaban " + registroMetodo.getCantidadParametros() + ".");
            return "";
        }

        for (int i = 0; i < argumentos.size(); i++) {
            NodoAST argumento = argumentos.get(i);
            String tipoReal = argumento.obtenerTipo();
            String tipoFormal = formales.get(i).getTipo();
            if (tipoReal == null || tipoFormal == null) {
                String errorArgumento =argumento.obtenerErrorTipo();
                registrarError( "Semántico", linea,  errorArgumento != null  ? errorArgumento : "No se pudo determinar el tipo del argumento "  + (i + 1) + " del método '" + metodo + "'." );
                return "";
            }
            boolean compatible =  tabla.tipoCompatibleAsignacion( tipoFormal,tipoReal, false);
            boolean compatibleEstructural = tabla.tiposObjetoCompatibles( tipoFormal, tipoReal);
            if (!compatible && !compatibleEstructural) {
                registrarError(  "Semántico", linea, "Tipo incompatible para el parámetro '"  + formales.get(i).getNombre()   + "' del método '" + metodo + "': se esperaba " + tipoFormal  + " y se recibió " + tipoReal + ".");
                return "";
            }
        }

        if (!tabla.validarOrdenParametros(ordenParametros, argumentos.size())) {
            registrarError( "Semántico", linea, "El orden de parámetros del método '" + metodo + "' debe ser una permutación de 1 a " + argumentos.size() + ".");
            return "";
        }

        List<Integer> ordenCopia = tabla.obtenerOrdenDeCopia(ordenParametros, argumentos.size());
        
        for (Integer indice : ordenCopia) {
            String valorReal = argumentos.get(indice).generarCodigo();
            if (valorReal == null) { 
                return "";
            }
            RegistroSimboloTp4.ParametroTp4 formal = formales.get(indice);
            String nombreFormal =  formal.getNombre() + ":"  + registroMetodo.getAmbito() + ":" + registroMetodo.getLexema();
            System.out.println(nombreFormal + " := " + valorReal );
        }
        
        String etiqueta = "METHOD_" + registroMetodo.getAmbito().replace(':', '_')  + "_" + registroMetodo.getLexema();
        String nombreObjeto = objeto.generarCodigo();
        if (nombreObjeto == null) {
            return "";
        }
        String resultado = GeneradorTAC.nuevoTemporal();
        System.out.println(resultado + " = CALL_METHOD "  + nombreObjeto + ", " + etiqueta );
        return resultado;
    }
}
