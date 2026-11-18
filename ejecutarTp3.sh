#!/bin/bash

set -Eeuo pipefail

# ============================================================
# CONFIGURACIÓN
# ============================================================

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$ROOT_DIR/TrabajoPractico3"
BUILD_DIR="$ROOT_DIR/build"
YACC="$SRC_DIR/yacc.linux"

# ============================================================
# FUNCIONES AUXILIARES
# ============================================================

error() {
    echo "ERROR: $*" >&2
    exit 1
}

# ============================================================
# 1. VALIDAR ARGUMENTOS
# ============================================================

if [[ $# -ne 1 ]]; then
    echo "Uso: $0 <archivo_de_prueba.txt>"
    exit 1
fi

ARCHIVO_PRUEBA="$1"

if [[ ! -f "$ARCHIVO_PRUEBA" ]]; then
    error "No existe el archivo de prueba: $ARCHIVO_PRUEBA"
fi

ARCHIVO_PRUEBA="$(cd "$(dirname "$ARCHIVO_PRUEBA")" && pwd)/$(basename "$ARCHIVO_PRUEBA")"

# ============================================================
# 2. VALIDAR ESTRUCTURA Y HERRAMIENTAS
# ============================================================

[[ -d "$SRC_DIR" ]] || error "No existe la carpeta TrabajoPractico3."
[[ -f "$SRC_DIR/gramatica.y" ]] || error "No se encontró gramatica.y."
[[ -x "$YACC" ]] || error "yacc.linux no existe o no tiene permisos de ejecución."

command -v java >/dev/null 2>&1 || error "Java no está instalado."
command -v javac >/dev/null 2>&1 || error "javac no está instalado."

# ============================================================
# 3. GENERAR PARSER
# ============================================================
echo "Generando parser..."
(
    cd "$SRC_DIR"
    "$YACC" -J -v gramatica.y
)

# ============================================================
# 4. PREPARAR PACKAGE DE LOS ARCHIVOS GENERADOS
# ============================================================

echo "Preparando archivos generados..."

for archivo in Parser.java ParserVal.java; do

    RUTA="$SRC_DIR/$archivo"

    [[ -f "$RUTA" ]] || error "No se generó $archivo."

    if ! grep -Eq '^[[:space:]]*package[[:space:]]+TrabajoPractico3;' "$RUTA"; then
        sed -i '1i package TrabajoPractico3;' "$RUTA"
    fi

done

# ============================================================
# 5. PREPARAR CARPETA DE COMPILACIÓN
# ============================================================
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

# Obtener todos los archivos Java del proyecto.
JAVA_FILES=()

while IFS= read -r -d '' archivo; do
    JAVA_FILES+=("$archivo")
done < <(find "$SRC_DIR" -type f -name "*.java" -print0)

[[ ${#JAVA_FILES[@]} -gt 0 ]] || error "No se encontraron archivos Java."

echo "Compilando ${#JAVA_FILES[@]} archivos Java..."

if ! javac -nowarn -encoding UTF-8 \
           -d "$BUILD_DIR" \
           "${JAVA_FILES[@]}" 2>/dev/null; then
    error "Falló la compilación."
fi

# ============================================================
# 6. EJECUTAR TP3
# ============================================================
echo "Ejecutando..."

java -cp "$BUILD_DIR" TrabajoPractico3.MainTp3 "$ARCHIVO_PRUEBA"

echo
