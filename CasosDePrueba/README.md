# Casos de prueba

Esta carpeta reúne programas fuente utilizados para comprobar las funcionalidades del lenguaje, los chequeos semánticos y los controles en tiempo de ejecución del Grupo 8.

Los archivos son entradas de texto para el compilador. Para ejecutar uno con la última etapa, usar desde la raíz del repositorio:

```bash
./ejecutarTp4.sh CasosDePrueba/NOMBRE_DEL_ARCHIVO.txt
```

Para ejecutar un control de tiempo de ejecución, indicar el archivo que se encuentra dentro de `ErroresEnTiempoDeEjecucion/`.

## Casos generales y temas particulares

| Archivo | Qué permite probar |
|---|---|
| `AccesoAtributos-Tema29-txt` | Accesos posicionales a atributos de objetos (Tema 29). El nombre del archivo no incluye la extensión `.txt`. |
| `Ambitos.txt` | Declaraciones y visibilidad de símbolos en distintos ámbitos. |
| `AsignacionExpresion-Tema17.txt` | Asignaciones de expresiones dentro de otras expresiones (Tema 17). |
| `AsignacionExpresionControl-Tema17.txt` | Asignaciones dentro de expresiones en condiciones o estructuras de control (Tema 17). |
| `AsignacionObjetos-Tema25.txt` | Asignación entre objetos mediante compatibilidad estructural (Tema 25). |
| `AtributoMismoNombreVariableGlobal.txt` | Diferenciación entre un atributo y una variable global con el mismo nombre. |
| `CasoIntegralValido.txt` | Programa de integración que combina varias construcciones del lenguaje en un caso válido. |
| `Comptime-Tema22.txt` | Declaración y utilización de variables `COMPTIME` (Tema 22). |
| `ControlYAnidamiento-Tema13.txt` | Sentencia REPEAT WHILE y anidamiento de estructuras de control (Tema 13). |
| `Conversiones-Tema33.txt` | Conversiones explícitas de entero a punto flotante mediante `TODF` (Tema 33). |
| `DesambiguacionMetodo-Tema30.txt` | Resolución de métodos heredados con la regla de orden de herencia (Tema 30). |
| `ErrorDeTipo.txt` | Casos con incompatibilidades de tipos. |
| `ErroresDeAlcanceYRedeclaracion.txt` | Símbolos no visibles, no declarados o redeclarados. |
| `HerenciaAtributos-Tema30.txt` | Acceso a atributos heredados (Tema 30). |
| `HerenciaMetodo-Tema30.txt` | Acceso e invocación de métodos heredados (Tema 30). |
| `MismoNombreAtributoClasesDiferentes.txt` | Atributos con el mismo nombre en clases diferentes y resolución según clase/ámbito. |
| `ModificadorFunciones-Tema20.txt` | Invocaciones con orden opcional de evaluación de parámetros (Tema 20). |
| `ObjetoGlobalVisibleFuncion.txt` | Visibilidad de un objeto global desde el ámbito de una función. |
| `ObjetoLocalMismoNombreGlobal.txt` | Resolución de un objeto local que comparte nombre con uno global. |
| `RedeclararMetodoHeredado.txt` | Caso de redeclaración de un método heredado. |
| `VariableNombreFuncion.txt` | Diferenciación de usos cuando una variable comparte nombre con una función. |
| `VariosCasosDePruebaExtras.txt` | Colección adicional de situaciones de prueba. Revisar los comentarios incluidos dentro del archivo para identificar cada escenario. |

## Controles en tiempo de ejecución — Grupo 8

Los siguientes casos se encuentran en `ErroresEnTiempoDeEjecucion/` y deben finalizar con el mensaje de error correspondiente cuando se alcanza la operación problemática.

| Archivo | Control | Situación que busca provocar |
|---|---|---|
| `DivCero.txt` | **a — División por cero** | Ejecuta una división cuyo divisor entero es cero. |
| `OverflowSumaPF.txt` | **c — Overflow en suma de punto flotante** | Suma valores `DOUBLEF` cuyo resultado supera el rango máximo del tipo. |
| `OverflowProductoEnteros.txt` | **d — Overflow en producto de enteros** | Multiplica valores `USHORTINT` de forma que el resultado supera 255. |
| `Recursion.txt` | **i — Recursión** | Invoca nuevamente una función que todavía se encuentra activa. |

Ejemplo de ejecución:

```bash
./ejecutarTp4.sh CasosDePrueba/ErroresEnTiempoDeEjecucion/OverflowProductoEnteros.txt
```

## Recomendación para ejecutar las pruebas

1. Ejecutar cada archivo individualmente con `ejecutarTp4.sh`.
2. Revisar los mensajes de error y la Tabla de Símbolos impresos por el compilador.
3. Cuando la compilación finaliza correctamente, revisar el `.wat` generado en `SalidaTp4/`.
4. Si está instalado `wat2wasm`, cargar el `.wasm` correspondiente en `probar.html` para observar el comportamiento durante la ejecución.

**Nota:** los casos enumerados son programas de entrada, no una suite de pruebas automatizada. La verificación se realiza ejecutando el compilador sobre cada archivo.
