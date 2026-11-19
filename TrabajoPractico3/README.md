# Trabajo Práctico 3 — Árbol sintáctico y análisis semántico

En esta etapa se amplía el parser para construir la representación intermedia asignada al **Grupo 8: Árbol Sintáctico (AST)**. También se incorporan chequeos semánticos, información de tipos y usos en la Tabla de Símbolos y administración de ámbitos.

## Qué realiza esta etapa

- Construye un árbol a partir de las acciones semánticas de `gramatica.y`.
- Representa expresiones, asignaciones, bloques, sentencias de control, funciones, métodos, clases, accesos posicionales, invocaciones, impresiones y retornos mediante nodos especializados.
- Administra los ámbitos de variables, funciones, clases y métodos.
- Enriquece los registros de la Tabla de Símbolos con tipo, uso, ámbito, nombre completo, parámetros y atributos particulares.
- Comprueba declaraciones, visibilidad, redeclaraciones, compatibilidad de tipos y reglas asociadas a los temas del grupo.
- Permite imprimir el AST y una representación textual del código intermedio.

## Archivos de la carpeta `TrabajoPractico3/`

| Archivo o carpeta | Función |
|---|---|
| `gramatica.y` | Gramática del TP3 con acciones semánticas para registrar declaraciones, consultar la Tabla de Símbolos y construir los nodos del árbol. |
| `Parser.java` | Parser Java generado a partir de `gramatica.y`. |
| `ParserVal.java` | Contenedor de valores semánticos utilizados por las acciones de YACC/BYACC-J. |
| `AnalizadorLexicoTp3.java` | Analizador léxico adaptado para entregar tokens y valores semánticos al parser, registrando constantes e identificadores. |
| `LecturaCodigoTp3.java` | Lectura del archivo fuente y seguimiento de líneas. |
| `TokenTp3.java` | Representación de un token de la etapa. |
| `TablaPalabrasReservadasTp3.java` | Tabla de palabras reservadas y sus identificadores de token. |
| `RegistroSimboloTp3.java` | Modelo de una entrada semántica: lexema, tipo de token, tipo, uso, ámbito, nombre completo, línea, atributos y parámetros. |
| `TablaDeSimbolosTp3.java` | Tabla de símbolos enriquecida. Incluye declaraciones y búsquedas por ámbito, constantes, parámetros, atributos, clases base, miembros heredados, accesos posicionales y compatibilidad estructural de objetos. |
| `GestionAmbitoTp3.java` | Administra la entrada y salida de ámbitos anidados y coordina las declaraciones y búsquedas con la Tabla de Símbolos. |
| `ErrorTp3.java` | Representa un error con su categoría, línea y descripción. |
| `RecolectorErroresTp3.java` | Acumula los errores detectados para imprimirlos de manera conjunta. |
| `MainTp3.java` | Punto de entrada del TP3. Ejecuta el análisis, recupera el AST, configura la tabla y el recolector para los nodos, imprime el árbol y la representación intermedia. |
| `yacc.linux` | Ejecutable BYACC/J utilizado para generar el parser. |
| `README.md` | Documentación de esta etapa. |

## Archivos de `ArbolSintactico/`

| Archivo | Función |
|---|---|
| `NodoAST.java` | Clase base abstracta de los nodos. Define la etiqueta, el acceso compartido a la Tabla de Símbolos y al recolector de errores, y los métodos de generación de representación. |
| `NodoPrograma.java` | Raíz del árbol. Contiene el nombre del programa, las declaraciones y el bloque ejecutable. |
| `NodoBloque.java` | Almacena una lista dinámica de sentencias. |
| `NodoHoja.java` | Representa identificadores, constantes y cadenas, vinculándolos con la información de la Tabla de Símbolos cuando corresponde. |
| `NodoBinario.java` | Representa operaciones con dos operandos, como operaciones aritméticas y comparaciones; también determina el tipo resultante. |
| `NodoUnario.java` | Representa operaciones con un operando, entre ellas conversiones explícitas. |
| `NodoAsignacion.java` | Representa asignaciones entre variables y expresiones y realiza verificaciones asociadas. |
| `NodoIF.java` | Representa una selección con condición, bloque THEN y bloque ELSE opcional. |
| `NodoRepeat.java` | Representa la iteración REPEAT WHILE del Tema 13. |
| `NodoFuncion.java` | Representa la declaración de una función, sus parámetros, declaraciones locales y cuerpo ejecutable. |
| `NodoMetodo.java` | Representa la declaración de un método de clase y su cuerpo. |
| `NodoClase.java` | Representa una declaración de clase y su cuerpo. |
| `NodoInvocacion.java` | Representa una llamada a función, sus argumentos y el orden opcional de evaluación. |
| `NodoInvocacionMetodo.java` | Representa una llamada a método sobre un objeto, incluyendo argumentos y orden de evaluación. |
| `NodoListaParametros.java` | Almacena dinámicamente los nodos correspondientes a los argumentos de una invocación. |
| `NodoAccesoArreglo.java` | Representa el acceso posicional a atributos de objetos (Tema 29). |
| `NodoPrint.java` | Representa una sentencia `POUT`. |
| `NodoReturn.java` | Representa una sentencia `RET` y conserva la expresión y el tipo de retorno esperado. |
| `GeneradorTAC.java` | Proporciona la generación de nombres temporales y etiquetas para la representación textual auxiliar. |
| `ImpresorArbol.java` | Recorre el árbol e imprime sus nodos con indentación para visualizar la jerarquía. |

## Ejecución

Desde la raíz del repositorio:

```bash
./ejecutarTp3.sh CasosDePrueba/CasoIntegralValido.txt
```

El script genera el parser desde `gramatica.y`, prepara el paquete de las clases generadas, compila los fuentes de `TrabajoPractico3/` en `build/` y ejecuta `TrabajoPractico3.MainTp3`.

## Salidas

La ejecución muestra los errores acumulados, la Tabla de Símbolos, el árbol sintáctico y la representación textual generada por los nodos. La estructura del AST se mantiene como representación intermedia principal del grupo; la salida textual es una forma auxiliar de inspeccionar las instrucciones.
