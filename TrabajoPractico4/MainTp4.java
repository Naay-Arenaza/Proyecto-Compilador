package TrabajoPractico4;
import TrabajoPractico4.ArbolSintactico.NodoAST;
import TrabajoPractico4.ArbolSintactico.ImpresorArbol;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MainTp4 {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: ./ejecutarTp4.sh <archivo.txt>");
            return;
        }

        String rutaArchivo = args[0];
        RecolectorErroresTp4 recolector = new RecolectorErroresTp4();
        LecturaCodigoTp4 lector = null;
        AnalizadorLexicoTp4 lexer = null;
        Parser parser = null;
        int resultado = -1;

        try {
            lector = new LecturaCodigoTp4(rutaArchivo, recolector);
            lexer = new AnalizadorLexicoTp4(lector, recolector);
            parser = new Parser(lexer);
            resultado = parser.yyparse();
        } catch (IOException e) {
            recolector.agregarError("Lectura", 1, "No se pudo abrir el archivo '" + rutaArchivo + "': " + e.getMessage());
        } finally {
            if (lector != null) {
                lector.cerrar();
            }
        }

        String codigoIntermedio = null;
        String codigoWasm = null;
        NodoAST raiz = (parser != null) ? parser.getRaizAST() : null;

        if (raiz != null) {
            if (lexer != null) {
                NodoAST.setTablaSimbolos(lexer.getTablaSimbolos());
            }
            NodoAST.setRecolectorErrores(recolector);

            // 1. Generar código intermedio y ejecutar validaciones semánticas del AST
            codigoIntermedio = generarCodigoIntermedio(raiz);

            // 2. Generar código WebAssembly (.wat) si no hubo errores
            if (!recolector.hayErrores() && resultado == 0) {
                codigoWasm = raiz.generarWasm();
            }
        }

        // --- SALIDAS DEL COMPILADOR (Orden exigido en TP4) ---

        // 1) Errores (Léxicos, Sintácticos y Semánticos)
        imprimirErrores(recolector);

        // 2) Representación intermedia del Grupo 8: Árbol Sintáctico
        if (raiz != null) {
            ImpresorArbol.imprimir(raiz);
        } else {
            System.out.println("\nNo se pudo generar el árbol sintáctico.");
        }

        // 3) Tabla de Símbolos
        imprimirTablaSimbolos(lexer);

        // 4) Archivo WebAssembly (.wat)
        if (codigoWasm != null && !codigoWasm.isBlank() && !recolector.hayErrores() && resultado == 0) {
            String rutaSalidaWat = construirRutaSalidaWat(rutaArchivo);
            guardarArchivoWat(rutaSalidaWat, codigoWasm);
        } else {
            System.out.println("\nNo se generó el archivo WebAssembly (.wat).");
        }

        imprimirResultadoFinal(recolector, resultado);
    }

    private static String construirRutaSalidaWat(String rutaEntrada) {
        int punto = rutaEntrada.lastIndexOf('.');
        if (punto > 0) {
            return rutaEntrada.substring(0, punto) + ".wat";
        }
        return rutaEntrada + ".wat";
    }

    private static void guardarArchivoWat(String rutaSalida, String contenidoWat) {
        try {
            Path path = Paths.get(rutaSalida);
            Files.writeString(path, contenidoWat, StandardCharsets.UTF_8);
            System.out.println();
            System.out.println("------------------------------------------------------------");
            System.out.println("  Archivo WebAssembly generado: " + path.toAbsolutePath());
            System.out.println("------------------------------------------------------------");
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo WebAssembly '" + rutaSalida + "': " + e.getMessage());
        }
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

    private static void imprimirTablaSimbolos(AnalizadorLexicoTp4 lexer) {
        if (lexer != null) {
            lexer.getTablaSimbolos().imprimirTabla();
        }
    }

    private static void imprimirCodigoIntermedio(String codigo) {
        System.out.println();
        System.out.println("------------------------------------------------------------");
        System.out.println("                    Código intermedio");
        System.out.println("------------------------------------------------------------");
        System.out.println();
        System.out.print(codigo);
    }

    private static void imprimirErrores(RecolectorErroresTp4 recolector) {
        recolector.imprimirErrores(System.out);
    }

    private static void imprimirResultadoFinal(RecolectorErroresTp4 recolector, int resultado) {
        if (recolector.hayErrores() || resultado != 0) {
            System.out.println("Análisis finalizado con errores.");
        } else {
            System.out.println("Análisis finalizado correctamente.");
        }
    }
}