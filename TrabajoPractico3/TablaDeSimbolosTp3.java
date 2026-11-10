package TrabajoPractico3;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong; // Generar números de orden únicos para los registros.

public class TablaDeSimbolosTp3 {
    public static final String AMBITO_GLOBAL = "main";
    
    // Identificadores que utilizamos para determinar qué representa cada registro.
    public static final String USO_PROGRAMA = "PROGRAMA";
    public static final String USO_VARIABLE = "VARIABLE";
    public static final String USO_COMPTIME = "VARIABLE_COMPTIME";
    public static final String USO_PARAMETRO = "PARAMETRO";
    public static final String USO_FUNCION = "FUNCION";
    public static final String USO_CLASE = "CLASE";
    public static final String USO_OBJETO = "OBJETO";
    public static final String USO_ATRIBUTO = "ATRIBUTO";
    public static final String USO_METODO = "METODO";
    public static final String USO_CONSTANTE = "CONSTANTE";

    // Estos identificadores representan atributos adicionales que podemos guardar dentro de cada registro.
    private static final String ATR_EXTENDS = "CLASES_PADRE";
    private static final String ATR_CLASE = "CLASE";
    private static final String ATR_COMPTIME = "COMPTIME";

    private final Map<String, RegistroSimboloTp3> tabla = new LinkedHashMap<>(); 
    private final AtomicLong secuencia = new AtomicLong(0); // Se utiliza para asignar un número consecutivo a cada nuevo registro.
   
    private String ambitoActual = AMBITO_GLOBAL; // Guarda el ámbito en el que estamos trabajando actualmente. Por defecto es la raiz "main".

    public void setAmbitoActual(String ambitoActual) { // Este método permite actualizar el ámbito actual.
        validarTexto(ambitoActual, "ambito actual");
        this.ambitoActual = ambitoActual;
    }

    public String getAmbitoActual() {
        return ambitoActual;
    }

    // REGISTRO DE CONSTANTES: ANALISIS LEXICO
    public RegistroSimboloTp3 agregarConstante(String lexema, String tipoDato, int linea) {
        validarTexto(lexema, "lexema de constante");
        validarTexto(tipoDato, "tipo de constante");
        String clave = construirClaveConstante(tipoDato, lexema); // Ejemplo de clave: "CONST|USHORTINT|10$us"
        RegistroSimboloTp3 existente = tabla.get(clave); // Buscamos en el mapa si ya tenemos esa misma constante.
        if (existente != null) {
            return existente;
        }
        // Creamos un objeto con toda la información de la constante.
        RegistroSimboloTp3 nueva = new RegistroSimboloTp3(lexema,"CONSTANTE",tipoDato,USO_CONSTANTE,"<constantes>",linea,secuencia.incrementAndGet());
        nueva.agregarAtributo("VALOR", lexema); // Agregamos un atributo adicional con su valor original.
        tabla.put(clave, nueva); // Lo agregamos a la tabla
        return nueva;
    }

    // Declara un simbolo en un ambito especifico.Retorna null si ya existe una declaracion con ese nombre en ese ambito.
    // este método solamente comprueba redeclaraciones locales. No impide utilizar el mismo nombre en otro ámbito.
    public RegistroSimboloTp3 declarar(String nombre,String tipo,String uso,String ambito,int linea) {
        validarTexto(nombre, "nombre");
        validarTexto(uso, "uso");
        validarTexto(ambito, "ambito"); 
        String clave = construirClaveDeclaracion(nombre, ambito);
        if (tabla.containsKey(clave)) {
            return null;
        }
        RegistroSimboloTp3 nuevo = new RegistroSimboloTp3(nombre,"IDENTIFICADOR",tipo,uso,ambito,linea,secuencia.incrementAndGet());
        tabla.put(clave, nuevo);
        return nuevo;
    }

    public RegistroSimboloTp3 declararEnAmbitoActual(String nombre, String tipo, String uso, int linea) {// Atajo para declarar en el ambito actual.
        return declarar(nombre, tipo, uso, ambitoActual, linea);
    }

    public RegistroSimboloTp3 declararPrograma(String nombre, int linea) {  // Registra el nombre del programa en el ambito global main.
        return declarar(nombre, "PROGRAMA", USO_PROGRAMA, AMBITO_GLOBAL, linea);
    }

