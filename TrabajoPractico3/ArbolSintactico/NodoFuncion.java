
package TrabajoPractico3.ArbolSintactico;

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

    public String getNombre() {
        return nombre;
    }

    public String getAmbitoCompleto() {
        return ambitoCompleto;
    }

    public NodoAST getParametros() {
        return parametros;
    }

    public NodoAST getCuerpoDeclarativo() {
        return cuerpoDeclarativo;
    }

    public NodoAST getCuerpoEjecutable() {
        return cuerpoEjecutable;
    }


    @Override
    public String generarCodigo() {
        System.out.println("; --- INICIO FUNCION " + nombre + " ---");// Encabezado de la función
        String etiquetaInicio = "FUNC_" + ambitoCompleto.replace(':', '_') + ":";// Etiqueta de inicio
        System.out.println(etiquetaInicio);

        if (cuerpoDeclarativo != null) {// Generar declaraciones locales
            cuerpoDeclarativo.generarCodigo();
        }

        if (cuerpoEjecutable != null) {// Generar instrucciones de la función
            cuerpoEjecutable.generarCodigo();
        }

        System.out.println("RET");// Retorno de finalización de la función

        return null;
    }

}
