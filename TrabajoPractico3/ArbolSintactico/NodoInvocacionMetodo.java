
package TrabajoPractico3.ArbolSintactico;

import java.util.List;

import TrabajoPractico3.RegistroSimboloTp3;
import TrabajoPractico3.TablaDeSimbolosTp3;

public class NodoInvocacionMetodo extends NodoAST {

    private final NodoHoja objeto;
    private final String metodo;
    private final NodoListaParametros parametros;
    private final List<Integer> ordenParametros;
    private final int linea;

    public NodoInvocacionMetodo(
            NodoHoja objeto,
            String metodo,
            NodoListaParametros parametros,
            List<Integer> ordenParametros,
            int linea) {

        super("INVOCACION_METODO");

        this.objeto = objeto;
        this.metodo = metodo;
        this.parametros = parametros;
        this.ordenParametros = ordenParametros;
        this.linea = linea;
    }

    public NodoHoja getObjeto() {
        return objeto;
    }

    public String getMetodo() {
        return metodo;
    }

    public NodoListaParametros getParametros() {
        return parametros;
    }

    public List<Integer> getOrdenParametros() {
        return ordenParametros;
    }

    @Override
public String obtenerTipo() {

    TablaDeSimbolosTp3 tabla = getTablaSimbolos();

    if (tabla == null || objeto == null) {
        return null;
    }

    // Obtener el símbolo del objeto.
    RegistroSimboloTp3 registroObjeto =
        objeto.getSimboloVinculado();

    if (registroObjeto == null ||
        !TablaDeSimbolosTp3.USO_OBJETO.equals(
            registroObjeto.getUso())) {
        return null;
    }

    // Resolver el método en la clase del objeto.
    RegistroSimboloTp3 registroMetodo =
        tabla.resolverMiembroClase(
            registroObjeto.getTipo(),
            metodo
        );

    if (registroMetodo == null ||
        !TablaDeSimbolosTp3.USO_METODO.equals(
            registroMetodo.getUso())) {
        return null;
    }

    // El tipo del símbolo del método es su tipo de retorno.
    return registroMetodo.getTipo();
}

    @Override
    public String generarCodigo() {

        TablaDeSimbolosTp3 tabla = getTablaSimbolos();

        if (tabla == null) {
            registrarError(
                "Interno",
                linea,
                "No se configuró la tabla de símbolos para generar la invocación del método."
            );
            return "";
        }

        // Obtener el símbolo concreto del objeto.
        RegistroSimboloTp3 registroObjeto =
            objeto.getSimboloVinculado();

        if (registroObjeto == null ||
            !TablaDeSimbolosTp3.USO_OBJETO.equals(
                registroObjeto.getUso())) {

            registrarError(
                "Semántico",
                linea,
                "'" + objeto.getValor() +
                "' no es un objeto declarado."
            );
            return "";
        }

        // Resolver el método en la clase del objeto.
        RegistroSimboloTp3 registroMetodo =
            tabla.resolverMiembroClase(
                registroObjeto.getTipo(),
                metodo
            );

        if (registroMetodo == null ||
            !TablaDeSimbolosTp3.USO_METODO.equals(
                registroMetodo.getUso())) {

            registrarError(
                "Semántico",
                linea,
                "El método '" + metodo +
                "' no existe en la clase '" +
                registroObjeto.getTipo() + "'."
            );
            return "";
        }

        List<NodoAST> argumentos =
            parametros.getParametros();

        // Verificar cantidad de argumentos.
        if (!tabla.validarCantidadParametros(
                metodo,
                registroMetodo.getAmbito(),
                argumentos.size())) {

            registrarError(
                "Semántico",
                linea,
                "Cantidad de argumentos incorrecta al invocar '" +
                metodo + "'. Se recibieron " +
                argumentos.size() + " y se esperaban " +
                registroMetodo.getCantidadParametros() + "."
            );
            return "";
        }

        // Validar orden de copia.
        if (!tabla.validarOrdenParametros(
                ordenParametros,
                argumentos.size())) {

            registrarError(
                "Semántico",
                linea,
                "El orden de parámetros del método '" +
                metodo + "' debe ser una permutación de 1 a " +
                argumentos.size() + "."
            );
            return "";
        }

        List<Integer> ordenCopia =
            tabla.obtenerOrdenDeCopia(
                ordenParametros,
                argumentos.size()
            );

        List<RegistroSimboloTp3.ParametroTp3> formales =
            registroMetodo.getParametros();

        for (Integer indice : ordenCopia) {

            String valorReal =
                argumentos.get(indice).generarCodigo();

            RegistroSimboloTp3.ParametroTp3 formal =
                formales.get(indice);

            String nombreFormal =
                formal.getNombre() + ":" +
                registroMetodo.getAmbito() + ":" +
                registroMetodo.getLexema();

            System.out.println(
                nombreFormal + " := " + valorReal
            );
        }

        String etiqueta =
            "METHOD_" +
            registroMetodo.getAmbito().replace(':', '_') +
            "_" + registroMetodo.getLexema();

        String nombreObjeto = objeto.generarCodigo();

        String resultado = GeneradorTAC.nuevoTemporal();

        System.out.println(
            resultado + " = CALL_METHOD " +
            nombreObjeto + ", " + etiqueta
        );

        return resultado;
    }
}
