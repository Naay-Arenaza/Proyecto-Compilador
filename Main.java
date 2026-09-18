import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String rutaArchivo = "TxtPrueba.txt";
        LecturaCodigo lector = new LecturaCodigo(rutaArchivo);
        AnalizadorLexico analizador = new AnalizadorLexico(lector);

        System.out.println("--- Tokens Detectados ---");
        
        Token token = analizador.getToken();

        while (token != null && token.getId() != 0) {
            System.out.println(obtenerDescripcionToken(token));
            token = analizador.getToken();
        }

        analizador.getTablaSimbolos().imprimirTabla();
    }

    private static String obtenerDescripcionToken(Token token) {
            int id = token.getId();
            String lex = token.getAtributo();

            if (id == Token.ID) {
                return "Identificador " + lex;
            } else if (id == Token.CTE) { 
                return "Constante " + lex;
            } else if (id == Token.CADENA) {
                return "Cadena " + lex;
            } 

            else if (id >= 59 && id <= 70) { 
                return "Palabra reservada " + (lex != null ? lex : "");
            }
                    
            else if (id == Token.MAYOR_IGUAL) {
                return "Operador compuesto >=";
            } else if (id == Token.MENOR_IGUAL) {
                return "Operador compuesto <=";
            } else if (id == Token.IGUAL_IGUAL) {
                return "Operador compuesto ==";
            } else if (id == Token.DISTINTO) {
                return "Operador compuesto !=";
            } else if (id == Token.ASIGNACION) {
                return "Operador de asignación :=";
            } 

            else {
                return lex != null && !lex.isEmpty() ? lex : String.valueOf((char) id);
            }
    }
}