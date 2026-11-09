package TrabajoPractico3.ArbolSintactico;
public class NodoIF extends NodoAST {
    private NodoAST condicion;
    private NodoAST cuerpoThen;
    private NodoAST cuerpoElse;

    public NodoIF(NodoAST condicion, NodoAST cuerpoThen, NodoAST cuerpoElse) {
        super("IF");
        this.condicion = condicion;
        this.cuerpoThen = cuerpoThen;
        this.cuerpoElse = cuerpoElse;
    }

    public NodoAST getCondicion() { return condicion; }
    public NodoAST getCuerpoThen() { return cuerpoThen; }
    public NodoAST getCuerpoElse() { return cuerpoElse; }

    @Override
    public String generarCodigo() {
        String lElse = GeneradorTAC.nuevaEtiqueta();
        String lFin = GeneradorTAC.nuevaEtiqueta();

        // 1. Generar TAC de la condición
        String condRes = condicion.generarCodigo();

        // 2. Salto condicional si es falso
        System.out.println("IF_FALSE " + condRes + " GOTO " + (cuerpoElse != null ? lElse : lFin));

        // 3. Generar cuerpo THEN
        if (cuerpoThen != null) {
            cuerpoThen.generarCodigo();
        }

        // 4. Si hay ELSE, saltar al fin tras el THEN y poner la etiqueta ELSE
        if (cuerpoElse != null) {
            System.out.println("GOTO " + lFin);
            System.out.println(lElse + ":");
            cuerpoElse.generarCodigo();
        }

        // 5. Etiqueta final
        System.out.println(lFin + ":");
    return null;
}
}