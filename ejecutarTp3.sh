
#!/bin/bash

set -Eeuo pipefail

# 1. Verificar que se haya pasado un argumento
if [ -z "${1:-}" ]; then
    echo "Error: Falta el archivo de texto."
    echo "Uso: $0 <archivo_de_prueba.txt>"
    exit 1
fi

# 2. Obtener las rutas del proyecto
ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$ROOT_DIR/TrabajoPractico3"
BUILD_DIR="$ROOT_DIR/build"

# Conservar la ruta del archivo de prueba
ARCHIVO_PRUEBA="$1"

if [[ "$ARCHIVO_PRUEBA" != /* ]]; then
    ARCHIVO_PRUEBA="$PWD/$ARCHIVO_PRUEBA"
fi

if [[ ! -f "$ARCHIVO_PRUEBA" ]]; then
    echo "Error: No existe el archivo $ARCHIVO_PRUEBA"
    exit 1
fi

# 3. Generar el parser con Yacc
echo "➡️ Generando parser con yacc..."

(
    cd "$SRC_DIR"
    ./yacc.linux -J -v gramatica.y
)

# 4. Añadir el package a los archivos generados
echo "➡️ Añadiendo package a los archivos de Yacc..."

for archivo in Parser.java ParserVal.java; do
    if [[ ! -f "$SRC_DIR/$archivo" ]]; then
        echo "Error: No se generó $archivo"
        exit 1
    fi

    if ! grep -q '^package TrabajoPractico3;' "$SRC_DIR/$archivo"; then
        sed -i '1i package TrabajoPractico3;' "$SRC_DIR/$archivo"
    fi
done

# 5. Limpiar y crear la carpeta de compilación
echo "➡️ Preparando compilación..."

rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

# 6. Compilar TODOS los archivos Java juntos
echo "➡️ Compilando archivos Java..."

find "$SRC_DIR" -name "*.java" -print0 | \
    xargs -0 javac -encoding UTF-8 -d "$BUILD_DIR"

# 7. Ejecutar el programa
echo "➡️ Ejecutando MainTp3..."

java -cp "$BUILD_DIR" TrabajoPractico3.MainTp3 "$ARCHIVO_PRUEBA"
