# Trabajo Práctico 4 — Generación de WebAssembly

El TP4 toma como entrada el árbol sintáctico generado en la etapa anterior y agrega la traducción a **WebAssembly**. El grupo eligió esta alternativa de salida en lugar de Assembler Pentium. El generador produce primero un módulo textual `.wat`; luego, si está disponible la herramienta `wat2wasm`, el script lo convierte a un archivo binario `.wasm`.

## Funcionalidad de esta etapa

- Reutiliza el análisis léxico, sintáctico, semántico y la Tabla de Símbolos.
- Construye y visualiza el árbol sintáctico.
- Genera WebAssembly recorriendo los nodos del AST.
- Administra variables globales, parámetros, variables auxiliares y variables locales de funciones.
- Emite operaciones aritméticas y comparaciones según el tipo (`i32` para enteros representados en 32 bits y `f64` para `DOUBLEF`).
- Genera funciones, métodos, llamadas, retornos, bloques, selecciones e iteraciones.
- Almacena cadenas y mensajes de error en la memoria lineal del módulo.
- Incorpora los controles en tiempo de ejecución del Grupo 8: división por cero, overflow en sumas `DOUBLEF`, overflow en productos `USHORTINT` y detección de recursión.

## Archivos principales de `TrabajoPractico4/`

| Archivo o carpeta | Función |
|---|---|
| `MainTp4.java` | Punto de entrada. Ejecuta lectura, análisis léxico y sintáctico; recupera la raíz AST; conecta la tabla y el recolector con los nodos; genera la representación intermedia; imprime errores, árbol y Tabla de Símbolos; y guarda el `.wat` cuando la compilación termina sin errores. |
| `gramatica.y` | Gramática del lenguaje y acciones semánticas del TP4. Es la fuente desde la cual se genera el parser. |
| `Parser.java` / `ParserVal.java` | Archivos generados por BYACC/J al ejecutar el script. En TP4 se vuelven a generar localmente y no se versionan. |
| `yacc.linux` | Ejecutable BYACC/J incluido para generar el parser. |
| `AnalizadorLexicoTp4.java` | Analizador léxico basado en matrices de estados y acciones; entrega tokens al parser y registra constantes y símbolos. |
| `LecturaCodigoTp4.java` | Lee el archivo fuente carácter por carácter y mantiene la línea actual. |
| `TokenTp4.java` | Modelo simple de token con identificador y atributo opcional. |
| `TablaPalabrasReservadasTp4.java` | Asocia las palabras reservadas (por ejemplo `USHORTINT`, `DOUBLEF`, `REPEAT`, `WHILE`, `COMPTIME`, `EXTENDS` y `TODF`) con tokens del parser. |
| `RegistroSimboloTp4.java` | Representa una entrada de la Tabla de Símbolos, con lexema, tipo, uso, ámbito, nombre completo, línea, atributos y parámetros. |
| `TablaDeSimbolosTp4.java` | Almacena y resuelve símbolos, constantes, variables, funciones, clases, métodos, parámetros y atributos; también contiene verificaciones asociadas a los temas particulares. |
| `GestionAmbitoTp4.java` | Mantiene la pila de ámbitos y coordina la entrada y salida de ámbitos de programa, clase y función, junto con la resolución de símbolos. |
| `ErrorTp4.java` | Representa un error con categoría, línea y descripción. |
| `RecolectorErroresTp4.java` | Centraliza los errores léxicos, sintácticos, semánticos y de generación para mostrarlos al finalizar. |
| `ArbolSintactico/` | Contiene los nodos del AST, el impresor del árbol y el generador de temporales/etiquetas de la representación textual auxiliar. |
| `GeneracionWebAssembly/` | Contiene el generador del módulo WebAssembly. |

## Archivos de `ArbolSintactico/`

Las clases de nodos mantienen la representación intermedia y generan instrucciones WebAssembly mediante `generarWasm()`.

