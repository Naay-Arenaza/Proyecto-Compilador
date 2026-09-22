# Compilador - TP1 & TP2 (Léxico y Sintáctico)
**Materia:** Compiladores e Intérpretes  
**Nro. de grupo:** 8
**Año:** 2026

## Requisitos Previos
- **Java Development Kit (JDK):** Versión 8 o superior (recomendado JDK 17+).
- **Yacc / BYacc/J (Opcional):** Si se desea regenerar el parser desde `gramatica.y`. Los archivos generados (`Parser.java`, `ParserVal.java`) ya están incluidos.

## Organización
- `Main.java`: Recibe la ruta del archivo por argumento de la consola, ejecuta primero una pasada léxica mostrando los tokens y la tabla de símbolos inicial, y luego reinicia el flujo del archivo para ejecutar el análisis sintáctico con `Parser.yyparse()`.
- `AnalizadorLexico.java`: Analizador léxico completo e integrado al Parser de Yacc. Implementa la interfaz de comunicación de tokens (`yylex()` y `yylval`) requerida por la gramática del TP2.
- `AnalizadorLexicoTp1.java`: Versión original del analizador léxico desarrollada para los requerimientos específicos del TP1, conservada como respaldo para no solapar comportamientos con las reglas del TP2.
- `gramatica.y`: Archivo de definición formal de la sintaxis y reglas de producción para Yacc/BYacc.
- `Parser.java` y `ParserVal.java`: Código Java del analizador sintáctico generado por BYacc/J.
- `LecturaCodigo.java`: Manejo de lectura secuencial de caracteres y control del número de línea para el reporte de advertencias y errores.
- `TablaDeSimbolos.java` / `RegistroSimbolo.java`: Estructura dinámica encargada de almacenar identificadores, cadenas y constantes reconocidas.
- `TablaPalabrasReservadas.java`: Registro estático de palabras reservadas.
- `TxtPruebaTp1.txt`: Casos léxicos del TP1 (conservados como referencia de pruebas unitarias de tokens y límites).
- `TxtPruebaTp2.txt`: Archivo general de pruebas sintácticas del TP2.
- `ejecutar.sh`: Archivo general de pruebas sintácticas del TP2.

## Casos de Prueba Sintácticos (TP2)

El archivo `TxtPruebaTp2.txt` contiene un conjunto de **17 casos de prueba** que evalúan el lenguaje y el manejo de errores sintácticos:

1. **Casos con errores sintácticos intencionales (1 al 16):**
   - Ausencia de encabezado/nombre de programa y delimitadores (`BEGIN` / `END`).
   - Omisión de punto y coma (`;`) al final de sentencias.
   - Declaraciones de funciones inválidas (falta de identificador, omisión de tipo o nombre de parámetros formales).
   - Variables sin separador coma (`,`).
   - Expresiones incompletas (falta de operando u operador).
   - Errores en sentencias de control: falta de argumentos en `POUT`, ausencia de paréntesis en condición de `IF`, omisión de `END_IF`, omisión de cuerpo en iteraciones `REPEAT...WHILE`.
   - Uso incorrecto del operador de asignación (`=` en lugar de `:=`).
   - Declaraciones `COMPTIME` sin tipo especificado.
   - Herencia de clases (`EXTENDS`) sin identificadores.

2. **Caso sintáctico correcto (Prueba final):**
   - Programa completo que integra declaraciones de variables `COMPTIME`, herencia de clases (`EXTENDS`), definición e invocación de funciones con parámetros indexados, sentencias de salida (`POUT`), estructuras de control `IF` y ciclos `REPEAT...WHILE`.

### Recomendación de Prueba
El archivo `TxtPruebaTp2.txt` incluye todos los casos concatenados para facilitar una ejecución global. Sin embargo, debido a que varios programas presentan errores intencionales y activan el descarte/sincronización de tokens del parser, **se recomienda aislar o probar los programas de forma individual en archivos de texto separados**. Esto permite apreciar con total claridad la salida limpia, las estructuras reconocidas y el diagnóstico exacto de cada caso.

## Compilación y Ejecución
La aplicación no posee interfaz gráfica y se opera íntegramente por consola.

### Uso mediante script automatizado (Linux)
Para facilitar la ejecución en entornos Linux, se incluye el script `ejecutar.sh` que compila el código y lanza el programa. Como establece la consigna, la ruta del archivo fuente a compilar debe pasarse obligatoriamente como parámetro[cite: 4].

1. **Dar permisos de ejecución al script:**
   `chmod +x ./ejecutar.sh`
2. **Ejecutar el compilador pasando el archivo de prueba:**
   `./ejecutar.sh TxtPruebaTp2.txt`