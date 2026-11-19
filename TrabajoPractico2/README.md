# Trabajo Práctico Nº 2: Analizador Sintáctico (Parser)

## 1. Objetivo
Construir un Analizador Sintáctico utilizando una herramienta tipo YACC (`yacc.linux` / BYACC/J) integrada con el Analizador Léxico adaptado (`yylex()`), validando la sintaxis general del lenguaje (bloques declarativos, ejecutables, funciones, clases, objetos, estructuras de selección e iteración) y eliminando todos los conflictos *shift/reduce* y *reduce/reduce*.

## 2. Temas Particulares Asignados (Integración Sintáctica)
* **Tema 13 (Control Iterativo):** Sentencia `repeat <bloque> while (<condicion>);`.
* **Tema 17 (Asignaciones en expresiones):** Uso del operador `=` para asignar expresiones dentro de términos aritméticos o condiciones sin anidamiento complejo.
* **Tema 20 (Orden de evaluación opcional):** Invocaciones a funciones con lista opcional de constantes para el orden de evaluación de parámetros (`ID (...) [1, 3, 2]`).
* **Tema 22 (Comptime):** Declaración de variables en tiempo de compilación (`comptime <tipo> <vars>;`).
* **Tema 25 (Asignación de objetos sin código):** Compatibilidad y reglas sintácticas para asignaciones entre objetos.
* **Tema 29 (Acceso posicional a atributos):** Notación basada en índices posicionales (`al[0$i] = 3$i`).
* **Tema 30 (Desambiguado por orden de herencia):** Clases con sentencia `extends cb, cc;`.
* **Tema 33 (Conversiones explícitas):** Operador de conversión explícita `tosf(<expr>)` / `todf(<expr>)`.

## 3. Resolución de Conflictos y Decisiones de Implementación
* **Conflictos del Parser:** Se refactorizaron las reglas de factores y constantes negativas unarias, se eliminaron reglas muertas (*rules never reduced*) como `expresion_simple` y se ajustaron las sentencias de herencia (`sentencia_extends`) y parámetros reales para garantizar 0 conflictos *shift/reduce* y *reduce/reduce*.
* **Paso de Tokens:** Uso estricto de `ParserVal` para coordinar referencias a la Tabla de Símbolos y tipos entre el lexer y el parser.

## 4. Distribución de Archivos (Carpeta `TrabajoPractico2/`)
* `gramatica.y`: Especificación completa de la gramática libre de contexto con acciones semánticas y manejo de errores sintácticos.
* `Parser.java` / `ParserVal.java`: Clases Java generadas por Yacc para el análisis sintáctico.
* `AnalizadorLexicoTp2.java`: Lexer adaptado para retornar tokens y punteros a la tabla mediante `yylex()`.
* `MainTp2.java`: Orquestador principal de ejecución del TP2.
* `README.md`: Documentación específica de la etapa sintáctica.