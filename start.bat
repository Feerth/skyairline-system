@echo off
REM =====================================================================
REM  Sky Airline Peru - Levanta backend + web + sistema de escritorio
REM  Uso: doble clic en este archivo, o ejecutar "start.bat" en la
REM  terminal desde la carpeta raiz del proyecto (skyairline-system).
REM =====================================================================
setlocal

set ROOT=%~dp0
set BACKEND_DIR=%ROOT%backend
set DESKTOP_DIR=%ROOT%desktop
set WEB_DIR=%ROOT%web
set WEB_PORT=5500

echo.
echo === Sky Airline Peru: iniciando sistema completo ===
echo.

REM --- 1) Backend (Spring Boot) en una ventana nueva ---
echo [1/3] Iniciando backend (Spring Boot) en http://localhost:8080 ...
start "SkyAirline - Backend" cmd /k "cd /d "%BACKEND_DIR%" && mvn spring-boot:run"

REM --- 2) Esperar a que el backend responda antes de continuar ---
echo Esperando a que el backend este listo...
:esperar_backend
powershell -Command "try { $r = Invoke-WebRequest -Uri http://localhost:8080/api/aeropuertos -UseBasicParsing -TimeoutSec 2; exit 0 } catch { exit 1 }" >nul 2>&1
if errorlevel 1 (
    timeout /t 3 /nobreak >nul
    goto esperar_backend
)
echo Backend listo.
echo.

REM --- 3) Web (servidor local simple) en una ventana nueva ---
echo [2/3] Iniciando web en http://localhost:%WEB_PORT% ...
start "SkyAirline - Web" cmd /k "cd /d "%WEB_DIR%" && python -m http.server %WEB_PORT%"
timeout /t 2 /nobreak >nul
start "" "http://localhost:%WEB_PORT%"

REM --- 4) Sistema de escritorio (JavaFX) en esta misma ventana ---
echo [3/3] Iniciando sistema de escritorio (JavaFX)...
echo.
cd /d "%DESKTOP_DIR%"
mvn javafx:run

echo.
echo El sistema de escritorio se cerro. El backend y la web siguen
echo corriendo en sus propias ventanas; cierralas manualmente si ya
echo no las necesitas.
pause
