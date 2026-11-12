package TrabajoPractico3;
import java.util.ArrayDeque;
import java.util.Deque;

public final class GestionAmbitoTp3 {
    private enum TipoAmbito { // Tipos de ámbitos que puede tener el programa.
        GLOBAL,
        CLASE,
        FUNCION
    }
    private final TablaDeSimbolosTp3 tabla; //Tabla única de símbolos compartida por el compilador.
    private final Deque<String> pilaAmbitos = new ArrayDeque<>(); // Pila de nombres completos de los ámbitos.
    private final Deque<TipoAmbito> pilaTipos = new ArrayDeque<>(); // Pila que indica el tipo de cada ámbito.
    private int nivelesSupresion = 0;

    public GestionAmbitoTp3(TablaDeSimbolosTp3 tabla) {
        if (tabla == null) {
            throw new IllegalArgumentException("La tabla de símbolos es obligatoria.");
        }
        this.tabla = tabla;
        iniciarPrograma();
    }

    public void iniciarSupresion() {
        this.nivelesSupresion++;
    }

    public void finalizarSupresion() {
        if (this.nivelesSupresion > 0) {
            this.nivelesSupresion--;
        }
    }

    public boolean estaSuprimido() {
        return this.nivelesSupresion > 0;
    }

    public final void iniciarPrograma() {// Inicializa el ámbito global del programa.
        pilaAmbitos.clear();
        pilaTipos.clear();
        pilaAmbitos.push(TablaDeSimbolosTp3.AMBITO_GLOBAL);
        pilaTipos.push(TipoAmbito.GLOBAL);
        this.nivelesSupresion = 0; //??
        sincronizarTabla();
    }

    public RegistroSimboloTp3 declararPrograma(String nombre, int linea) {// Registra el nombre del programa en el ámbito global.
        return tabla.declararPrograma(nombre, linea);
    }

    public String entrarAmbitoClase(String nombreClase) {// Ingresa al ámbito de una clase.
        return entrarAmbito(nombreClase, TipoAmbito.CLASE);
    }

    public String entrarAmbitoFuncion(String nombreFuncion) {// Ingresa al ámbito de una función o método.
        return entrarAmbito(nombreFuncion, TipoAmbito.FUNCION);
    }

