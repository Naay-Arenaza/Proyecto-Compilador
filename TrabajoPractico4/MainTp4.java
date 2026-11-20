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
            recolector.agregarError( "Lectura", 1, "No se pudo abrir el archivo '" + rutaArchivo + "': " + e.getMessage() );
        } catch (RuntimeException e) {
            recolector.agregarError( "Analisis", 1,"Error inesperado durante el análisis: "  + e.getMessage());
        } finally {
            if (lector != null) {
                lector.cerrar();
            }
        }
        NodoAST raiz = null;
        if (parser != null) {
            raiz = parser.getRaizAST();
        }
        String codigoWasm = null;

        if (raiz != null) {
            if (lexer != null) {
                NodoAST.setTablaSimbolos( lexer.getTablaSimbolos());
            }
            NodoAST.setRecolectorErrores( recolector );
            generarCodigoIntermedio(raiz, recolector);
            if (!recolector.hayErrores() && resultado == 0) {
                try {
                    codigoWasm = raiz.generarWasm();
                } catch (RuntimeException e) {
                    recolector.agregarError(  "Generacion WebAssembly",1,   "No se pudo generar el código WebAssembly: " + e.getMessage() );
                    codigoWasm = null;
                }
            }
        }
        imprimirErrores(recolector);
        if (raiz != null) {
            ImpresorArbol.imprimir(raiz);
        } else {
            System.out.println( "No se pudo generar el árbol sintáctico." );
        }

        imprimirTablaSimbolos(lexer);
        if (codigoWasm != null  && !codigoWasm.isBlank()  && !recolector.hayErrores() && resultado == 0) {
            String rutaSalidaWat = construirRutaSalidaWat(rutaArchivo);
            guardarArchivoWat( rutaSalidaWat,  codigoWasm );
        } else {
            System.out.println();
            System.out.println( "No se generó el archivo WebAssembly (.wat).");
        }
        imprimirResultadoFinal(recolector, resultado );
    }

    private static void generarCodigoIntermedio( NodoAST raiz, RecolectorErroresTp4 recolector) {
        ByteArrayOutputStream validarArbol = new ByteArrayOutputStream();
        PrintStream salidaOriginal = System.out;
        try (
            PrintStream salidaTemporal =  new PrintStream( validarArbol, true,  StandardCharsets.UTF_8)
        ) {
            System.setOut(salidaTemporal);
            raiz.generarCodigo();
        } catch (RuntimeException e) {
            recolector.agregarError(  "Generacion intermedia",1, "Error durante la generación de la " + "representación intermedia: " + e.getMessage());
        } finally {
            System.setOut(salidaOriginal);
        }
    }

    private static String construirRutaSalidaWat(String rutaEntrada) {
        Path archivo = Paths.get(rutaEntrada);
        String nombreArchivo = archivo.getFileName().toString();
        int punto = nombreArchivo.lastIndexOf('.');
        if (punto > 0) {
            nombreArchivo = nombreArchivo.substring(0, punto);
        }
        Path directorio =  archivo.getParent();
        if (directorio == null) {
            return nombreArchivo + ".wat";
        }
        return directorio.resolve(nombreArchivo + ".wat").toString();
    }

    private static void guardarArchivoWat( String rutaSalida, String contenidoWat) {
        try {
            Path path = Paths.get(rutaSalida);
            Files.writeString( path,  contenidoWat, StandardCharsets.UTF_8);
            System.out.println();
            System.out.println(  "------------------------------------------------------------");
            System.out.println( "  Archivo WebAssembly generado correctamente:");
            System.out.println( path.toAbsolutePath());
            System.out.println( "------------------------------------------------------------" );
        } catch (IOException e) {
            System.err.println();
            System.err.println("Error al escribir el archivo WebAssembly '" + rutaSalida + "': " + e.getMessage() );
        }
    }

    private static void imprimirTablaSimbolos(AnalizadorLexicoTp4 lexer) {
        if (lexer != null) {
            lexer.getTablaSimbolos().imprimirTabla();
        } else {
            System.out.println("No se dispone de la tabla de símbolos.");
        }
    }

    private static void imprimirErrores( RecolectorErroresTp4 recolector) {
        recolector.imprimirErrores(System.out);
    }

    private static void imprimirResultadoFinal( RecolectorErroresTp4 recolector, int resultado) {
        System.out.println();
        System.out.println( "------------------------------------------------------------");
        if (recolector.hayErrores() || resultado != 0) {
            System.out.println( "        Compilación finalizada con errores." );
        } else {
            System.out.println(  "        Compilación finalizada correctamente." );
        }
        System.out.println(  "------------------------------------------------------------");
    }
}