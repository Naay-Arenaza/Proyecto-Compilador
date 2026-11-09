import java.util.List;

public class NodoBloque extends NodoAST {
    private List<NodoAST> sentencias;

    public NodoBloque(List<NodoAST> sentencias) {
        super("BLOQUE");
        this.sentencias = sentencias;
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