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
./yacc.linux -J -v gramatica.y

echo "➡️ Compilando archivos Java..."
javac *.java

echo "➡️ Ejecutando Main con el archivo: $ARCHIVO_PRUEBA"
java Main "$ARCHIVO_PRUEBA"