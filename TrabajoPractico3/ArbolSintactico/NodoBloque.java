import java.util.ArrayList;
import java.util.List;

public class NodoBloque extends NodoAST {
    private List<NodoAST> sentencias;

    public NodoBloque() {
        super("BLOQUE");
        this.sentencias = new ArrayList<>();
    }

    public NodoBloque(List<NodoAST> sentencias) {
        super("BLOQUE");
        this.sentencias = sentencias;
    }

    public void agregarSentencia(NodoAST sentencia) {
        sentencias.add(sentencia);
    }

    @Override
    public String generarCodigo(/*TablaSimbolosTP3 tabla*/) {
        //tabla.abrirScope(); // Abre un ambito nuevo en la tabla de simbolos para este bloque local
        for (NodoAST s : sentencias) {
            if (s != null) {
                //s.generarCodigo(tabla);
            }
        }
        //tabla.cerrarScope(); // Salida del bloque local (Esta bueno)
        return null;
    }
}