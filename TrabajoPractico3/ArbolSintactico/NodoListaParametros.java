package TrabajoPractico3.ArbolSintactico;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodoListaParametros extends NodoAST {
    private final List<NodoAST> parametros;

    public NodoListaParametros() {
        super("LISTA_PARAMETROS");
        this.parametros = new ArrayList<>();
    }

    public void agregarParametro(NodoAST parametro) {
        if (parametro != null) {
            parametros.add(parametro);
        }
    }

    public List<NodoAST> getParametros() { return Collections.unmodifiableList(parametros); }

    @Override
    public String generarCodigo() {  return null; }
}

