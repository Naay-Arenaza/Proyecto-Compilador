package TrabajoPractico3.ArbolSintactico;
public class NodoPrograma extends NodoAST {
    private String nombrePrograma;
    private NodoAST bloqueEjecutable;

    public NodoPrograma(String nombrePrograma, NodoAST bloqueEjecutable) {
        super("PROGRAMA");
        this.nombrePrograma = nombrePrograma;
        this.bloqueEjecutable = bloqueEjecutable;
    }

    public String getNombrePrograma() { return nombrePrograma; }
    public NodoAST getBloqueEjecutable() { return bloqueEjecutable; }

    @Override
    public String generarCodigo() {
        System.out.println("; Inicio del Programa " + nombrePrograma);
        if (bloqueEjecutable != null) {
            bloqueEjecutable.generarCodigo();
        }
        System.out.println("; Fin del Programa");
        return null;
    }
}