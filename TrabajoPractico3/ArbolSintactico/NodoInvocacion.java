package TrabajoPractico3.ArbolSintactico;
import java.util.List;
import TrabajoPractico3.RegistroSimboloTp3;
import TrabajoPractico3.TablaDeSimbolosTp3;

public class NodoInvocacion extends NodoAST {
    private final String nombreFuncion;
    private final NodoListaParametros parametros;
    private final List<Integer> ordenParametros;
    private final int linea;
    private final RegistroSimboloTp3 funcionResuelta;

    public NodoInvocacion(String nombreFuncion, NodoListaParametros parametros, List<Integer> ordenParametros, int linea, RegistroSimboloTp3 funcionResuelta) {
        super("INVOCACION_FUNCION");
        this.nombreFuncion = nombreFuncion;
        this.parametros = parametros;
        this.ordenParametros = ordenParametros;
        this.linea = linea;
        this.funcionResuelta = funcionResuelta;
    }

    public String getNombreFuncion() { return nombreFuncion;}

    public NodoListaParametros getParametros() { return parametros;}

    public List<Integer> getOrdenParametros() {  return ordenParametros;}

    public RegistroSimboloTp3 getFuncionResuelta() {return funcionResuelta;}

    @Override
    public String obtenerErrorTipo() {
        if (funcionResuelta == null || !TablaDeSimbolosTp3.USO_FUNCION.equals( funcionResuelta.getUso())) {
            return "La función '" + nombreFuncion + "' no está declarada o no es visible en este ámbito.";
        }
        return null;
    }

    @Override
    public String obtenerTipo() { return obtenerErrorTipo() == null ? funcionResuelta.getTipo()  : null; }

    @Override
    public String generarCodigo() {
        TablaDeSimbolosTp3 tabla = getTablaSimbolos();
        if (tabla == null) {
            registrarError( "Interno", linea, "No se configuró la tabla de símbolos para generar la invocación." );
            return "";
        }
        RegistroSimboloTp3 funcion = funcionResuelta;
        if (obtenerErrorTipo() != null) {
            registrarError(  "Semántico",   linea,  obtenerErrorTipo());
            return "";
        }
        if (parametros == null) {
            registrarError( "Sintáctico", linea, "La invocación de '" + nombreFuncion + "' tiene una lista de argumentos inválida." );
            return "";
        }
        List<NodoAST> argumentos = parametros.getParametros();
        if (!tabla.validarCantidadParametros(  nombreFuncion,   funcion.getAmbito(), argumentos.size())) {
            registrarError( "Semántico",  linea,"Cantidad de argumentos incorrecta al invocar '" + nombreFuncion + "'. Se recibieron "  + argumentos.size() + " y se esperaban "  + funcion.getCantidadParametros() + "."  );
            return "";
        }
        if (!tabla.validarOrdenParametros( ordenParametros, argumentos.size())) {
            registrarError( "Semántico",  linea,  "El orden de parámetros de '" + nombreFuncion  + "' debe ser una permutación de 1 a "   + argumentos.size() + "." );
            return "";
        }
        List<Integer> ordenCopia = tabla.obtenerOrdenDeCopia(  ordenParametros, argumentos.size());
        List<RegistroSimboloTp3.ParametroTp3> formales = funcion.getParametros();
        for (int i = 0; i < argumentos.size(); i++) {
            String tipoReal = argumentos.get(i).obtenerTipo();
            String tipoFormal = formales.get(i).getTipo();
            if (tipoReal == null || tipoFormal == null || (!tabla.tipoCompatibleAsignacion(tipoFormal,tipoReal,false) && !tabla.tiposObjetoCompatibles(tipoFormal,tipoReal))) {
                registrarError("Semántico",linea,"Tipo incompatible para el parámetro '"+ formales.get(i).getNombre()+ "' de la función '" + nombreFuncion+ "': se esperaba " + tipoFormal+ " y se recibió "+ (tipoReal == null ? "un tipo indeterminado" : tipoReal) + "." );
                return "";
            }
        }
        for (Integer indice : ordenCopia) {
            String valorReal = argumentos.get(indice).generarCodigo();
            if (valorReal == null) { return "";}
            RegistroSimboloTp3.ParametroTp3 formal = formales.get(indice);
            String nombreFormal = formal.getNombre() + ":" + funcion.getAmbito() + ":" + funcion.getLexema();
            System.out.println(  nombreFormal + " := " + valorReal);
        }
        String etiqueta = "FUNC_" + funcion.getAmbito().replace(':', '_')  + "_" + funcion.getLexema();
        String resultado = GeneradorTAC.nuevoTemporal();
        System.out.println( resultado + " = CALL " + etiqueta);
        return resultado;
    }
}