| Archivo | Función |
|---|---|
| `NodoAST.java` | Clase base abstracta. Mantiene la etiqueta del nodo y referencias compartidas a la Tabla de Símbolos y al recolector de errores. Define los métodos de generación. |
| `NodoPrograma.java` | Raíz del AST. Organiza declaraciones y bloque ejecutable y coordina la generación del módulo. |
| `NodoBloque.java` | Contiene una lista de sentencias y genera cada una en orden. |
| `NodoHoja.java` | Representa identificadores, constantes y cadenas; consulta el registro del símbolo y genera la referencia correspondiente. |
| `NodoBinario.java` | Representa operaciones aritméticas y comparaciones. Determina tipos y emite instrucciones WebAssembly específicas. |
| `NodoUnario.java` | Representa operaciones unarias y conversiones, entre ellas `TODF`. |
| `NodoAsignacion.java` | Verifica y genera asignaciones entre variables, expresiones y objetos. |
| `NodoIF.java` | Representa condiciones y ramas THEN/ELSE. |
| `NodoRepeat.java` | Representa la iteración REPEAT WHILE. |
| `NodoFuncion.java` | Representa declaraciones de funciones, parámetros, cuerpo y control de recursión. |
| `NodoMetodo.java` | Representa declaraciones de métodos y genera sus funciones WebAssembly. |
| `NodoClase.java` | Representa declaraciones de clase y sus miembros. |
| `NodoInvocacion.java` | Representa invocaciones a funciones, argumentos y orden opcional de parámetros. |
| `NodoInvocacionMetodo.java` | Representa invocaciones a métodos sobre objetos. |
| `NodoListaParametros.java` | Mantiene la lista de parámetros de una invocación. |
| `NodoAccesoArreglo.java` | Representa accesos posicionales a atributos de objetos y su traducción. |
| `NodoPrint.java` | Genera la salida correspondiente a `POUT`. |
| `NodoReturn.java` | Genera el retorno de una función y realiza el tratamiento asociado al valor devuelto. |
| `GeneradorTAC.java` | Genera nombres temporales y etiquetas para la representación textual auxiliar del árbol. No es el backend WebAssembly. |
| `ImpresorArbol.java` | Imprime el AST de forma jerárquica para facilitar su inspección. |

## `GeneracionWebAssembly/GeneradorWasm.java`

Es el componente central del backend. Entre sus responsabilidades se encuentran:

- Reiniciar y administrar contadores de auxiliares y etiquetas.
- Convertir tipos del lenguaje a tipos WebAssembly.
- Declarar variables globales y locales, y parámetros.
- Emitir instrucciones en el cuerpo de la función actual.
- Registrar cadenas en la memoria lineal y construir la sección `data`.
- Generar los mensajes y las llamadas para errores de ejecución.
- Administrar datos de objetos, direcciones, tamaños de clases y desplazamientos de atributos.
- Construir el módulo textual completo que se guarda como `.wat`.

## Controles en tiempo de ejecución del Grupo 8

| Control | Qué verifica |
|---|---|
| **a — División por cero** | Comprueba que el divisor sea distinto de cero antes de ejecutar la división, tanto para enteros como para punto flotante. |
| **c — Overflow en suma de punto flotante** | Comprueba que el resultado de una suma `DOUBLEF` no supere el máximo representable para el tipo. |
| **d — Overflow en producto de enteros** | Extiende los operandos `USHORTINT` a 64 bits, realiza el producto y verifica que no supere 255 antes de volver a 32 bits. |
| **i — Recursión** | Utiliza una bandera de actividad por función/método para detectar una nueva entrada mientras esa rutina continúa activa. |

Cuando se detecta una situación de error, el código generado invoca la función de entorno `error_runtime` con la dirección del mensaje en memoria y utiliza `unreachable` para detener la ejecución del módulo.

## Ejecución

Desde la raíz del repositorio:

```bash
./ejecutarTp4.sh CasosDePrueba/CasoIntegralValido.txt
```

El script:

1. Verifica el archivo de entrada y las herramientas Java.
2. Ejecuta BYACC/J sobre `TrabajoPractico4/gramatica.y`.
3. Prepara el `package` de los archivos generados.
4. Compila todos los fuentes Java de `TrabajoPractico4/` en `build/`.
5. Ejecuta `TrabajoPractico4.MainTp4`.
6. Mueve los `.wat` generados a `SalidaTp4/`.
7. Si encuentra `wat2wasm`, convierte el `.wat` correspondiente a `.wasm`.
8. Intenta abrir `probar.html` en el navegador.

Para convertir `.wat` a `.wasm` se necesita WABT instalado y disponible en el `PATH` como `wat2wasm`. Si no está disponible, el `.wat` sigue siendo la salida textual generada por el compilador, pero no se obtiene automáticamente el binario.

## Ejecutar los controles asignados

```bash
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/DivCero.txt
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/OverflowSumaPF.txt
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/OverflowProductoEnteros.txt
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/Recursion.txt
```

Después de generar el `.wasm`, abrí `probar.html`, seleccioná el archivo y observá la salida. La página importa las funciones `print_i32`, `print_f64`, `print_str` y `error_runtime` que requiere el módulo.

## Archivos generados

- `Parser.java` y `ParserVal.java`: se generan al ejecutar el script.
- `build/`: contiene las clases compiladas.
- `SalidaTp4/`: contiene los `.wat` y, si se pudo ejecutar `wat2wasm`, los `.wasm`.
- Los archivos `.wat` y `.wasm` no se versionan porque están excluidos por `.gitignore`.
