# Proyecto Compilador — Grupo 8

Proyecto desarrollado para la materia **Diseño de Compiladores I – Compiladores e Intérpretes**. El repositorio reúne las entregas de los trabajos prácticos 1 a 4 y muestra la evolución del compilador: desde el reconocimiento de tokens hasta la generación de un módulo WebAssembly.

## Resumen del proyecto

El compilador está implementado en Java. Utiliza un analizador léxico construido con matrices de transición y acciones semánticas, un parser generado a partir de una gramática YACC/BYACC-J, una Tabla de Símbolos con información semántica y, desde el TP3, un **Árbol Sintáctico** como representación intermedia. En el TP4, el árbol se recorre para generar WebAssembly.

**Grupo:** 8  
**Representación intermedia:** Árbol Sintáctico (AST)  
**Backend del TP4:** WebAssembly (`.wat` y `.wasm`)

### Temas particulares del Grupo 8

| Tema | Funcionalidad |
|---|---|
| 2 | Enteros cortos sin signo de 8 bits (`USHORTINT`, constantes con sufijo `$us`, rango 0–255). |
| 8 | Punto flotante de 64 bits (`DOUBLEF`, exponente indicado con `d`/`D`). |
| 9 | Cadenas de una línea delimitadas por llaves. |
| 13 | Iteración `REPEAT ... WHILE (condición)`. |
| 16 | Comentarios multilínea. Ver la nota de implementación más abajo. |
| 17 | Asignaciones de expresiones dentro de expresiones mediante `=` y paréntesis. |
| 20 | Orden opcional de evaluación de parámetros reales en invocaciones. |
| 22 | Variables en tiempo de compilación (`COMPTIME`). |
| 25 | Asignación entre objetos por compatibilidad estructural. |
| 29 | Acceso posicional a atributos de objetos (`objeto[índice]`). |
| 30 | Herencia múltiple con resolución según el orden de las clases base. |
| 33 | Conversión explícita de entero a punto flotante (`TODF`). |

En el TP4 se incorporan los controles de ejecución asignados al grupo:

- **a)** División por cero para datos enteros y de punto flotante.
- **c)** Overflow en sumas de datos de punto flotante (`DOUBLEF`).
- **d)** Overflow en productos de enteros (`USHORTINT`).
- **i)** Detección de recursión en invocaciones de funciones.

## Recorrido general del compilador

```text
Código fuente (.txt)
        │
        ▼
Análisis léxico
  - reconoce tokens
  - registra símbolos y constantes
  - informa errores léxicos
        │
        ▼
Análisis sintáctico (YACC / BYACC-J)
  - valida la gramática
  - ejecuta acciones semánticas
        │
        ▼
Análisis semántico y Tabla de Símbolos
  - tipos, usos y ámbitos
  - visibilidad y redeclaraciones
  - chequeos propios de los temas
        │
        ▼
Árbol Sintáctico (AST)
  - nodos para expresiones y sentencias
  - representación intermedia dinámica
        │
        ▼
Generación WebAssembly (TP4)
  - variables auxiliares
  - instrucciones y funciones
  - controles en tiempo de ejecución
        │
        ▼
Archivo .wat ── wat2wasm ──► Archivo .wasm
                                  │
                                  ▼
                           probar.html
```

## Estructura del repositorio

```text
Proyecto-Compilador/
├── TrabajoPractico1/          # Analizador léxico inicial y Tabla de Símbolos
├── TrabajoPractico2/          # Gramática YACC y análisis sintáctico
├── TrabajoPractico3/          # Árbol sintáctico y análisis semántico
├── TrabajoPractico4/          # Generación WebAssembly y controles del grupo
│   ├── ArbolSintactico/
│   └── GeneracionWebAssembly/
├── CasosDePrueba/             # Programas de prueba de las funcionalidades del TP3/TP4
│   └── ErroresEnTiempoDeEjecucion/
├── ejecutarTp2.sh             # Genera el parser, compila y ejecuta el TP2
├── ejecutarTp3.sh             # Genera el parser, compila y ejecuta el TP3
├── ejecutarTp4.sh             # Genera el parser, compila y ejecuta el TP4
├── probar.html                # Cargador y ejecutor de módulos .wasm en el navegador
├── EjecucionG8.zip            # Paquete comprimido incluido en el repositorio
└── .gitignore                 # Excluye clases y artefactos generados
```

