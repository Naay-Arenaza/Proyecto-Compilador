package TrabajoPractico3;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import TrabajoPractico3.ArbolSintactico.ImpresorArbol;
import TrabajoPractico3.ArbolSintactico.NodoAST;

public class MainTp3 {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: ./ejecutarTp3.sh <archivo.txt>");
            return;
        }
        String rutaArchivo = args[0];
        RecolectorErroresTp3 recolector = new RecolectorErroresTp3();
        LecturaCodigoTp3 lector = null;
        AnalizadorLexicoTp3 lexer = null;
        Parser parser = null;
        int resultado = -1;
        try {
            lector = new LecturaCodigoTp3(rutaArchivo, recolector);
            lexer = new AnalizadorLexicoTp3(lector, recolector);
            parser = new Parser(lexer);
            resultado = parser.yyparse();
        } catch (IOException e) {
            recolector.agregarError( "Lectura", 1,  "No se pudo abrir el archivo '" + rutaArchivo + "': " + e.getMessage() );
        } finally {
            if (lector != null) {
                lector.cerrar();
            }
        }
        imprimirTablaSimbolos(lexer);
        imprimirResultadoSintactico(parser, resultado);
        if (parser != null) {
            NodoAST raiz = parser.getRaizAST();
            if (raiz != null) {
                imprimirArbol(raiz);
                if (lexer != null) {
                    NodoAST.setTablaSimbolos(lexer.getTablaSimbolos());
                }
                NodoAST.setRecolectorErrores(recolector);
                String codigoIntermedio = generarCodigoIntermedio(raiz);
                if (codigoIntermedio != null && !codigoIntermedio.isBlank()) {
                    imprimirCodigoIntermedio(codigoIntermedio);
                } else {
                    System.out.println("No se pudo generar el código intermedio.");
                }
            } else {
                imprimirArbolNoDisponible();
            }
        }
        imprimirErrores(recolector);
        imprimirResultadoFinal(recolector, resultado);
    }


    private static void imprimirArbolNoDisponible() {
        System.out.println();
        System.out.println("--- ÁRBOL SINTÁCTICO ---");
        System.out.println("No hay árbol sintáctico para mostrar.");
    }

    private static String generarCodigoIntermedio(NodoAST raiz) {

        ByteArrayOutputStream bufferCodigo = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;
        try (PrintStream salidaTemporal = new PrintStream(bufferCodigo, true, StandardCharsets.UTF_8)) {
            System.setOut(salidaTemporal);
            raiz.generarCodigo();
        } finally {
            System.setOut(salidaOriginal);
        }
        return bufferCodigo.toString(StandardCharsets.UTF_8);
    }

    private static void imprimirTablaSimbolos(AnalizadorLexicoTp3 lexer) {
        if (lexer == null) {
            return;
        }
        lexer.getTablaSimbolos().imprimirTabla();
    }

    private static void imprimirResultadoSintactico(Parser parser, int resultado) {
        if (parser == null || resultado != 0) {
            return;
        }
    }

    private static void imprimirArbol(NodoAST raiz) {
        ImpresorArbol.imprimir(raiz);
    }

    private static void imprimirCodigoIntermedio(String codigo) {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                    Código intermedio");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.print(codigo);

    }

    private static void imprimirErrores(RecolectorErroresTp3 recolector) {
        recolector.imprimirErrores(System.out);
    }

    private static void imprimirResultadoFinal(RecolectorErroresTp3 recolector,int resultado) {
        if (recolector.hayErrores() || resultado != 0) {
            System.out.println("Análisis finalizado con errores.");
        } else {
            System.out.println("Análisis finalizado correctamente.");
        }
    }
}

