package TrabajoPractico4;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong; 

public class TablaDeSimbolosTp4 {
    public static final String AMBITO_GLOBAL = "main";
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
    private static final String ATR_EXTENDS = "CLASES_PADRE";
    private static final String ATR_CLASE = "CLASE";
    private static final String ATR_COMPTIME = "COMPTIME";
    private final Map<String, RegistroSimboloTp4> tabla = new LinkedHashMap<>(); 
    private final AtomicLong secuencia = new AtomicLong(0); 
    private String ambitoActual = AMBITO_GLOBAL;
    private static final BigDecimal MIN_NEG_DOUBLE = new BigDecimal("-1.7976931348623157e+308");
    private static final BigDecimal MAX_NEG_DOUBLE = new BigDecimal("-2.2250738585072014e-308");

    public void setAmbitoActual(String ambitoActual) { 
        validarTexto(ambitoActual, "ambito actual");
        this.ambitoActual = ambitoActual;
    }

    public String getAmbitoActual() { return ambitoActual; }

    public RegistroSimboloTp4 agregarConstante(String lexema, String tipoDato, int linea) {
        validarTexto(lexema, "lexema de constante");
        validarTexto(tipoDato, "tipo de constante");
        String clave = construirClaveConstante(tipoDato, lexema); 
        RegistroSimboloTp4 existente = tabla.get(clave); 
        if (existente != null) {
            return existente;
        }
        RegistroSimboloTp4 nueva = new RegistroSimboloTp4(lexema,"CONSTANTE",tipoDato,USO_CONSTANTE,"<constantes>",linea,secuencia.incrementAndGet());
        nueva.agregarAtributo("VALOR", lexema); 
        tabla.put(clave, nueva); 
        return nueva;
    }

    public RegistroSimboloTp4 declarar(String nombre,String tipo,String uso,String ambito,int linea) {
        validarTexto(nombre, "nombre");
        validarTexto(uso, "uso");
        validarTexto(ambito, "ambito"); 
        String clave = construirClaveDeclaracion(nombre, ambito);
        if (tabla.containsKey(clave)) {
            return null;
        }
        RegistroSimboloTp4 nuevo = new RegistroSimboloTp4(nombre,"IDENTIFICADOR",tipo,uso,ambito,linea,secuencia.incrementAndGet());
        tabla.put(clave, nuevo);
        return nuevo;
    }

    public RegistroSimboloTp4 declararEnAmbitoActual(String nombre, String tipo, String uso, int linea) { return declarar(nombre, tipo, uso, ambitoActual, linea);}

    public RegistroSimboloTp4 declararPrograma(String nombre, int linea) {
        return declarar(nombre, "PROGRAMA", USO_PROGRAMA, AMBITO_GLOBAL, linea);
    }

    public RegistroSimboloTp4 declararVariable(String nombre, String tipo, int linea, boolean comptime) { 
        String uso = comptime ? USO_COMPTIME : USO_VARIABLE; 
        RegistroSimboloTp4 registro = declararEnAmbitoActual( nombre, tipo, uso, linea);
        if (registro != null && comptime) {
            registro.agregarAtributo(ATR_COMPTIME, Boolean.TRUE);
        }
        return registro;
    }

    public RegistroSimboloTp4 declararFuncion(String nombre, String tipoRetorno, int linea) { return declararEnAmbitoActual(nombre, tipoRetorno, USO_FUNCION, linea); }

    public RegistroSimboloTp4 declararClase(String nombre, int linea) {
        RegistroSimboloTp4 clase = declararEnAmbitoActual( nombre, "CLASS", USO_CLASE, linea);
        if (clase != null) {
            clase.agregarAtributo(ATR_EXTENDS, new ArrayList<String>()); 
        }
        return clase;
    }

    public RegistroSimboloTp4 declararParametro(String nombre, String tipo, int linea) { return declararEnAmbitoActual(nombre, tipo, USO_PARAMETRO, linea);}

    public RegistroSimboloTp4 declararAtributoClase(String nombre, String tipo, int linea) {  return declararEnAmbitoActual(nombre, tipo, USO_ATRIBUTO, linea);}

    public RegistroSimboloTp4 declararMetodoClase(String nombre, String tipoRetorno, int linea) {  return declararEnAmbitoActual(nombre, tipoRetorno, USO_METODO, linea); }