    private String entrarAmbito(String nombre, TipoAmbito tipo) {// Agrega un nuevo ámbito

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del nuevo ámbito no puede ser vacío.");
        }
        String nuevoAmbito = getAmbitoActual() + ":" + nombre;
        pilaAmbitos.push(nuevoAmbito);
        pilaTipos.push(tipo);
        sincronizarTabla();
        return getAmbitoActual();
    }

    public String salirAmbito() {// Sale del ámbito actual y vuelve al ámbito anterior.
        if (pilaAmbitos.size() <= 1) {
            throw new IllegalStateException("No se puede salir del ámbito global main.");
        }
        pilaAmbitos.pop();
        pilaTipos.pop();
        sincronizarTabla();
        return getAmbitoActual();
    }

    // CONSULTA DE ÁMBITOS
    public String getAmbitoActual() {  // Devuelve el nombre completo del ámbito actual.
        if (pilaAmbitos.isEmpty()) {
            return TablaDeSimbolosTp3.AMBITO_GLOBAL;
        }
        return pilaAmbitos.peek();
    }

    public String getAmbitoPadre() {  // Devuelve el ámbito padre del ámbito actual.
        String ambitoActual = getAmbitoActual();
        int posicionSeparador = ambitoActual.lastIndexOf(':');
        if (posicionSeparador == -1) {
            return null;
        }
        return ambitoActual.substring(0, posicionSeparador);
    }

    public boolean esAmbitoGlobal() {  // Comprueba si estamos en el ámbito global.
        return pilaTipos.peek() == TipoAmbito.GLOBAL;
    }

    public boolean estaEnAmbitoClase() {    // Comprueba si estamos dentro de una clase.
        return pilaTipos.peek() == TipoAmbito.CLASE;
    }

    public boolean estaEnAmbitoFuncion() {    // Comprueba si estamos dentro de una función o método.
        return pilaTipos.peek() == TipoAmbito.FUNCION;
    }

    // DECLARACIÓN DE SÍMBOLOS
    public RegistroSimboloTp3 declararVariable(String nombre,String tipo,int linea,boolean comptime) {   // Declara una variable o un atributo de clase.
        if (estaSuprimido()) {
            String uso = estaEnAmbitoClase() ? TablaDeSimbolosTp3.USO_ATRIBUTO : (comptime ? TablaDeSimbolosTp3.USO_COMPTIME : TablaDeSimbolosTp3.USO_VARIABLE);
        return registroFicticio(nombre, tipo, uso);
        }

        if (estaEnAmbitoClase()) {  // Si estamos dentro de una clase, declaramos un atributo.
            RegistroSimboloTp3 atributo = tabla.declararAtributoClase(nombre,tipo,linea);
            if (atributo != null && comptime) {  // Conserva la marca comptime si corresponde.
                atributo.agregarAtributo("COMPTIME", Boolean.TRUE);
            }
            return atributo;
        }
        return tabla.declararVariable(nombre, tipo,linea,comptime );// En los demás ámbitos declaramos una variable.
    }

    // Registra una función o un método, dependiendo del ámbito actual.
    public RegistroSimboloTp3 declararFuncion( String nombre, String tipoRetorno, int linea) {
        if (estaSuprimido()) {
            return registroFicticio(nombre, tipoRetorno, TablaDeSimbolosTp3.USO_FUNCION);
        }
        return tabla.declararFuncion( nombre, tipoRetorno, linea);
    }

    public RegistroSimboloTp3 declararClase( String nombre,int linea) {// Registra una clase.
        if (estaSuprimido()) {
            return registroFicticio(nombre, "CLASS", TablaDeSimbolosTp3.USO_CLASE);
        }
        return tabla.declararClase( nombre, linea);
    }

    public RegistroSimboloTp3 declararMetodo( String nombre,String tipoRetorno, int linea) {// Registra un método dentro de una clase.
    if (estaSuprimido()) {
        return registroFicticio(nombre, tipoRetorno, TablaDeSimbolosTp3.USO_METODO);
    }
        return tabla.declararMetodoClase(nombre,tipoRetorno,linea);
    }

    public RegistroSimboloTp3 declararParametro(String nombre,String tipo,int linea) {// Registra un parámetro formal.
    if (estaSuprimido()) {
        return registroFicticio(nombre, tipo, TablaDeSimbolosTp3.USO_PARAMETRO);
    }
        return tabla.declararParametro(nombre, tipo,linea);
    }

    public RegistroSimboloTp3 declararObjeto(String nombre,String nombreClase, int linea) {// Registra una instancia de una clase.
        if (estaSuprimido()) {
            return registroFicticio(nombre, nombreClase, TablaDeSimbolosTp3.USO_OBJETO);
        }
        return tabla.declararObjeto(nombre,nombreClase,linea);
    }

    public boolean agregarParametroFormal(String nombreFuncion,String nombreParametro,String tipoParametro,int lineaParametro, String ambitoDeclaracion) {
        if (estaSuprimido()) {
            return true;          // no toca la firma de la función existente
        }    
        // Agrega un parámetro a la firma de una función o método.
        return tabla.agregarParametroFormal(nombreFuncion,nombreParametro,tipoParametro,lineaParametro,ambitoDeclaracion);
    }

    public boolean agregarClaseBase(String clase,String claseBase) {// Registra una relación de herencia entre clases.
        if (estaSuprimido()) {
            return true;          // no agrega herencia a la clase existente
        }
        return tabla.agregarClaseBase(clase, claseBase);
    }

    // BÚSQUEDA DE SÍMBOLOS
    public RegistroSimboloTp3 resolver(String nombre) {// Busca un símbolo visible desde el ámbito actual.
        return tabla.buscar( nombre, getAmbitoActual());
    }

    public RegistroSimboloTp3 resolverVariable(String nombre) {// Busca una variable visible.
        return tabla.resolverVariable(nombre);
    }

    public RegistroSimboloTp3 resolverFuncion(String nombre) {// Busca una función o método visible.
        return tabla.resolverFuncion(nombre);
    }

    // SINCRONIZACIÓN CON LA TABLA DE SÍMBOLOS
    private void sincronizarTabla() {// Actualiza el ámbito actual de la tabla de símbolos.
        tabla.setAmbitoActual(getAmbitoActual());
    }

    private RegistroSimboloTp3 registroFicticio(String nombre, String tipo, String uso) {
        return new RegistroSimboloTp3(
            nombre, "IDENTIFICADOR", tipo, uso, getAmbitoActual(), -1, -1L
        );
    }

}