    // DECLARACIONES ESPECIFICAS
    public RegistroSimboloTp3 declararVariable(String nombre, String tipo, int linea, boolean comptime) { // Este método permite declarar variables normales y variables comptime
        String uso = comptime ? USO_COMPTIME : USO_VARIABLE; // Si recibe false, tenemos una variable normal. Si recibe true, tenemos una variable de tiempo de compilación.
        RegistroSimboloTp3 registro = declararEnAmbitoActual( nombre, tipo, uso, linea);
        if (registro != null && comptime) {
            registro.agregarAtributo(ATR_COMPTIME, Boolean.TRUE);
        }
        return registro;
    }

    public RegistroSimboloTp3 declararFuncion(String nombre, String tipoRetorno, int linea) { // Registra una función.
        return declararEnAmbitoActual(nombre, tipoRetorno, USO_FUNCION, linea); 
    }

    public RegistroSimboloTp3 declararClase(String nombre, int linea) { // Registra una clase.
        RegistroSimboloTp3 clase = declararEnAmbitoActual( nombre, "CLASS", USO_CLASE, linea);
        if (clase != null) {
            clase.agregarAtributo(ATR_EXTENDS, new ArrayList<String>()); // Creamos una lista vacía para almacenar las clases de las que hereda.
        }
        return clase;
    }

    public RegistroSimboloTp3 declararParametro(String nombre, String tipo, int linea) { // Registra los parámetros formales de una función o método.
        return declararEnAmbitoActual(nombre, tipo, USO_PARAMETRO, linea);
    }

    public RegistroSimboloTp3 declararAtributoClase(String nombre, String tipo, int linea) { // Esto nos permite distinguir un atributo de una variable local.
        return declararEnAmbitoActual(nombre, tipo, USO_ATRIBUTO, linea);
    }

    public RegistroSimboloTp3 declararMetodoClase(String nombre, String tipoRetorno, int linea) { // Registra un método dentro de una clase.
        return declararEnAmbitoActual(nombre, tipoRetorno, USO_METODO, linea);
    }

    public RegistroSimboloTp3 declararObjeto(String nombre, String nombreClase, int linea) { //Declara una variable cuyo tipo es una clase visible. 
        RegistroSimboloTp3 clase = buscarClaseVisible(nombreClase, ambitoActual);
        if (clase == null) {
            return null;
        }
        RegistroSimboloTp3 objeto = declararEnAmbitoActual(nombre, nombreClase, USO_OBJETO, linea);
        if (objeto != null) {
            objeto.agregarAtributo(ATR_CLASE, clase.getNombreCompleto());
        }
        return objeto;
    }

    // FIRMA DE FUNCIONES Y METODOS: TEMA 20
    // Agrega un parametro formal a la firma de una funcion/metodo.
    public boolean agregarParametroFormal(String nombreFuncion,String nombreParametro,String tipoParametro,int lineaParametro, String ambitoDeclaracion) {
        RegistroSimboloTp3 funcion = buscarLocal(nombreFuncion, ambitoDeclaracion);
        if (funcion == null || !(USO_FUNCION.equals(funcion.getUso()) || USO_METODO.equals(funcion.getUso()))) {
            return false;
        }
        return funcion.agregarParametro( nombreParametro, tipoParametro, lineaParametro);
    }

    public boolean validarCantidadParametros(String nombreFuncion,String ambitoDeclaracion,int cantidadRecibida) {
        RegistroSimboloTp3 funcion = buscarLocal(nombreFuncion, ambitoDeclaracion);
        return funcion != null && (USO_FUNCION.equals(funcion.getUso())|| USO_METODO.equals(funcion.getUso()))&& funcion.getCantidadParametros() == cantidadRecibida;
    }

    // BUSQUEDA Y VISIBILIDAD
    public boolean estaDeclaradoLocalmente(String nombre, String ambito) { // Comprueba si existe una declaración exactamente en ese ámbito.
        return buscarLocal(nombre, ambito) != null;  // No busca en los ámbitos padres.
    }