Cada carpeta de trabajo práctico contiene su propio README con el detalle de los archivos. La carpeta `CasosDePrueba/` también cuenta con una guía para identificar qué comprueba cada entrada.

## Requisitos

Para ejecutar las etapas desde Linux se necesita:

- **JDK 11 o superior** (se utiliza `javac` y código compatible con APIs de Java 11 o posteriores).
- **Bash** para ejecutar los scripts `.sh`.
- Permisos de ejecución para los scripts (`chmod +x ejecutarTp2.sh ejecutarTp3.sh ejecutarTp4.sh`, si hiciera falta).
- **WABT**, que proporciona el comando `wat2wasm`, para convertir los archivos de texto `.wat` a binarios `.wasm`.
- Un navegador moderno con soporte WebAssembly para utilizar `probar.html`.

El ejecutable `yacc.linux` está incluido en las carpetas de los TP2, TP3 y TP4, por lo que los scripts utilizan esa copia para generar el parser. No es necesario instalar un `yacc` del sistema para esos scripts.

## Cómo ejecutar cada etapa

Los comandos siguientes se ejecutan desde la **raíz del repositorio**.

### TP1 — Analizador léxico

```bash
cd TrabajoPractico1
javac *.java
java MainTp1
```

`MainTp1` utiliza el archivo `TxtPrueba.txt` por nombre fijo y no recibe actualmente una ruta como argumento. Por eso se ejecuta desde `TrabajoPractico1/`, donde se encuentra ese archivo.

### TP2 — Analizador sintáctico

```bash
./ejecutarTp2.sh TrabajoPractico2/PruebaTp2.txt
```

El script ejecuta BYACC/J sobre `TrabajoPractico2/gramatica.y`, prepara el `package` de los archivos generados, compila las clases Java y ejecuta `MainTp2` con el archivo indicado.

### TP3 — Árbol sintáctico y semántica

```bash
./ejecutarTp3.sh CasosDePrueba/CasoIntegralValido.txt
```

El script genera el parser a partir de `TrabajoPractico3/gramatica.y`, compila las fuentes en `build/` y ejecuta `TrabajoPractico3.MainTp3`.

### TP4 — Generación de WebAssembly

```bash
./ejecutarTp4.sh CasosDePrueba/CasoIntegralValido.txt
```

El script genera el parser, compila las fuentes de `TrabajoPractico4/`, ejecuta `MainTp4` y organiza los archivos `.wat` en `SalidaTp4/`. Si `wat2wasm` está instalado, también convierte el `.wat` correspondiente a `.wasm`. La carpeta `build/` y `SalidaTp4/` se crean durante la ejecución; no son carpetas fuente del repositorio.

### Ejecutar el módulo WebAssembly en el navegador

1. Ejecutá el TP4 para generar el archivo `.wasm`.
2. Abrí `probar.html` en un navegador compatible.
3. Seleccioná el archivo `.wasm` generado (normalmente desde `SalidaTp4/`).
4. Consultá la consola de salida que muestra los valores impresos y los mensajes de error de ejecución.

## Casos de prueba del TP4

Los programas de prueba principales están en `CasosDePrueba/`. Para ejecutar un archivo individual se utiliza el script del TP4, por ejemplo:

```bash
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/DivCero.txt
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/OverflowSumaPF.txt
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/OverflowProductoEnteros.txt
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/Recursion.txt
```

La descripción del resto de los archivos se encuentra en [`CasosDePrueba/README.md`](CasosDePrueba/README.md).

## Archivos generados y control de versiones

- `Parser.java` y `ParserVal.java` se generan a partir de `gramatica.y` mediante `yacc.linux` cuando se ejecuta el script correspondiente. En TP4 no se encuentran versionados y se vuelven a generar al ejecutar `ejecutarTp4.sh`.
- Los archivos `.class`, `.wat` y `.wasm` están excluidos por `.gitignore`.
- `build/` y `SalidaTp4/` son carpetas de salida creadas por los scripts.
- El archivo fuente de la gramática (`gramatica.y`) es el que debe modificarse cuando se cambia la sintaxis; los archivos del parser son productos generados.

---

**Materia:** Compiladores e Intérpretes  
**Grupo:** 8
