# Trabajo Práctico 2 — Analizador sintáctico

Esta carpeta contiene la etapa de análisis sintáctico. El parser recibe los tokens del analizador léxico y comprueba que el programa respete la gramática del lenguaje. La gramática está escrita en `gramatica.y` y se procesa con el generador BYACC/J incluido como `yacc.linux`.

## Funcionalidad de la etapa

- Reconocimiento de la estructura general del programa y de sus bloques declarativos y ejecutables.
- Declaraciones de variables, funciones, clases y objetos.
- Sentencias ejecutables, expresiones, asignaciones, selecciones y llamadas.
- Incorporación sintáctica de los temas particulares que se fueron sumando al lenguaje, como `REPEAT WHILE`, asignaciones dentro de expresiones, orden opcional de parámetros, `COMPTIME`, acceso posicional, herencia y conversiones `TODF`.
- Recuperación e informe de errores sintácticos según las producciones y acciones de la gramática.

## Archivos de esta carpeta

| Archivo | Función |
|---|---|
| `gramatica.y` | Fuente de la gramática. Contiene los tokens, las producciones y las acciones semánticas que se ejecutan cuando el parser reconoce una construcción. Es el archivo que debe editarse para modificar la sintaxis. |
| `Parser.java` | Parser Java generado por BYACC/J a partir de `gramatica.y`. No debe editarse manualmente como reemplazo de la gramática. |
| `ParserVal.java` | Clase utilizada por BYACC/J para transportar los valores semánticos asociados a los símbolos de la gramática. |
| `AnalizadorLexicoTp2.java` | Adapta el analizador léxico a la interfaz que espera el parser (`yylex()`), entrega tokens y valores semánticos mediante `yylval`. |
| `LecturaCodigoTp2.java` | Lee el archivo fuente y mantiene el seguimiento de la posición y línea de lectura. |
| `TokenTp2.java` | Define la representación de los tokens de esta etapa. |
| `TablaDeSimbolosTp2.java` | Almacena los símbolos reconocidos y permite su consulta durante el análisis. |
| `RegistroSimboloTp2.java` | Representa una entrada individual de la Tabla de Símbolos. |
| `TablaPalabrasReservadasTp2.java` | Mantiene la correspondencia entre palabras reservadas y códigos de token. |
| `MainTp2.java` | Coordina la lectura, el análisis léxico y el parser. Reúne las salidas del análisis y presenta los resultados y errores de la etapa. |
| `PruebaTp2.txt` | Archivo fuente de ejemplo para probar el TP2. |
| `yacc.linux` | Ejecutable BYACC/J incluido para generar `Parser.java` y `ParserVal.java` a partir de la gramática. |
| `README.md` | Documentación de esta etapa. |

## Ejecución

Desde la raíz del repositorio:

```bash
./ejecutarTp2.sh TrabajoPractico2/PruebaTp2.txt
```

El script realiza los siguientes pasos:

1. Ingresa en `TrabajoPractico2/`.
2. Ejecuta `yacc.linux -J -v gramatica.y` para generar el parser y el archivo de descripción de estados.
3. Añade el `package TrabajoPractico2` a los archivos Java generados.
4. Compila las clases Java de la carpeta.
5. Ejecuta `TrabajoPractico2.MainTp2` con el archivo de prueba indicado.

Se requiere Java y Bash. No es necesario instalar YACC por separado porque se utiliza el ejecutable incluido en la carpeta.

## Archivos generados

La gramática es la fuente de verdad del parser. Cuando se cambia `gramatica.y`, se debe volver a ejecutar el script para regenerar el parser. El archivo `y.output` es una salida de diagnóstico del generador y sirve para analizar los estados de la gramática.