    public RegistroSimboloTp3 buscar(String nombre, String ambito) { // Busca desde el ambito dado hacia todos sus ambitos contenedores.
        if (nombre == null || ambito == null) {
            return null;
        }
        String ambitoBusqueda = ambito;
        while (ambitoBusqueda != null && !ambitoBusqueda.isEmpty()) {
            RegistroSimboloTp3 encontrado = tabla.get(
                construirClaveDeclaracion(nombre, ambitoBusqueda)
            );
            if (encontrado != null) {
                return encontrado;
            }
            int posicionSeparador = ambitoBusqueda.lastIndexOf(':');
            if (posicionSeparador == -1) {
                break;
            }
            ambitoBusqueda = ambitoBusqueda.substring(0, posicionSeparador);
        }
        return null;
    }

    public RegistroSimboloTp3 buscar(String nombre) {return buscar(nombre, ambitoActual); } // En lugar de pasar el ámbito manualmente, utiliza el ámbito que tiene configurado la tabla.


    public RegistroSimboloTp3 buscarLocal(String nombre, String ambito) {// Busca exclusivamente dentro del ambito indicado.
        if (nombre == null || ambito == null) {
            return null;
        }
        return tabla.get(construirClaveDeclaracion(nombre, ambito));
    }


    public RegistroSimboloTp3 obtenerPorNombreCompleto(String nombreCompleto) {// Busca una declaracion por su nombre completo, por ejemplo x:main:f. 
        if (nombreCompleto == null) {
            return null;
        }
        return tabla.get("ID|" + nombreCompleto);
    }

    public RegistroSimboloTp3 obtener(String nombre) {// Busca primero declaraciones visibles y luego constantes. 
        RegistroSimboloTp3 simbolo = buscar(nombre);
        if (simbolo != null) {
            return simbolo;
        }
        return obtenerConstante(nombre);
    }

    public boolean contiene(String nombre) {
        return buscar(nombre) != null || contieneConstante(nombre);
    }

    public RegistroSimboloTp3 resolverVariable(String nombre) {    // Resuelve un identificador que debe poder usarse como valor.
        RegistroSimboloTp3 registro = buscar(nombre);
        if (registro == null) {
            return null;
        }
        String uso = registro.getUso();
        if (USO_VARIABLE.equals(uso)|| USO_COMPTIME.equals(uso)|| USO_PARAMETRO.equals(uso)|| USO_OBJETO.equals(uso)|| USO_ATRIBUTO.equals(uso)) {
            return registro;
        }
        return null;
    }

    public RegistroSimboloTp3 resolverFuncion(String nombre) {    //Resuelve un identificador que debe ser una funcion o metodo.
        RegistroSimboloTp3 registro = buscar(nombre);
        if (registro == null) {
            return null;
        }
        String uso = registro.getUso();
        if (USO_FUNCION.equals(uso) || USO_METODO.equals(uso)) {
            return registro;
        }
        return null;
    }

    // CONSTANTES
    public boolean contieneConstante(String lexema) {
        return obtenerConstante(lexema) != null;
    }
    
    public RegistroSimboloTp3 obtenerConstante(String lexema) {// Busca una constante por lexema. Si hay tipos diferentes, retorna la primera.
        if (lexema == null) {
            return null;
        }
        for (RegistroSimboloTp3 registro : tabla.values()) {
            if (USO_CONSTANTE.equals(registro.getUso())  && lexema.equals(registro.getLexema())) {
                return registro;
            }
        }
        return null;
    }

    public RegistroSimboloTp3 obtenerConstante(String lexema, String tipo) {    // Busca una constante por lexema y tipo exactos. 
        if (lexema == null || tipo == null) {
            return null;
        }
        RegistroSimboloTp3 registro = tabla.get(construirClaveConstante(tipo, lexema));
        return registro;
    }

    public Collection<RegistroSimboloTp3> obtenerTodos() { return Collections.unmodifiableCollection(tabla.values());}

    public Collection<RegistroSimboloTp3> obtenerSimbolosDeclarados() {
        List<RegistroSimboloTp3> resultado = new ArrayList<>();

        for (RegistroSimboloTp3 registro : tabla.values()) {
            if (!USO_CONSTANTE.equals(registro.getUso())) {
                resultado.add(registro);
            }
        }

        return Collections.unmodifiableList(resultado);
    }

    public Collection<RegistroSimboloTp3> obtenerConstantes() {
        List<RegistroSimboloTp3> resultado = new ArrayList<>();

        for (RegistroSimboloTp3 registro : tabla.values()) {
            if (USO_CONSTANTE.equals(registro.getUso())) {
                resultado.add(registro);
            }
        }

        return Collections.unmodifiableList(resultado);
    }

