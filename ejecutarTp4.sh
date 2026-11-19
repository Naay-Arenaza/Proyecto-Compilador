#!/bin/bash

set -Eeuo pipefail

# ============================================================
# CONFIGURACIÓN
# ============================================================

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
SRC_DIR="$ROOT_DIR/TrabajoPractico4"
BUILD_DIR="$ROOT_DIR/build"
YACC="$SRC_DIR/yacc.linux"
CARPETA_SALIDA="$ROOT_DIR/SalidaTp4"

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

[[ -d "$SRC_DIR" ]] || error "No existe la carpeta TrabajoPractico4."
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

    if ! grep -Eq '^[[:space:]]*package[[:space:]]+TrabajoPractico4;' "$RUTA"; then
        sed -i '1i package TrabajoPractico4;' "$RUTA"
    fi

done

# ============================================================
# 5. PREPARAR CARPETA DE COMPILACIÓN Y SALIDA
# ============================================================
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"
mkdir -p "$CARPETA_SALIDA"

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
# 6. EJECUTAR TP4
# ============================================================
echo "Ejecutando..."

java -cp "$BUILD_DIR" TrabajoPractico4.MainTp4 "$ARCHIVO_PRUEBA"

# ============================================================
# 7. ORGANIZAR ARCHIVOS EN LA CARPETA DE SALIDA (A PRUEBA DE SUBCARPETAS)
# ============================================================
echo "Organizando archivos en SalidaTp4..."

# Busca cualquier .wat generado en el proyecto (incluso dentro de subcarpetas de pruebas) 
# y los mueve a SalidaTp4 (evitando mover los que ya estén ahí adentro)
find "$ROOT_DIR" -maxdepth 3 -name "*.wat" ! -path "$CARPETA_SALIDA/*" -exec mv {} "$CARPETA_SALIDA/" \;

# Detectar el nombre del archivo de prueba para convertirlo a .wasm
NOMBRE_PRUEBA=$(basename "$ARCHIVO_PRUEBA" .txt)
ARCHIVO_WAT_DESTINO="$CARPETA_SALIDA/${NOMBRE_PRUEBA}.wat"
ARCHIVO_WASM_DESTINO="$CARPETA_SALIDA/${NOMBRE_PRUEBA}.wasm"

# Si el .wat se guardó con el nombre del txt en SalidaTp4, generamos el .wasm ahí mismo
if [[ -f "$ARCHIVO_WAT_DESTINO" ]]; then
    if command -v wat2wasm >/dev/null 2>&1; then
        wat2wasm "$ARCHIVO_WAT_DESTINO" -o "$ARCHIVO_WASM_DESTINO"
        echo "Generado binario: SalidaTp4/${NOMBRE_PRUEBA}.wasm"
    fi
else
    # Por si acaso el MainTp4 le puso otro nombre, buscamos el último .wat movido
    ULTIMO_WAT=$(find "$CARPETA_SALIDA" -maxdepth 1 -name "*.wat" | tsort 2>/dev/null | tail -n 1)
    if [[ -n "$ULTIMO_WAT" && -f "$ULTIMO_WAT" ]]; then
        BASE_NAME=$(basename "$ULTIMO_WAT" .wat)
        wat2wasm "$ULTIMO_WAT" -o "$CARPETA_SALIDA/${BASE_NAME}.wasm"
        echo "Generado binario: SalidaTp4/${BASE_NAME}.wasm"
    fi
fi

echo "¡Listo! Resultados guardados en la carpeta SalidaTp4."
echo
# ============================================================
# 8. ABRIR HTML AUTOMÁTICAMENTE EN EL NAVEGADOR
# ============================================================
HTML_PATH="$ROOT_DIR/probar.html" # O el nombre de tu archivo html (ej: probar.html)

if [[ -f "$HTML_PATH" ]]; then
    echo "Abriendo el ejecutor web en el navegador..."
    if command -v xdg-open >/dev/null 2>&1; then
        xdg-open "$HTML_PATH" &>/dev/null &
    elif command -v sensible-browser >/dev/null 2>&1; then
        sensible-browser "$HTML_PATH" &>/dev/null &
    fi
fi