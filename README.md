# Sky Airlines Peru - Backend

Aplicacion de escritorio para gestion administrativa de vuelos, pasajeros, equipaje y personal.

## Como Ejecutar

### Requisitos
- JDK 17 o superior
- Maven 3.8 o superior
- PostgreSQL 14 o superior

### 1. Clonar el repositorio
```bash
git clone https://github.com/Feerth/skyairline-system.git
cd skyairline-system/backend
```

### 2. Compilar
```bash
mvn clean package -DskipTests
```

### 3. Configurar la base de datos

**Crear la BD y ejecutar el schema:**
```bash
psql -U postgres -c "CREATE DATABASE skyairline_db;"
psql -U postgres -d skyairline_db -f src/main/resources/db/schema.sql
psql -U postgres -d skyairline_db -f src/main/resources/db/setup_admin.sql
```

**Si usa una contrasena diferente a `postgres`:**
Editar el archivo `backend/src/main/resources/db/config.properties` y cambiar la contrasena:
```properties
db.url=jdbc:postgresql://localhost:5432/skyairline_db
db.user=postgres
db.password=SU_CONTRASENA_AQUI
```

### 4. Ejecutar
```bash
java -jar target/skyairlines-desktop-2.4.1.jar
```

### 5. Iniciar sesion
- **Usuario:** admin@skyairline.com
- **Contrasena:** admin123

## Credenciales de Base de datos

Las credenciales por defecto son:
- **Usuario:** postgres
- **Contrasena:** postgres
- **Host:** localhost
- **Puerto:** 5432
- **Base de datos:** skyairline_db

Si cambio la contrasena de PostgreSQL durante la instalacion, edite el archivo `src/main/resources/db/config.properties`.

## Modulos
- **Dashboard** - Metricas en tiempo real y alertas
- **Vuelos** - CRUD completo con detalles de ruta y aeronave
- **Equipaje** - Trazabilidad con flujo de estados
- **Asientos** - Inventario en vivo con simulador de cancelacion
- **Pasajeros** - Roster por vuelo
- **Clientes** - ABM con usuario asociado
- **Personal** - ABM de empleados con roles
- **Usuarios** - Gestion de usuarios del sistema
- **Reportes** - En construccion