    public List<RegistroSimboloTp3> obtenerSimbolosEnAmbito(String ambito) {
        List<RegistroSimboloTp3> resultado = new ArrayList<>();

        for (RegistroSimboloTp3 registro : tabla.values()) {
            if (!USO_CONSTANTE.equals(registro.getUso())
                    && ambito.equals(registro.getAmbito())) {
                resultado.add(registro);
            }
        }

        return resultado;
    }

    public void imprimirTabla() {
        System.out.println("\n====================== TABLA DE SIMBOLOS ======================");
        System.out.printf("%-30s | %-12s | %-20s | %-22s | %-6s%n",
            "NOMBRE COMPLETO", "TIPO", "USO", "AMBITO", "LINEA");
        System.out.println("------------------------------------------------------------------------------------------------------");

        for (RegistroSimboloTp3 registro : tabla.values()) {
            boolean constante = USO_CONSTANTE.equals(registro.getUso());

            String nombreVisible;
            String ambitoVisible;

            if (constante) {
                nombreVisible = registro.getLexema();
                ambitoVisible = "<constantes>";
            } else {
                nombreVisible = registro.getNombreCompleto();
                ambitoVisible = texto(registro.getAmbito());
            }

            System.out.printf("%-30s | %-12s | %-20s | %-22s | %-6d%n",
                nombreVisible,
                texto(registro.getTipo()),
                texto(registro.getUso()),
                ambitoVisible,
                registro.getLinea());

            if (!registro.getParametros().isEmpty()) {
                System.out.println("  Parametros formales: " + registro.getParametros());
            }

            if (registro.esComptime()) {
                System.out.println("  COMPTIME: " + registro.getValorComptime());
            }

            if (registro.tieneAtributo(ATR_EXTENDS)) {
                System.out.println("  EXTENDS: " + registro.obtenerAtributo(ATR_EXTENDS));
            }

            if (registro.tieneAtributo(ATR_CLASE)) {
                System.out.println("  Clase del objeto: " + registro.obtenerAtributo(ATR_CLASE));
            }
        }

        System.out.println("======================================================================================================\n");
    }


    // TEMA 22: VARIABLES COMPTIME
    public boolean actualizarValorComptime(String nombre, String valor, int linea) {    // Actualiza el valor conocido en compilacion de una variable comptime. 
        RegistroSimboloTp3 registro = buscar(nombre);

        if (registro == null || !registro.esComptime()) {
            return false;
        }

        if (valor == null || valor.trim().isEmpty()) {
            return false;
        }

        registro.setValorComptime(valor);
        registro.agregarAtributo("LINEA_ULTIMA_ASIGNACION", linea);
        return true;
    }

    public String obtenerValorComptime(String nombre) { // Devuelve el valor conocido de una variable comptime, o null si no se conoce. 
        RegistroSimboloTp3 registro = buscar(nombre);

        if (registro == null || !registro.esComptime()
                || !registro.tieneValorComptime()) {
            return null;
        }

        return registro.getValorComptime();
    }

    // TEMAS 25, 29 Y 30: CLASES, HERENCIA Y ATRIBUTOS
    public RegistroSimboloTp3 buscarClaseVisible(String nombreClase, String ambito) {// Busca una clase visible desde el ambito indicado. 
        RegistroSimboloTp3 registro = buscar(nombreClase, ambito);
        if (registro != null && USO_CLASE.equals(registro.getUso())) {
            return registro;
        }
        return null;
    }

    //Registra una clase base respetando el orden en que aparece en EXTENDS. Rechaza clases inexistentes, herencia repetida y ciclos de herencia.
    public boolean agregarClaseBase(String clase, String claseBase) {
        RegistroSimboloTp3 registroClase = buscarClaseVisible(clase, ambitoActual);
        RegistroSimboloTp3 registroBase = buscarClaseVisible(claseBase, ambitoActual);

        if (registroClase == null || registroBase == null) {
            return false;
        }

        if (registroClase.getNombreCompleto().equals(registroBase.getNombreCompleto())) {
            return false;
        }

        if (heredaDe(registroBase, registroClase.getNombreCompleto(), new LinkedHashSet<String>())) {
            return false;
        }

        @SuppressWarnings("unchecked")
        List<String> clasesPadre = (List<String>) registroClase.obtenerAtributo(ATR_EXTENDS);

        if (clasesPadre == null) {
            clasesPadre = new ArrayList<>();
            registroClase.agregarAtributo(ATR_EXTENDS, clasesPadre);
        }

        String nombreCompletoBase = registroBase.getNombreCompleto();

        if (clasesPadre.contains(nombreCompletoBase)) {
            return false;
        }

        // LinkedList se evita: ArrayList conserva el orden declarado de EXTENDS.
        clasesPadre.add(nombreCompletoBase);
        return true;
    }

