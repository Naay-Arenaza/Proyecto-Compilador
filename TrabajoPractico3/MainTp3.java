
package TrabajoPractico3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import TrabajoPractico3.ArbolSintactico.NodoAST;
import TrabajoPractico3.ArbolSintactico.ImpresorArbol;


public class MainTp3 {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: ./ejecutarTp3.sh <archivo.txt>");
            return;
        }

        String rutaArchivo = args[0];

        // Un único recolector para todo el análisis.
        RecolectorErroresTp3 recolector = new RecolectorErroresTp3();

        LecturaCodigoTp3 lector = null;
        AnalizadorLexicoTp3 lexer = null;
        Parser parser = null;

        int resultado = -1;

        System.out.println("========================================");
        System.out.println("          ANÁLISIS DEL TP3");
        System.out.println("========================================");

        try {
            // Compartimos el mismo recolector.
            lector = new LecturaCodigoTp3(rutaArchivo, recolector );
            lexer = new AnalizadorLexicoTp3(lector, recolector);
            parser = new Parser(lexer);
            resultado = parser.yyparse();// Ejecutamos el análisis sintáctico.
        } catch (IOException e) {
            recolector.agregarError( "Lectura",1,"No se pudo abrir el archivo '" +rutaArchivo + "': " + e.getMessage());
        } finally {
            if (lector != null) {
                lector.cerrar();
            }
        }

        // Mostrar tabla de símbolos.
        if (lexer != null) {
            System.out.println();
            System.out.println("--- TABLA DE SÍMBOLOS ---");
            lexer.getTablaSimbolos().imprimirTabla();
        }

        // Mostrar información del parser y generar código si corresponde.
        if (parser != null) {
            System.out.println();
            System.out.println("Resultado de yyparse: " + resultado);
            NodoAST raiz = parser.getRaizAST();

            if (raiz != null) {

                // Mostrar el árbol sintáctico.
                ImpresorArbol.imprimir(raiz);

                NodoAST.setTablaSimbolos(lexer.getTablaSimbolos());
                NodoAST.setRecolectorErrores(recolector);
                // No generar código si el análisis tiene errores.
                
                // Ejecutamos la generación para validar los identificadores
                // del AST, pero guardamos temporalmente el código generado.
                ByteArrayOutputStream bufferCodigo = new ByteArrayOutputStream();

                PrintStream salidaOriginal = System.out;

                try (PrintStream salidaTemporal = new PrintStream(
                        bufferCodigo, true, StandardCharsets.UTF_8)) {

                    System.setOut(salidaTemporal);

                    raiz.generarCodigo();

                } finally {
                    System.setOut(salidaOriginal);
                }

                // Solamente mostramos el código si no hay errores.
                if (!recolector.hayErrores() && resultado == 0) {

                    System.out.println();
                    System.out.println("--- CÓDIGO GENERADO ---");

                    System.out.print(
                        bufferCodigo.toString(StandardCharsets.UTF_8)
                    );

                } else {

                    System.out.println(
                        "No se genera código porque el análisis contiene errores."
                    );
                }

            }
        }
        // Mostrar todos los errores juntos al final.
        recolector.imprimirErrores(System.out);
        System.out.println("Cantidad de errores registrados: "+ recolector.cantidadErrores());
        System.out.println();
        if (recolector.hayErrores() || resultado != 0) {
            System.out.println("ANÁLISIS FINALIZADO CON ERRORES.");
        } else {
            System.out.println("ANÁLISIS FINALIZADO CORRECTAMENTE.");
        }
    }
}