    public RegistroSimboloTp4 declararObjeto(String nombre, String nombreClase, int linea) { 
        RegistroSimboloTp4 clase = buscarClaseVisible(nombreClase, ambitoActual);
        if (clase == null) {
            return null;
        }
        RegistroSimboloTp4 objeto = declararEnAmbitoActual(nombre, nombreClase, USO_OBJETO, linea);
        if (objeto != null) {
            objeto.agregarAtributo(ATR_CLASE, clase.getNombreCompleto());
        }
        return objeto;
    }

    public boolean agregarParametroFormal(String nombreFuncion,String nombreParametro,String tipoParametro,int lineaParametro, String ambitoDeclaracion) {
        RegistroSimboloTp4 funcion = buscarLocal(nombreFuncion, ambitoDeclaracion);
        if (funcion == null || !(USO_FUNCION.equals(funcion.getUso()) || USO_METODO.equals(funcion.getUso()))) {
            return false;
        }
        return funcion.agregarParametro( nombreParametro, tipoParametro, lineaParametro);
    }

    public boolean validarCantidadParametros(String nombreFuncion,String ambitoDeclaracion,int cantidadRecibida) {
        RegistroSimboloTp4 funcion = buscarLocal(nombreFuncion, ambitoDeclaracion);
        return funcion != null && (USO_FUNCION.equals(funcion.getUso())|| USO_METODO.equals(funcion.getUso()))&& funcion.getCantidadParametros() == cantidadRecibida;
    }

    public boolean estaDeclaradoLocalmente(String nombre, String ambito) { return buscarLocal(nombre, ambito) != null;  }

