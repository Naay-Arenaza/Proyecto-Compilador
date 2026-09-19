
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        
        String rutaArchivo = "TxtPrueba.txt";

        System.out.println("======================================");
        System.out.println("       PRUEBA LÉXICO + PARSER");
        System.out.println("======================================");

        // =====================================================
        // 1. CREAR LECTOR
        // =====================================================

        LecturaCodigo lector = new LecturaCodigo(rutaArchivo);

        // =====================================================
        // 2. CREAR ANALIZADOR LÉXICO
        // =====================================================

        AnalizadorLexico lexer = new AnalizadorLexico(lector);

        // =====================================================
        // 3. MOSTRAR TOKENS
        // =====================================================

        System.out.println();
        System.out.println("--- TOKENS RECONOCIDOS ---");

        int token;

        do {
            token = lexer.yylex();

            System.out.println(
                "Token: " + token +
                " | Valor: " + lexer.yylval
            );

        } while (token != 0);

        // =====================================================
        // 4. TABLA DE SÍMBOLOS
        // =====================================================

        System.out.println();
        System.out.println("--- TABLA DE SÍMBOLOS ---");

        lexer.getTablaSimbolos().imprimirTabla();

        // =====================================================
        // 5. VOLVER A CREAR EL LECTOR Y LEXER
        //    PARA QUE EL PARSER RECIBA EL ARCHIVO DESDE EL INICIO
        // =====================================================

        System.out.println();
        System.out.println("--- INICIANDO PARSER ---");

        LecturaCodigo lectorParser = new LecturaCodigo(rutaArchivo);
        AnalizadorLexico lexerParser =
                new AnalizadorLexico(lectorParser);

        Parser parser = new Parser(lexerParser);

        int resultado = parser.yyparse();

        // =====================================================
        // 6. RESULTADO DEL PARSER
        // =====================================================

        System.out.println();
        System.out.println("======================================");

        if (resultado == 0) {
            System.out.println("ANÁLISIS SINTÁCTICO CORRECTO");
        } else {
            System.out.println("ANÁLISIS SINTÁCTICO CON ERRORES");
        }

        System.out.println("======================================");

        // =====================================================
        // 7. TABLA DE SÍMBOLOS DEL ANÁLISIS COMPLETO
        // =====================================================

        System.out.println();
        System.out.println("--- TABLA DE SÍMBOLOS DEL PARSER ---");

        lexerParser.getTablaSimbolos().imprimirTabla();
    }
}