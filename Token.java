public class Token {
    public static final int EOF = 0;  
    public static final int ID = 27;
    public static final int CTE = 28;
    public static final int CADENA = 29;
    public static final int MAYOR_IGUAL = 80; // '>='
    public static final int MENOR_IGUAL = 81; // '<='
    public static final int IGUAL_IGUAL = 82; // '=='
    public static final int DISTINTO    = 83; // '!='
    public static final int ASIGNACION  = 85; // ':='


    private int id;
    private String atributo;

    public Token(int id, String atributo) {
        this.id = id;
        this.atributo = atributo;
    }

    public Token(int id) {
        this(id, null);
    }

    public int getId() {
        return id;
    }

    public String getAtributo() {
        return atributo;
    }

    @Override
    public String toString() {
        if (atributo != null) {
            return "[" + id + ", '" + atributo + "']";
        }
        return "[" + id + "]";
    }
}