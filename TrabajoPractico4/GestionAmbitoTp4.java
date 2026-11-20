package TrabajoPractico4;
import java.util.ArrayDeque;
import java.util.Deque;

public final class GestionAmbitoTp4 {
    private enum TipoAmbito { GLOBAL, CLASE, FUNCION }
    private final TablaDeSimbolosTp4 tabla;
    private final Deque<String> pilaAmbitos = new ArrayDeque<>(); 
    private final Deque<TipoAmbito> pilaTipos = new ArrayDeque<>(); 
    private int nivelesSupresion = 0;

    public GestionAmbitoTp4 (TablaDeSimbolosTp4 tabla) {
        if (tabla == null) {
            throw new IllegalArgumentException("La tabla de símbolos es obligatoria.");
        }
        this.tabla = tabla;
        iniciarPrograma();
    }

    public void iniciarSupresion() { this.nivelesSupresion++; }

    public void finalizarSupresion() {
        if (this.nivelesSupresion > 0) {
            this.nivelesSupresion--;
        }
    }

    public boolean estaSuprimido() { return this.nivelesSupresion > 0; }

    public final void iniciarPrograma() {
        pilaAmbitos.clear();
        pilaTipos.clear();
        pilaAmbitos.push(TablaDeSimbolosTp4.AMBITO_GLOBAL);
        pilaTipos.push(TipoAmbito.GLOBAL);
        this.nivelesSupresion = 0;
        sincronizarTabla();
    }

    public String entrarAmbitoClase(String nombreClase) { return entrarAmbito(nombreClase, TipoAmbito.CLASE); }

    public String entrarAmbitoFuncion(String nombreFuncion) {  return entrarAmbito(nombreFuncion, TipoAmbito.FUNCION);}

    private String entrarAmbito(String nombre, TipoAmbito tipo) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del nuevo ámbito no puede ser vacío.");
        }
        String nuevoAmbito = getAmbitoActual() + ":" + nombre;
        pilaAmbitos.push(nuevoAmbito);
        pilaTipos.push(tipo);
        sincronizarTabla();
        return getAmbitoActual();
    }

    public String salirAmbito() {
        if (pilaAmbitos.size() <= 1) {
            throw new IllegalStateException("No se puede salir del ámbito global main.");
        }
        pilaAmbitos.pop();
        pilaTipos.pop();
        sincronizarTabla();
        return getAmbitoActual();
    }

    public String getAmbitoActual() { 
        if (pilaAmbitos.isEmpty()) {
            return TablaDeSimbolosTp4.AMBITO_GLOBAL;
        }
        return pilaAmbitos.peek();
    }

    public boolean estaEnAmbitoClase() { return pilaTipos.peek() == TipoAmbito.CLASE; }

    public RegistroSimboloTp4 declararVariable(String nombre,String tipo,int linea,boolean comptime) { 
        if (estaSuprimido()) {
            String uso = estaEnAmbitoClase() ? TablaDeSimbolosTp4.USO_ATRIBUTO : (comptime ? TablaDeSimbolosTp4.USO_COMPTIME : TablaDeSimbolosTp4.USO_VARIABLE);
            return registroFicticio(nombre, tipo, uso);
        }

        if (estaEnAmbitoClase()) { 
            RegistroSimboloTp4 atributo = tabla.declararAtributoClase(nombre,tipo,linea);
            if (atributo != null && comptime) { 
                atributo.agregarAtributo("COMPTIME", Boolean.TRUE);
            }
            return atributo;
        }
        return tabla.declararVariable(nombre, tipo,linea,comptime );
    }

    public RegistroSimboloTp4 declararFuncion( String nombre, String tipoRetorno, int linea) {
        if (estaSuprimido()) {
            return registroFicticio(nombre, tipoRetorno, TablaDeSimbolosTp4.USO_FUNCION);
        }
        return tabla.declararFuncion( nombre, tipoRetorno, linea);
    }

    public RegistroSimboloTp4 declararClase( String nombre,int linea) {
        if (estaSuprimido()) {
            return registroFicticio(nombre, "CLASS", TablaDeSimbolosTp4.USO_CLASE);
        }
        return tabla.declararClase( nombre, linea);
    }

    public RegistroSimboloTp4 declararMetodo( String nombre,String tipoRetorno, int linea) {
    if (estaSuprimido()) {
        return registroFicticio(nombre, tipoRetorno, TablaDeSimbolosTp4.USO_METODO);
    }
        return tabla.declararMetodoClase(nombre,tipoRetorno,linea);
    }

    public RegistroSimboloTp4 declararParametro(String nombre,String tipo,int linea) {
    if (estaSuprimido()) {
        return registroFicticio(nombre, tipo, TablaDeSimbolosTp4.USO_PARAMETRO);
    }
        return tabla.declararParametro(nombre, tipo,linea);
    }

    public RegistroSimboloTp4 declararObjeto(String nombre,String nombreClase, int linea) {
        if (estaSuprimido()) {
            return registroFicticio(nombre, nombreClase, TablaDeSimbolosTp4.USO_OBJETO);
        }
        return tabla.declararObjeto(nombre,nombreClase,linea);
    }

    public boolean agregarParametroFormal(String nombreFuncion,String nombreParametro,String tipoParametro,int lineaParametro, String ambitoDeclaracion) {
        if (estaSuprimido()) {
            return true;     
        }    
        return tabla.agregarParametroFormal(nombreFuncion,nombreParametro,tipoParametro,lineaParametro,ambitoDeclaracion);
    }

    public boolean agregarClaseBase(String clase,String claseBase) {
        if (estaSuprimido()) {
            return true;        
        }
        return tabla.agregarClaseBase(clase, claseBase);
    }

    private void sincronizarTabla() {
        tabla.setAmbitoActual(getAmbitoActual());
    }

    private RegistroSimboloTp4 registroFicticio(String nombre, String tipo, String uso) {
        return new RegistroSimboloTp4(nombre, "IDENTIFICADOR", tipo, uso, getAmbitoActual(), -1, -1L);
    }
}