    private boolean heredaDe(RegistroSimboloTp3 clase,String nombreCompletoBuscado,Set<String> visitadas) {//Verifica recursivamente si una clase hereda de otra. 
        if (clase == null || !visitadas.add(clase.getNombreCompleto())) {
            return false;
        }
        Object basesObjeto = clase.obtenerAtributo(ATR_EXTENDS);
        if (!(basesObjeto instanceof List<?>)) {
            return false;
        }
        for (Object referenciaBase : (List<?>) basesObjeto) {
            String nombreBase = String.valueOf(referenciaBase);

            if (nombreCompletoBuscado.equals(nombreBase)) {
                return true;
            }

            RegistroSimboloTp3 base = buscarClasePorReferencia(nombreBase);

            if (heredaDe(base, nombreCompletoBuscado, visitadas)) {
                return true;
            }
        }

        return false;
    }

    public RegistroSimboloTp3 resolverMiembroClase( String nombreClase, String nombreMiembro) { //Busca un miembro propio o heredado de una clase.
        RegistroSimboloTp3 clase = buscarClaseVisible(nombreClase, ambitoActual);
        if (clase == null) {
            return null;
        }
        return resolverMiembroClase(clase,nombreMiembro,new LinkedHashSet<String>());
    }

    private RegistroSimboloTp3 resolverMiembroClase(RegistroSimboloTp3 clase,String nombreMiembro,Set<String> visitadas) {
        if (clase == null || !visitadas.add(clase.getNombreCompleto())) {
            return null;
        }
        String ambitoClase = clase.getAmbito() + ":" + clase.getLexema();
        RegistroSimboloTp3 miembroPropio = buscarLocal(nombreMiembro, ambitoClase);

        if (miembroPropio != null &&
            (USO_ATRIBUTO.equals(miembroPropio.getUso()) ||
            USO_OBJETO.equals(miembroPropio.getUso()) ||
            USO_METODO.equals(miembroPropio.getUso()))) {

            return miembroPropio;
        }

        Object basesObjeto = clase.obtenerAtributo(ATR_EXTENDS);

        if (basesObjeto instanceof List<?>) {
            for (Object referenciaBase : (List<?>) basesObjeto) {
                RegistroSimboloTp3 base = buscarClasePorReferencia(
                    String.valueOf(referenciaBase)
                );

                RegistroSimboloTp3 heredado = resolverMiembroClase(
                    base,
                    nombreMiembro,
                    visitadas
                );

                if (heredado != null) {
                    return heredado;
                }
            }
        }

        return null;
    }
   
    private RegistroSimboloTp3 buscarClasePorReferencia(String referencia) { // Obtiene una clase por nombre visible o por nombre completo. 
        if (referencia == null) {
            return null;
        }
        RegistroSimboloTp3 clase;

        if (referencia.indexOf(':') >= 0) {
            clase = obtenerPorNombreCompleto(referencia);
        } else {
            clase = buscarClaseVisible(referencia, ambitoActual);
        }

        if (clase != null && USO_CLASE.equals(clase.getUso())) {
            return clase;
        }

        return null;
    }

    // Tema 29: devuelve el atributo por posicion, comenzando desde indice 0. se consideran primero los atributos propios y luego los heredados, Respetando el orden de EXTENDS.
    public RegistroSimboloTp3 obtenerAtributoPosicional(String nombreClase, int indice) {

        if (indice < 0) {return null;}

        RegistroSimboloTp3 clase = buscarClaseVisible(nombreClase, ambitoActual);

        if (clase == null) {return null;}

        List<RegistroSimboloTp3> atributos = obtenerAtributosEfectivos(clase,new LinkedHashSet<String>());

        if (indice >= atributos.size()) {return null;}

        return atributos.get(indice);
    }

