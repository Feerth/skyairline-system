#!/usr/bin/env bash
# =====================================================================
# Sky Airline Peru - Levanta backend + web + sistema de escritorio
# Uso:
#   chmod +x start.sh   (solo la primera vez)
#   ./start.sh
# =====================================================================
set -e

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
BACKEND_DIR="$ROOT/backend"
DESKTOP_DIR="$ROOT/desktop"
WEB_DIR="$ROOT/web"
WEB_PORT=5500
LOG_DIR="$ROOT/.logs"

mkdir -p "$LOG_DIR"

BACKEND_PID=""
WEB_PID=""

cleanup() {
    echo ""
    echo "Deteniendo backend y servidor web..."
    [ -n "$BACKEND_PID" ] && kill "$BACKEND_PID" 2>/dev/null
    [ -n "$WEB_PID" ] && kill "$WEB_PID" 2>/dev/null
    exit 0
}
trap cleanup INT TERM

echo ""
echo "=== Sky Airline Peru: iniciando sistema completo ==="
echo ""

# --- 1) Backend (Spring Boot) en segundo plano ---
echo "[1/3] Iniciando backend (Spring Boot) en http://localhost:8080 ..."
(cd "$BACKEND_DIR" && mvn spring-boot:run > "$LOG_DIR/backend.log" 2>&1) &
BACKEND_PID=$!

# --- 2) Esperar a que el backend responda ---
echo "Esperando a que el backend este listo (esto puede tardar un poco la primera vez)..."
until curl -s -o /dev/null http://localhost:8080/api/aeropuertos; do
    sleep 3
    if ! kill -0 "$BACKEND_PID" 2>/dev/null; then
        echo ""
        echo "El backend se detuvo inesperadamente. Revisa el log en: $LOG_DIR/backend.log"
        exit 1
    fi
done
echo "Backend listo."
echo ""

# --- 3) Web (servidor local simple) en segundo plano ---
echo "[2/3] Iniciando web en http://localhost:$WEB_PORT ..."
(cd "$WEB_DIR" && python3 -m http.server "$WEB_PORT" > "$LOG_DIR/web.log" 2>&1) &
WEB_PID=$!
sleep 1

if command -v open >/dev/null 2>&1; then
    open "http://localhost:$WEB_PORT"
elif command -v xdg-open >/dev/null 2>&1; then
    xdg-open "http://localhost:$WEB_PORT"
fi

# --- 4) Sistema de escritorio (JavaFX) en primer plano ---
echo "[3/3] Iniciando sistema de escritorio (JavaFX)..."
echo "(Cierra la ventana del programa, o presiona Ctrl+C aqui, para detener todo)"
echo ""
cd "$DESKTOP_DIR"
mvn javafx:run

cleanup
