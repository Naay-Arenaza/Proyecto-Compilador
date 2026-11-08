import java.io.IOException;

public class MainTp1 {
    public static void main(String[] args) throws IOException {
        String rutaArchivo = "TxtPrueba.txt";
        LecturaCodigoTp1 lector = new LecturaCodigoTp1(rutaArchivo);
        AnalizadorLexicoTp1 analizador = new AnalizadorLexicoTp1(lector);

        System.out.println("--- Tokens Detectados ---");
        
        TokenTp1 token = analizador.getToken();

        while (token != null && token.getId() != 0) {
            System.out.println(obtenerDescripcionToken(token));
            token = analizador.getToken();
        }

        analizador.getTablaSimbolos().imprimirTabla();
    }

    private static String obtenerDescripcionToken(TokenTp1 token) {
            int id = token.getId();
            String lex = token.getAtributo();

            if (id == TokenTp1.ID) {
                return "Identificador " + lex;
            } else if (id == TokenTp1.CTE) { 
                return "Constante " + lex;
            } else if (id == TokenTp1.CADENA) {
                return "Cadena " + lex;
            } 

            else if (id >= 100 && id <= 110) { 
                return "Palabra reservada " + (lex != null ? lex : "");
            }
                    
            else if (id == TokenTp1.MAYOR_IGUAL) {
                return "Operador compuesto >=";
            } else if (id == TokenTp1.MENOR_IGUAL) {
                return "Operador compuesto <=";
            } else if (id == TokenTp1.IGUAL_IGUAL) {
                return "Operador compuesto ==";
            } else if (id == TokenTp1.DISTINTO) {
                return "Operador compuesto !=";
            } else if (id == TokenTp1.ASIGNACION) {
                return "Operador de asignación :=";
            } 

            else {
                return lex != null && !lex.isEmpty() ? lex : String.valueOf((char) id);
            }
    }
}