    private List<RegistroSimboloTp3> obtenerAtributosEfectivos(RegistroSimboloTp3 clase,Set<String> visitadas) {

        List<RegistroSimboloTp3> resultado = new ArrayList<>();

        if (clase == null || !visitadas.add(clase.getNombreCompleto())) {
            return resultado;
        }

        String ambitoClase = clase.getAmbito() + ":" + clase.getLexema();
        for (RegistroSimboloTp3 miembro : obtenerSimbolosEnAmbito(ambitoClase)) {

            if (USO_ATRIBUTO.equals(miembro.getUso()) ||
                USO_OBJETO.equals(miembro.getUso())) {

                resultado.add(miembro);
            }
        }

        Object basesObjeto = clase.obtenerAtributo(ATR_EXTENDS);

        if (basesObjeto instanceof List<?>) {
            for (Object referenciaBase : (List<?>) basesObjeto) {
                RegistroSimboloTp3 base = buscarClasePorReferencia(String.valueOf(referenciaBase));
                resultado.addAll(obtenerAtributosEfectivos(base, visitadas));
            }
        }

        return resultado;
    }

    // Tema 25: compara estructuralmente dos clases. Criterio aplicado: misma cantidad, orden, nombre y tipo de atributos Efectivos. La consigna no detalla una regla mas amplia de equivalencia.
    public boolean estructurasCompatibles(String nombreClaseA, String nombreClaseB) {
        RegistroSimboloTp3 claseA = buscarClaseVisible(nombreClaseA, ambitoActual);
        RegistroSimboloTp3 claseB = buscarClaseVisible(nombreClaseB, ambitoActual);

        if (claseA == null || claseB == null) {
            return false;
        }

        List<RegistroSimboloTp3> atributosA = obtenerAtributosEfectivos(claseA, new LinkedHashSet<String>());
        List<RegistroSimboloTp3> atributosB = obtenerAtributosEfectivos(claseB, new LinkedHashSet<String>());

        if (atributosA.size() != atributosB.size()) {
            return false;
        }

        for (int i = 0; i < atributosA.size(); i++) {
            RegistroSimboloTp3 atributoA = atributosA.get(i);
            RegistroSimboloTp3 atributoB = atributosB.get(i);
            if (!atributoA.getLexema().equals(atributoB.getLexema())) {
                return false;
            }
            if (!equalsIgnorandoMayusculas(atributoA.getTipo(), atributoB.getTipo())) {
                return false;
            }
        }
        return true;
    }

    public boolean tiposObjetoCompatibles(String tipoDestino, String tipoOrigen) {
        if (equalsIgnorandoMayusculas(tipoDestino, tipoOrigen)) {
            return buscarClaseVisible(tipoDestino, ambitoActual) != null;
        }
        return estructurasCompatibles(tipoDestino, tipoOrigen);
    }

    // TEMA 20: ORDEN OPCIONAL DE PARAMETROS
    // Valida que el orden recibido sea una permutacion de 1..n. 
    public boolean validarOrdenParametros(List<Integer> orden, int cantidadParametros) {
        if (cantidadParametros < 0) {return false;}
        if (orden == null) {return true;}        // Sin lista explicita, se utiliza el orden natural.
        if (orden.size() != cantidadParametros) {return false;  }

        boolean[] posicionesVistas = new boolean[cantidadParametros + 1];
        for (Integer posicion : orden) {
            if (posicion == null || posicion < 1 || posicion > cantidadParametros || posicionesVistas[posicion]) {
                return false;
            }
            posicionesVistas[posicion] = true;
        }
        return true;
    }

    // Devuelve indices base cero para copiar los argumentos en el orden solicitado. 
    public List<Integer> obtenerOrdenDeCopia(List<Integer> orden, int cantidadParametros) {

        if (!validarOrdenParametros(orden, cantidadParametros)) {
            throw new IllegalArgumentException("El orden de parametros no es una permutacion valida de 1..n." );
        }

        List<Integer> resultado = new ArrayList<>();

        if (orden == null) {
            for (int i = 0; i < cantidadParametros; i++) {
                resultado.add(i);
            }
        } else {
            for (Integer posicion : orden) {
                resultado.add(posicion - 1);
            }
        }
        return resultado;
    }

