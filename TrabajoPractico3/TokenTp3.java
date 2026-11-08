package TrabajoPractico3;
public class TokenTp3 {
    public static final int EOF = 0;  

    private int id;
    private String atributo;

    public TokenTp3(int id, String atributo) {
        this.id = id;
        this.atributo = atributo;
    }

    public TokenTp3(int id) {
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