package TrabajoPractico3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecolectorErroresTp3 {

    private final List<ErrorTp3> errores = new ArrayList<>();

    public void agregarError(String etapa,int linea,String descripcion) {
        errores.add( new ErrorTp3(etapa, linea, descripcion));
    }

    public List<ErrorTp3> getErrores() {
        return Collections.unmodifiableList(errores);
    }

    public boolean hayErrores() {
        return !errores.isEmpty();
    }
}