    //Devuelve los parametros formales en el orden de copia solicitado.Retorna null si no existe la funcion/metodo o si el orden es invalido.
    public List<RegistroSimboloTp3.ParametroTp3> obtenerParametrosEnOrden(String nombreFuncion,String ambitoDeclaracion, List<Integer> orden) {

        RegistroSimboloTp3 funcion = buscarLocal(nombreFuncion, ambitoDeclaracion);

        if (funcion == null) {
            return null;
        }

        if (!USO_FUNCION.equals(funcion.getUso()) && !USO_METODO.equals(funcion.getUso())) {
            return null;
        }

        List<RegistroSimboloTp3.ParametroTp3> parametros = funcion.getParametros();

        // Comprobar que el orden sea válido antes de procesarlo.
        if (!validarOrdenParametros(orden, parametros.size())) {
            return null;
        }

        // Obtener las posiciones en orden de copia.
        List<Integer> posiciones = obtenerOrdenDeCopia(orden, parametros.size());

        List<RegistroSimboloTp3.ParametroTp3> resultado = new ArrayList<>();

        for (Integer posicion : posiciones) {
            resultado.add(parametros.get(posicion));
        }

        return Collections.unmodifiableList(resultado);
    }

    // TEMA 33: CONVERSION EXPLICITA ENTERO -> DOUBLEF (TODF)
    public boolean esEntero(String tipo) {
        if (tipo == null) {
            return false;
        }
        String normalizado = normalizarTipo(tipo);
        return "SHORTINT".equals(normalizado) || "USHORTINT".equals(normalizado)|| "INTEGER".equals(normalizado)|| "UINTEGER".equals(normalizado)
        || "LONGINT".equals(normalizado)|| "ULONGINT".equals(normalizado);
    }

    public boolean esFlotante(String tipo) {    //Tipos de punto flotante contemplados por la especificacion.
        if (tipo == null) {return false;}
        String normalizado = normalizarTipo(tipo);
        return "SINGLEF".equals(normalizado) || "DOUBLEF".equals(normalizado);
    }

    // Valida el operando de todf y devuelve el tipo resultante.Para este grupo, el tipo flotante implementado en el lexer es DOUBLEF. Devuelve null cuando el operando no es entero.
    public String tipoResultadoTodf(String tipoOriginal) {
        if (!esEntero(tipoOriginal)) {
            return null;
        }
        return "DOUBLEF";
    }

    //Comprueba tipos para una operacion.Si los tipos son distintos, conversionExplicitaTodf solo debe ser true cuando el parser ya verifico que el operando entero esta envuelto en todf.
    public boolean tiposCompatiblesParaOperacion(String tipoIzquierdo,String tipoDerecho,boolean conversionExplicitaTodf) {
        if (equalsIgnorandoMayusculas(tipoIzquierdo, tipoDerecho)) {return true;}
        if (!conversionExplicitaTodf) { return false;}
        return (esEntero(tipoIzquierdo) && esFlotante(tipoDerecho))|| (esFlotante(tipoIzquierdo) && esEntero(tipoDerecho));
    }

    // Comprueba asignaciones segun el tema 33.Una expresion entera puede asignarse a DOUBLEF solamente si el lado derecho incluye una conversion todf valida.
    public boolean tipoCompatibleAsignacion(String tipoDestino,String tipoExpresion, boolean contieneTodf) {
        if (equalsIgnorandoMayusculas(tipoDestino, tipoExpresion)) {return true;}
        return contieneTodf && esFlotante(tipoDestino)&& esEntero(tipoExpresion);
    }

    private static String construirNombreCompleto(String nombre, String ambito) { return nombre + ":" + ambito;}

    private static String construirClaveDeclaracion(String nombre, String ambito) {
        return "ID|" + construirNombreCompleto(nombre, ambito);
    }

    private static String construirClaveConstante(String tipo, String lexema) {
        return "CONST|" + normalizarTipo(tipo) + "|" + lexema;
    }

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " no puede ser vacio.");
        }
    }

    private static String texto(String valor) { return valor == null ? "-" : valor;}

    private static String normalizarTipo(String tipo) {
        return tipo == null ? "" : tipo.trim().toUpperCase(Locale.ROOT);
    }

    private static boolean equalsIgnorandoMayusculas(String primero, String segundo) {
        if (primero == null) {
            return segundo == null;
        }
        return segundo != null && primero.equalsIgnoreCase(segundo);
    }
}
