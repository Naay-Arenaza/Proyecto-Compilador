public class NodoRepeat extends NodoAST {
    private NodoAST cuerpo;
    private NodoAST condicion;

    public NodoRepeat(NodoAST cuerpo, NodoAST condicion) {
        super("REPEAT");
        this.cuerpo = cuerpo;
        this.condicion = condicion;
    }

    public NodoAST getCuerpo() { return cuerpo; }
    public NodoAST getCondicion() { return condicion; }

    @Override
    public String generarCodigo() {
        String lInicio = GeneradorTAC.nuevaEtiqueta();

        // 1. Etiqueta de inicio de iteración
        System.out.println(lInicio + ":");

        // 2. Generar el código del cuerpo
        if (cuerpo != null) {
            cuerpo.generarCodigo();
        }

        // 3. Evaluar condición y saltar de nuevo al inicio si es verdadera
        if (condicion != null) {
            String condRes = condicion.generarCodigo();
            System.out.println("IF " + condRes + " GOTO " + lInicio);
        }

        return null;
    }
}