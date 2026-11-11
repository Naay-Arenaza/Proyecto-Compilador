package TrabajoPractico3.ArbolSintactico;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodoBloque extends NodoAST {
    private List<NodoAST> sentencias;

    public NodoBloque() {
        super("BLOQUE");
        this.sentencias = new ArrayList<>();
    }

    public NodoBloque(List<NodoAST> sentencias) {
        super("BLOQUE");
        this.sentencias = new ArrayList<>(sentencias);
    }

    public void agregarSentencia(NodoAST sentencia) {
        sentencias.add(sentencia);
    }

    public List<NodoAST> getSentencias() {
        return Collections.unmodifiableList(sentencias);
    }

    @Override
    public String generarCodigo() {
        for (NodoAST s : sentencias) {
            if (s != null) {
                s.generarCodigo();
            }
        }
        return null;
    }
}