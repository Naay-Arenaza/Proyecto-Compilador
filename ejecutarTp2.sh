#!/bin/bash

# 1. Verificar que se haya pasado un argumento
if [ -z "$1" ]; then
  echo "Error: Falta el archivo de texto."
  echo "Uso: $0 <archivo_de_prueba.txt>"
  exit 1
fi

ARCHIVO_PRUEBA=$1

# Detener el script si ocurre algún error en los comandos
set -e

echo "➡️ Generando parser con yacc..."
cd ./TrabajoPractico2
./yacc.linux -J -v gramatica.y

# --- SOLUCIÓN: INYECTAR EL PACKAGE EN LOS ARCHIVOS GENERADOS ---
echo "➡️ Añadiendo package a los archivos de Yacc..."
sed -i '1s/^/package TrabajoPractico2;\n/' Parser.java
sed -i '1s/^/package TrabajoPractico2;\n/' ParserVal.java
# ---------------------------------------------------------------

echo "➡️ Compilando archivos Java..."
cd ..
javac TrabajoPractico2/*.java

echo "➡️ Ejecutando Main con el archivo: $ARCHIVO_PRUEBA"
java TrabajoPractico2.MainTp2 "$ARCHIVO_PRUEBA"
