#!/usr/bin/env bash
set -euo pipefail

# Resolve project root even if the path contains spaces
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CLASSPATH="${ROOT}/lib/*"
OUT_DIR="${ROOT}/out"

rm -rf "${OUT_DIR}"
mkdir -p "${OUT_DIR}"
mkdir -p "${OUT_DIR}/META-INF"

# Collect all Java sources
mapfile -t SOURCES < <(find "${ROOT}" -name "*.java")

# Compile with external libs on the classpath
javac -cp "${CLASSPATH}" -d "${OUT_DIR}" "${SOURCES[@]}"

echo "Compilation OK"

# Run the main application
java -cp "${OUT_DIR}:${CLASSPATH}" MainApp
