# Trabajo Práctico 1 — Analizador léxico

Esta carpeta contiene la primera versión del compilador: un analizador léxico desarrollado en Java mediante una matriz de transición de estados y una matriz de acciones semánticas. Su función es leer el archivo fuente, reconocer los componentes léxicos del lenguaje y registrar identificadores, constantes y cadenas en una Tabla de Símbolos.

## Temas particulares trabajados

- **Tema 2:** constantes enteras sin signo de 8 bits, con sufijo `$us` y rango de 0 a 255; palabra reservada `USHORTINT`.
- **Tema 8:** constantes de punto flotante de 64 bits (`DOUBLEF`), con exponente indicado mediante `d`/`D`.
- **Tema 9:** cadenas de una línea delimitadas por `{` y `}`.
- **Tema 16:** comentarios multilínea, según la delimitación establecida para el grupo. Ver la observación al final.

El analizador también reconoce identificadores, operadores aritméticos, asignaciones, comparadores, signos de puntuación y palabras reservadas generales del lenguaje. Los espacios y comentarios se descartan sin devolver tokens al consumidor.

## Archivos de esta carpeta

| Archivo | Función |
|---|---|
| `AnalizadorLexicoTp1.java` | Implementa el autómata léxico. Clasifica caracteres, consulta las matrices de estados y acciones, construye lexemas, reconoce tokens, valida rangos de constantes y registra símbolos. |
| `LecturaCodigoTp1.java` | Lee el archivo fuente carácter por carácter y permite devolver el último carácter cuando el autómata necesita volver a procesarlo. |
| `TokenTp1.java` | Modelo de token: almacena el identificador del token y, cuando corresponde, su atributo o lexema. |
| `TablaDeSimbolosTp1.java` | Mantiene la estructura dinámica donde se registran identificadores, constantes y cadenas reconocidas. También permite consultar e imprimir su contenido. |
| `RegistroSimboloTp1.java` | Representa una entrada individual de la Tabla de Símbolos. |
| `TablaPalabrasReservadasTp1.java` | Relaciona las palabras reservadas del lenguaje con los identificadores de token correspondientes. |
| `MainTp1.java` | Ejecuta una prueba independiente del analizador léxico, imprime los tokens reconocidos y muestra la Tabla de Símbolos. Actualmente utiliza el nombre fijo `TxtPrueba.txt`. |
| `TxtPrueba.txt` | Archivo de entrada de ejemplo utilizado por `MainTp1`. |

## Ejecución

Desde una terminal, ingresar a esta carpeta y ejecutar:

```bash
cd TrabajoPractico1
javac *.java
java MainTp1
```

El programa lee `TxtPrueba.txt`, imprime los tokens reconocidos y muestra el contenido de la Tabla de Símbolos.

> **Importante:** `MainTp1` no recibe actualmente la ruta del archivo por parámetro. Para probar otro programa, reemplazá el contenido de `TxtPrueba.txt` o modificá la ruta definida en `MainTp1.java`.

## Resultado esperado

La salida permite observar los tokens que reconoce el autómata y los símbolos que fueron incorporados a la tabla. Los errores léxicos y advertencias se informan durante el recorrido del archivo, incluyendo situaciones como identificadores que superan los 22 caracteres y constantes fuera de rango.

## Observación sobre el Tema 16

La consigna del grupo define comentarios multilínea delimitados por `{{` y `}}`. En el código del analizador se encuentra una acción identificada como comentario multilínea entre " ".