    public RegistroSimboloTp4 buscar(String nombre, String ambito) { 
        if (nombre == null || ambito == null) {
            return null;
        }
        String ambitoBusqueda = ambito;
        while (ambitoBusqueda != null && !ambitoBusqueda.isEmpty()) {
            RegistroSimboloTp4 encontrado = tabla.get(
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

    public RegistroSimboloTp4 buscar(String nombre) {return buscar(nombre, ambitoActual); } 

    public RegistroSimboloTp4 buscarLocal(String nombre, String ambito) {
        if (nombre == null || ambito == null) {
            return null;
        }
        return tabla.get(construirClaveDeclaracion(nombre, ambito));
    }


    public RegistroSimboloTp4 obtenerPorNombreCompleto(String nombreCompleto) {
        if (nombreCompleto == null) {
            return null;
        }
        return tabla.get("ID|" + nombreCompleto);
    }

    public RegistroSimboloTp4 obtener(String nombre) {
        RegistroSimboloTp4 simbolo = buscar(nombre);
        if (simbolo != null) {
            return simbolo;
        }
        return obtenerConstante(nombre);
    }

    public boolean contiene(String nombre) { return buscar(nombre) != null || contieneConstante(nombre); }

    public RegistroSimboloTp4 resolverVariable(String nombre) {
        RegistroSimboloTp4 registro = buscar(nombre);
        if (registro == null) {
            return null;
        }
        String uso = registro.getUso();
        if (USO_VARIABLE.equals(uso)|| USO_COMPTIME.equals(uso)|| USO_PARAMETRO.equals(uso)|| USO_OBJETO.equals(uso)|| USO_ATRIBUTO.equals(uso)) {
            return registro;
        }
        return null;
    }

    public RegistroSimboloTp4 resolverFuncion(String nombre) { 
        RegistroSimboloTp4 registro = buscar(nombre);
        if (registro == null) {
            return null;
        }
        String uso = registro.getUso();
        if (USO_FUNCION.equals(uso) || USO_METODO.equals(uso)) {
            return registro;
        }
        return null;
    }

    public boolean contieneConstante(String lexema) {  return obtenerConstante(lexema) != null; }
    
    public RegistroSimboloTp4 obtenerConstante(String lexema) {
        if (lexema == null) {
            return null;
        }
        for (RegistroSimboloTp4 registro : tabla.values()) {
            if (USO_CONSTANTE.equals(registro.getUso())  && lexema.equals(registro.getLexema())) {
                return registro;
            }
        }
        return null;
    }

    public RegistroSimboloTp4 obtenerConstante(String lexema, String tipo) {
        if (lexema == null || tipo == null) {
            return null;
        }
        RegistroSimboloTp4 registro = tabla.get(construirClaveConstante(tipo, lexema));
        return registro;
    }

    public Collection<RegistroSimboloTp4> obtenerTodos() { return Collections.unmodifiableCollection(tabla.values());}

    public Collection<RegistroSimboloTp4> obtenerSimbolosDeclarados() {
        List<RegistroSimboloTp4> resultado = new ArrayList<>();
        for (RegistroSimboloTp4 registro : tabla.values()) {
            if (!USO_CONSTANTE.equals(registro.getUso())) {
                resultado.add(registro);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public Collection<RegistroSimboloTp4> obtenerConstantes() {
        List<RegistroSimboloTp4> resultado = new ArrayList<>();
        for (RegistroSimboloTp4 registro : tabla.values()) {
            if (USO_CONSTANTE.equals(registro.getUso())) {
                resultado.add(registro);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public List<RegistroSimboloTp4> obtenerSimbolosEnAmbito(String ambito) {
        List<RegistroSimboloTp4> resultado = new ArrayList<>();
        if (ambito == null || ambito.trim().isEmpty()) {
            return resultado;
        }
        for (RegistroSimboloTp4 registro : tabla.values()) {
            if (!USO_CONSTANTE.equals(registro.getUso()) && ambito.equals(registro.getAmbito())) {
                resultado.add(registro);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public void imprimirTabla() {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                    Tabla de símbolos");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.printf("%-30s | %-12s | %-20s | %-22s | %-6s%n","NOMBRE COMPLETO", "TIPO", "USO", "AMBITO", "LINEA");
        System.out.println("------------------------------------------------------------------------------------------------------");
        for (RegistroSimboloTp4 registro : tabla.values()) {
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
            System.out.printf("%-30s | %-12s | %-20s | %-22s | %-6d%n",nombreVisible,texto(registro.getTipo()), texto(registro.getUso()),ambitoVisible,registro.getLinea());
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
        System.out.println("-----------------------------------------------------------------------------------------------------");
    }

    public boolean actualizarValorComptime(String nombre, String valor, int linea) {  
        RegistroSimboloTp4 registro = buscar(nombre);
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

    public String obtenerValorComptime(String nombre) {
        RegistroSimboloTp4 registro = buscar(nombre);
        if (registro == null || !registro.esComptime()
                || !registro.tieneValorComptime()) {
            return null;
        }
        return registro.getValorComptime();
    }

    public RegistroSimboloTp4 buscarClaseVisible(String nombreClase, String ambito) {
        RegistroSimboloTp4 registro = buscar(nombreClase, ambito);
        if (registro != null && USO_CLASE.equals(registro.getUso())) {
            return registro;
        }
        return null;
    }

    public boolean agregarClaseBase(String clase, String claseBase) {
        RegistroSimboloTp4 registroClase = buscarClaseVisible(clase, ambitoActual);
        RegistroSimboloTp4 registroBase = buscarClaseVisible(claseBase, ambitoActual);
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
        clasesPadre.add(nombreCompletoBase);
        return true;
    }

    private boolean heredaDe(RegistroSimboloTp4 clase,String nombreCompletoBuscado,Set<String> visitadas) {
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
            RegistroSimboloTp4 base = buscarClasePorReferencia(nombreBase);
            if (heredaDe(base, nombreCompletoBuscado, visitadas)) {
                return true;
            }
        }
        return false;
    }

    public RegistroSimboloTp4 resolverMiembroClase( String nombreClase, String nombreMiembro) { //Busca un miembro propio o heredado de una clase.
        RegistroSimboloTp4 clase = buscarClaseVisible(nombreClase, ambitoActual);
        if (clase == null) {
            return null;
        }
        return resolverMiembroClase(clase,nombreMiembro,new LinkedHashSet<String>());
    }

    private RegistroSimboloTp4 resolverMiembroClase(RegistroSimboloTp4 clase,String nombreMiembro,Set<String> visitadas) {
        if (clase == null || !visitadas.add(clase.getNombreCompleto())) {
            return null;
        }
        String ambitoClase = clase.getAmbito() + ":" + clase.getLexema();
        RegistroSimboloTp4 miembroPropio = buscarLocal(nombreMiembro, ambitoClase);
        if (miembroPropio != null && (USO_ATRIBUTO.equals(miembroPropio.getUso()) || USO_OBJETO.equals(miembroPropio.getUso()) || USO_METODO.equals(miembroPropio.getUso()))) {
            return miembroPropio;
        }
        Object basesObjeto = clase.obtenerAtributo(ATR_EXTENDS);
        if (basesObjeto instanceof List<?>) {
            for (Object referenciaBase : (List<?>) basesObjeto) {
                RegistroSimboloTp4 base = buscarClasePorReferencia( String.valueOf(referenciaBase));
                RegistroSimboloTp4 heredado = resolverMiembroClase(  base,nombreMiembro, visitadas);
                if (heredado != null) {
                    return heredado;
                }
            }
        }
        return null;
    }

    public RegistroSimboloTp4 resolverMiembroClasePorReferencia(String referenciaClase, String nombreMiembro) {
        RegistroSimboloTp4 clase = buscarClasePorReferencia(referenciaClase);
        if (clase == null) {
            return null;
        }
        return resolverMiembroClase(clase,nombreMiembro,new LinkedHashSet<String>());
    }
   
    private RegistroSimboloTp4 buscarClasePorReferencia(String referencia) { 
        if (referencia == null) {
            return null;
        }
        RegistroSimboloTp4 clase;
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

    public RegistroSimboloTp4 obtenerAtributoPosicional(String nombreClase, int indice) {
        RegistroSimboloTp4 clase = buscarClaseVisible(nombreClase, ambitoActual);
        return obtenerAtributoPosicional(clase, indice);
    }

    public RegistroSimboloTp4 obtenerAtributoPosicionalPorReferencia(String referenciaClase, int indice) {
        return obtenerAtributoPosicional(buscarClasePorReferencia(referenciaClase), indice);
    }

    private RegistroSimboloTp4 obtenerAtributoPosicional( RegistroSimboloTp4 clase, int indice) {
        if (indice < 0 || clase == null) {
            return null;
        }
        List<RegistroSimboloTp4> atributos = obtenerAtributosEfectivos(  clase,  new LinkedHashSet<String>() );
        if (indice >= atributos.size()) {
            return null;
        }
        return atributos.get(indice);
    }

    public List<RegistroSimboloTp4> obtenerAtributosPosicionalesPorReferencia(String referenciaClase) {
        RegistroSimboloTp4 clase = buscarClasePorReferencia(referenciaClase);
        if (clase == null) {
            return Collections.emptyList();
        }
        List<RegistroSimboloTp4> atributos = obtenerAtributosEfectivos(  clase, new LinkedHashSet<String>());
        return Collections.unmodifiableList( new ArrayList<>(atributos));
    }

    private List<RegistroSimboloTp4> obtenerAtributosEfectivos(RegistroSimboloTp4 clase, Set<String> visitadas) {
        List<RegistroSimboloTp4> resultado = new ArrayList<>();
        if (clase == null || !visitadas.add(clase.getNombreCompleto())) {
            return resultado;
        }
        String ambitoClase = clase.getAmbito() + ":" + clase.getLexema();
        for (RegistroSimboloTp4 miembro : obtenerSimbolosEnAmbito(ambitoClase)) {
            if (USO_ATRIBUTO.equals(miembro.getUso()) || USO_OBJETO.equals(miembro.getUso())) {
                resultado.add(miembro);
            }
        }
        Object basesObjeto = clase.obtenerAtributo(ATR_EXTENDS);
        if (basesObjeto instanceof List<?>) {
            for (Object referenciaBase : (List<?>) basesObjeto) {
                RegistroSimboloTp4 base =  buscarClasePorReferencia(String.valueOf(referenciaBase));
                resultado.addAll(  obtenerAtributosEfectivos(base, visitadas) );
            }
        }
        return resultado;
    }

   public boolean estructurasCompatibles(String nombreClaseA, String nombreClaseB) {
        RegistroSimboloTp4 claseA = buscarClaseVisible(nombreClaseA, ambitoActual);
        RegistroSimboloTp4 claseB = buscarClaseVisible(nombreClaseB, ambitoActual);
        if (claseA == null || claseB == null) {
            return false;
        }
        List<RegistroSimboloTp4> atributosA = obtenerAtributosEfectivos(claseA, new LinkedHashSet<String>());
        List<RegistroSimboloTp4> atributosB = obtenerAtributosEfectivos(claseB, new LinkedHashSet<String>());
        if (atributosA.size() != atributosB.size()) {
            return false;
        }
        for (int i = 0; i < atributosA.size(); i++) {
            RegistroSimboloTp4 atributoA = atributosA.get(i);
            RegistroSimboloTp4 atributoB = atributosB.get(i);
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

    public boolean validarOrdenParametros(List<Integer> orden, int cantidadParametros) {
        if (cantidadParametros < 0) {return false;}
        if (orden == null) {return true;}     
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

    public List<RegistroSimboloTp4.ParametroTp4> obtenerParametrosEnOrden(String nombreFuncion,String ambitoDeclaracion, List<Integer> orden) {
        RegistroSimboloTp4 funcion = buscarLocal(nombreFuncion, ambitoDeclaracion);
        if (funcion == null) {
            return null;
        }
        if (!USO_FUNCION.equals(funcion.getUso()) && !USO_METODO.equals(funcion.getUso())) {
            return null;
        }
        List<RegistroSimboloTp4.ParametroTp4> parametros = funcion.getParametros();
        if (!validarOrdenParametros(orden, parametros.size())) {  
            return null;
        }
        List<Integer> posiciones = obtenerOrdenDeCopia(orden, parametros.size());
        List<RegistroSimboloTp4.ParametroTp4> resultado = new ArrayList<>();
        for (Integer posicion : posiciones) {
            resultado.add(parametros.get(posicion));
        }
        return Collections.unmodifiableList(resultado);
    }

    public boolean esEntero(String tipo) {   
        if (tipo == null) {
            return false;
        }
        String normalizado = normalizarTipo(tipo);
        return "SHORTINT".equals(normalizado) || "USHORTINT".equals(normalizado)|| "INTEGER".equals(normalizado)|| "UINTEGER".equals(normalizado) || "LONGINT".equals(normalizado)|| "ULONGINT".equals(normalizado);
    }

    public boolean esFlotante(String tipo) { 
        if (tipo == null) {return false;}
        String normalizado = normalizarTipo(tipo);
        return "SINGLEF".equals(normalizado) || "DOUBLEF".equals(normalizado);
    }

    public String tipoResultadoTodf(String tipoOriginal) {
        if (!esEntero(tipoOriginal)) {
            return null;
        }
        return "DOUBLEF";
    }

    public boolean tiposCompatiblesParaOperacion(String tipoIzquierdo,String tipoDerecho,boolean conversionExplicitaTodf) {
        if (equalsIgnorandoMayusculas(tipoIzquierdo, tipoDerecho)) {return true;}
        if (!conversionExplicitaTodf) { return false;}
        return (esEntero(tipoIzquierdo) && esFlotante(tipoDerecho))|| (esFlotante(tipoIzquierdo) && esEntero(tipoDerecho));
    }

    public boolean tipoCompatibleAsignacion(String tipoDestino,String tipoExpresion, boolean contieneTodf) {
        if (equalsIgnorandoMayusculas(tipoDestino, tipoExpresion)) {return true;}
        return contieneTodf && esFlotante(tipoDestino)&& esEntero(tipoExpresion);
    }

    private static String construirNombreCompleto(String nombre, String ambito) { return nombre + ":" + ambito;}

    private static String construirClaveDeclaracion(String nombre, String ambito) { return "ID|" + construirNombreCompleto(nombre, ambito);}

    private static String construirClaveConstante(String tipo, String lexema) {  return "CONST|" + normalizarTipo(tipo) + "|" + lexema;}

    private static void validarTexto(String texto, String campo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " no puede ser vacio.");
        }
    }

    private static String texto(String valor) { return valor == null ? "-" : valor;}

    private static String normalizarTipo(String tipo) { return tipo == null ? "" : tipo.trim().toUpperCase(Locale.ROOT); }

    private static boolean equalsIgnorandoMayusculas(String primero, String segundo) {
        if (primero == null) {
            return segundo == null;
        }
        return segundo != null && primero.equalsIgnoreCase(segundo);
    }

    public RegistroSimboloTp4 registrarConstanteNegativa(String lexemaPositivo, int linea) {
        if (lexemaPositivo == null) {
            return null;
        }
        RegistroSimboloTp4 original = obtenerConstante(lexemaPositivo);
        if (original == null || !"DOUBLEF".equalsIgnoreCase(original.getTipo())) {
            return null;
        }
        String lexemaNegativo = "-" + lexemaPositivo;
        RegistroSimboloTp4 existente = obtenerConstante(lexemaNegativo, "DOUBLEF");
        if (existente != null) {
            return existente;
        }
        try {
            String expJava = lexemaNegativo.replace('d', 'e').replace('D', 'e');
            BigDecimal valor = new BigDecimal(expJava);
            boolean valido = (valor.compareTo(BigDecimal.ZERO) == 0)
                    || (valor.compareTo(MIN_NEG_DOUBLE) > 0 && valor.compareTo(MAX_NEG_DOUBLE) < 0);
            if (!valido) {
                return null;
            }
            return agregarConstante(lexemaNegativo, "DOUBLEF", linea);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
