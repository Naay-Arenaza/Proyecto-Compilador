
package TrabajoPractico3.ArbolSintactico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Conserva los argumentos reales en el orden en que aparecen
 * en la invocación.
 */
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

    public List<NodoAST> getParametros() {
        return Collections.unmodifiableList(parametros);
    }

    @Override
    public String generarCodigo() {
        /*
         * La invocación controla la evaluación y la copia,
         * porque puede existir un orden explícito como [1,3,2].
         */
        return null;
    }
}

