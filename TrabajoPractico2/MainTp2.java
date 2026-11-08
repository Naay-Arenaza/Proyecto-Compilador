package TrabajoPractico2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MainTp2 {

    public static void main(String[] args) throws IOException {

        if (args.length == 0) {
            System.out.println("Se requiere al menos un argumento para ejecutar el programa.");
            return;
        }

        String rutaArchivo = args[0];

        // Listas donde se guardan los resultados del análisis.
        List<String> tokensReconocidos = new ArrayList<>();
        List<RegistroSalida> estructurasReconocidas = new ArrayList<>();
        List<RegistroSalida> avisos = new ArrayList<>();
        List<String> erroresLexicos = new ArrayList<>();
        List<String> erroresSintacticos = new ArrayList<>();

        System.out.println("========================================");
        System.out.println("          RESULTADO DEL ANÁLISIS");
        System.out.println("========================================");

        // Se crea un único lector y un único analizador léxico.
        LecturaCodigoTp2 lector = new LecturaCodigoTp2(rutaArchivo);
        LexerConRegistro lexer = new LexerConRegistro(lector, tokensReconocidos);

        Parser parser = new Parser(lexer);

        // Se guardan las salidas originales para restaurarlas después.
        PrintStream salidaOriginal = System.out;
        PrintStream errorOriginal = System.err;

        ByteArrayOutputStream erroresCapturados = new ByteArrayOutputStream();

        // Durante el análisis se capturan las salidas de la gramática.
        CapturadorSalida salidaCapturada =
                new CapturadorSalida(lexer);

        PrintStream erroresCapturadosStream =
                new PrintStream(erroresCapturados);

        int resultado;

        try {
            System.setOut(salidaCapturada);
            System.setErr(erroresCapturadosStream);

            // IMPORTANTE: el parser se ejecuta una sola vez.
            resultado = parser.yyparse();

        } finally {
            System.setOut(salidaOriginal);
            System.setErr(errorOriginal);

            salidaCapturada.flush();
            erroresCapturadosStream.flush();
        }

        // Se clasifican los mensajes que imprimió la gramática.
        for (RegistroSalida registro : salidaCapturada.getMensajes()) {

            String mensaje = registro.mensaje.trim();

            if (mensaje.isEmpty()) {
                continue;
            }

            if (mensaje.toLowerCase().contains("warning")) {
                avisos.add(registro);
            } else {
                estructurasReconocidas.add(registro);
            }
        }

        // Se separan los errores léxicos de los sintácticos.
        String[] lineasError =
                erroresCapturados.toString().split("\\R");

        for (String error : lineasError) {

            error = error.trim();

            if (error.isEmpty()
                    || error.contains("Final archivo")) {
                continue;
            }

            if (error.contains("Error sintáctico")
                    || error.contains("Error sintactico")
                    || error.contains("syntax error")) {

                erroresSintacticos.add(error);

            } else {
                erroresLexicos.add(error);
            }
        }

        // ========================================
        // TOKENS RECONOCIDOS
        // ========================================

        salidaOriginal.println();
        salidaOriginal.println("--- TOKENS RECONOCIDOS ---");

        if (tokensReconocidos.isEmpty()) {
            salidaOriginal.println("No se reconocieron tokens.");
        } else {
            for (String token : tokensReconocidos) {
                salidaOriginal.println(token);
            }
        }

        // ========================================
        // ESTRUCTURAS RECONOCIDAS
        // ========================================

        salidaOriginal.println();
        salidaOriginal.println("--- ESTRUCTURAS RECONOCIDAS ---");

        if (estructurasReconocidas.isEmpty()) {
            salidaOriginal.println("No se reconocieron estructuras.");
        } else {
            for (RegistroSalida estructura : estructurasReconocidas) {
                salidaOriginal.println(
                        "Línea (posición del lexer): "
                        + estructura.linea
                        + " | "
                        + estructura.mensaje
                );
            }
        }

        // ========================================
        // AVISOS
        // ========================================

        salidaOriginal.println();
        salidaOriginal.println("--- AVISOS ---");

        if (avisos.isEmpty()) {
            salidaOriginal.println("No hay avisos.");
        } else {
            for (RegistroSalida aviso : avisos) {
                salidaOriginal.println(
                        "Línea " + aviso.linea + ": " + aviso.mensaje
                );
            }
        }

        // ========================================
        // ERRORES LÉXICOS
        // ========================================

        salidaOriginal.println();
        salidaOriginal.println("--- ERRORES LÉXICOS ---");

        if (erroresLexicos.isEmpty()) {
            salidaOriginal.println("No se encontraron errores léxicos.");
        } else {
            for (String error : erroresLexicos) {
                salidaOriginal.println(error);
            }
        }

        // ========================================
        // ERRORES SINTÁCTICOS
        // ========================================

        salidaOriginal.println();
        salidaOriginal.println("--- ERRORES SINTÁCTICOS ---");

        if (erroresSintacticos.isEmpty()
                && parser.yynerrs == 0) {

            salidaOriginal.println("No se encontraron errores sintácticos.");

        } else {
            for (String error : erroresSintacticos) {
                salidaOriginal.println(error);
            }
        }

        // ========================================
        // TABLA DE SÍMBOLOS
        // ========================================

        salidaOriginal.println();
        salidaOriginal.println("--- TABLA DE SÍMBOLOS ---");
        salidaOriginal.println();

        lexer.getTablaSimbolos().imprimirTabla();

        // ========================================
        // RESULTADO FINAL
        // ========================================

        boolean hayErrores =
                !erroresLexicos.isEmpty()
                || !erroresSintacticos.isEmpty()
                || parser.yynerrs > 0
                || resultado != 0;

        salidaOriginal.println();
        salidaOriginal.println("========================================");

        if (hayErrores) {
            salidaOriginal.println("ANÁLISIS COMPLETO CON ERRORES");
        } else {
            salidaOriginal.println("ANÁLISIS COMPLETO CORRECTO");
        }

        salidaOriginal.println("========================================");
    }

    // =====================================================
    // REGISTRO DE CADA TOKEN
    // =====================================================

    private static class LexerConRegistro extends AnalizadorLexicoTp2 {

        private final List<String> tokens;

        public LexerConRegistro(
                LecturaCodigoTp2 lector,
                List<String> tokens) {

            super(lector);
            this.tokens = tokens;
        }

        @Override
        public int yylex() {

            int token = super.yylex();

            if (token != 0) {

                String nombre = obtenerNombreToken(token);
                String valor = obtenerValorToken(token);

                tokens.add(
                        "Línea " + getLineaActual()
                        + " | Número de token: " + token
                        + " | Tipo: " + nombre
                        + " | Valor: " + valor
                );
            }

            return token;
        }

private String obtenerNombreToken(int token) {

    switch (token) {

        case Parser.ID:
            return "IDENTIFICADOR";

        case Parser.CTE:
            return "CONSTANTE";

        case Parser.CADENA:
            return "CADENA";

        // Palabras reservadas
        case Parser.DOUBLEF:
        case Parser.USHORTINT:
        case Parser.BEGIN:
        case Parser.END:
        case Parser.FUNCTION:
        case Parser.RET:
        case Parser.CLASS:
        case Parser.IF:
        case Parser.ELSE:
        case Parser.END_IF:
        case Parser.POUT:
        case Parser.REPEAT:
        case Parser.WHILE:
        case Parser.COMPTIME:
        case Parser.EXTENDS:
        case Parser.TODF:
            return "PALABRA_RESERVADA";

        case Parser.ASIGNACION:
            return "ASIGNACION";

        case Parser.IGUAL:
            return "IGUAL";

        case Parser.DISTINTO:
            return "DISTINTO";

        case Parser.MAYORIGUAL:
            return "MAYORIGUAL";

        case Parser.MENORIGUAL:
            return "MENORIGUAL";

        case Parser.SIN_PUNTO_Y_COMA:
            return "SIN_PUNTO_Y_COMA";

        default:
            if (token > 0 && token < 128) {
                return "'" + (char) token + "'";
            }

            return "TOKEN_" + token;
    }
}


private String obtenerValorToken(int token) {

    // Recuperar el lexema real de USHORTINT y DOUBLEF.
    // Estos tokens también representan constantes numéricas.
    if ((token == Parser.USHORTINT || token == Parser.DOUBLEF)
            && yylval != null && yylval.sval != null) {
        return yylval.sval;
    }

    switch (token) {

        // Palabras reservadas
        case Parser.BEGIN: return "BEGIN";
        case Parser.END: return "END";
        case Parser.FUNCTION: return "FUNCTION";
        case Parser.RET: return "RET";
        case Parser.CLASS: return "CLASS";
        case Parser.IF: return "IF";
        case Parser.ELSE: return "ELSE";
        case Parser.END_IF: return "END_IF";
        case Parser.POUT: return "POUT";
        case Parser.REPEAT: return "REPEAT";
        case Parser.WHILE: return "WHILE";
        case Parser.COMPTIME: return "COMPTIME";
        case Parser.EXTENDS: return "EXTENDS";
        case Parser.TODF: return "TODF";

        // Operadores compuestos
        case Parser.ASIGNACION: return ":=";
        case Parser.IGUAL: return "==";
        case Parser.DISTINTO: return "!=";
        case Parser.MAYORIGUAL: return ">=";
        case Parser.MENORIGUAL: return "<=";

        default:

            if (token > 0 && token < 128) {
                return String.valueOf((char) token);
            }

            if (yylval != null && yylval.sval != null) {
                return yylval.sval;
            }

            return obtenerNombreToken(token);
    }
}
}

    // =====================================================
    // CAPTURA DE LOS MENSAJES DE LA GRAMÁTICA
    // =====================================================

    private static class CapturadorSalida extends PrintStream {

        private final AnalizadorLexicoTp2 lexer;
        private final List<RegistroSalida> mensajes =
                new ArrayList<>();

        public CapturadorSalida(AnalizadorLexicoTp2 lexer) {
            super(new ByteArrayOutputStream());
            this.lexer = lexer;
        }

        @Override
        public void println(String mensaje) {

            mensajes.add(
                    new RegistroSalida(
                            mensaje,
                            lexer.getLineaActual()
                    )
            );
        }

        @Override
        public void println(Object mensaje) {
            println(String.valueOf(mensaje));
        }

        public List<RegistroSalida> getMensajes() {
            return mensajes;
        }
    }

    // =====================================================
    // CLASE AUXILIAR PARA GUARDAR MENSAJES Y LÍNEAS
    // =====================================================

    private static class RegistroSalida {

        String mensaje;
        int linea;

        public RegistroSalida(String mensaje, int linea) {
            this.mensaje = mensaje;
            this.linea = linea;
        }
    }
}