# Sky Airlines Peru - Desktop Application v2.4.1

Sistema de Gestion Administrativa para Sky Airlines Peru. Aplicacion de escritorio desarrollada en Java Swing con conexion directa a PostgreSQL.

## Arquitectura

- **Patron:** MVC + DAO (Model-View-Controller + Data Access Object)
- **UI:** Java Swing programatico (sin NetBeans GUI Builder)
- **Base de Datos:** PostgreSQL con JDBC nativo
- **Build:** Maven (standard layout)
- **JDK:** 17+

## Estructura del Proyecto

```
backend/
├── pom.xml
├── src/main/java/com/skyairlines/
│   ├── Main.java                          # Punto de entrada
│   ├── config/ConexionBD.java             # Conexion PostgreSQL
│   ├── dao/api/                           # Interfaces DAO (16 archivos)
│   ├── dao/impl/                          # Implementaciones JDBC (15 archivos)
│   ├── exception/                         # Excepciones personalizadas
│   ├── model/entity/                      # Entidades POJO (16 + DTO)
│   ├── model/enums/                       # Enums de dominio (5)
│   ├── model/tablemodel/                  # Modelos de tabla dinamicos (9)
│   ├── util/                              # Utilidades (Session, Dates, Swing)
│   └── view/                              # Vistas Swing (11 paneles + Login + Main)
└── src/main/resources/db/schema.sql       # Script de base de datos
```

## Requisitos Previos

| Componente | Version Minima | Verificar |
|------------|---------------|-----------|
| JDK | 17+ | `java -version` |
| Maven | 3.8+ | `mvn -version` |
| PostgreSQL | 14+ | `psql --version` |

### Instalar JDK 17+

**Windows:**
Descargar desde https://adoptium.net/ y ejecutar el instalador.

**Linux:**
```bash
sudo apt install openjdk-17-jdk
```

**Mac:**
```bash
brew install openjdk@17
```

### Instalar Maven

**Windows:**
Descargar desde https://maven.apache.org/download.cgi, descomprimir y agregar `bin/` al PATH.

**Linux/Mac:**
```bash
sudo apt install maven    # Linux
brew install maven        # Mac
```

### Instalar PostgreSQL

**Windows:**
Descargar desde https://www.postgresql.org/download/windows/ e instalar. Recordar la contraseña del usuario `postgres`.

**Linux:**
```bash
sudo apt install postgresql
sudo systemctl start postgresql
```

## Como Ejecutar (Paso a Paso)

### 1. Clonar el repositorio
```bash
git clone https://github.com/Feerth/skyairline-system.git
cd skyairline-system/backend
```

### 2. Compilar el proyecto
```bash
mvn clean package -DskipTests
```

Esto genera el JAR ejecutable en `target/skyairlines-desktop-2.4.1.jar`.

### 3. Crear la base de datos
```bash
psql -U postgres -c "CREATE DATABASE skyairline_db;"
psql -U postgres -d skyairline_db -f src/main/resources/db/schema.sql
```

Si no recuerdas la contrasena de postgres, usa pgAdmin para crear la BD y ejecutar el script SQL manualmente.

### 4. Crear el usuario administrador
```bash
psql -U postgres -d skyairline_db -f src/main/resources/db/setup_admin.sql
```

O ejecutar manualmente en PostgreSQL:
```sql
INSERT INTO usuarios (email, password_hash, rol, activo)
VALUES ('admin@skyairline.com', '$2a$10$KDnGmk2dsaxHXreaYiiK8OViv5chk720y0HB3FaS74nIVtMNEAUcq', 'ADMINISTRADOR', TRUE);
```

### 5. Ejecutar la aplicacion
```bash
java -jar target/skyairlines-desktop-2.4.1.jar
```

### 6. Iniciar sesion
- **Usuario:** admin@skyairline.com
- **Contrasena:** admin123

## Modulos de la Aplicacion

| Modulo | Descripcion |
|--------|-------------|
| **Dashboard** | Panel de control con metricas en tiempo real y alertas operativas |
| **Vuelos** | Gestion completa de vuelos (Crear, Editar, Eliminar, Ver detalles) |
| **Detalle de Vuelo** | Informacion de ruta, aeronave, tripulacion y navegacion a sub-modulos |
| **Equipaje** | Trazabilidad de equipaje con flujo de estados y historial de auditoria |
| **Asientos** | Inventario en vivo por categoria con simulador de cancelacion |
| **Pasajeros** | Roster de pasajeros por vuelo (solo lectura) |
| **Clientes** | ABM completo de clientes con usuario asociado |
| **Personal** | ABM completo de empleados con roles |
| **Usuarios** | Gestion de usuarios del sistema con control de acceso |
| **Reportes** | Modulo en construccion (Q1 2027) |

## Funcionalidades Clave

- **Concurrencia Optimista:** Control de version en `vuelo_asientos` para evitar sobreventa de asientos entre el desktop y la plataforma web
- **Sincronizacion en Tiempo Real:** Cada operacion CRUD se refleja inmediatamente en la base de datos
- **Autenticacion BCrypt:** Contrasenas hasheadas con BCrypt (cost factor 10)
- **Arquitectura MVC+DAO:** Capas completamente desacopladas para facil mantenimiento
- **JTable Dinamico:** Todos los componentes de tabla usan AbstractTableModel con refresh automatico desde la base de datos

## Configuracion de Base de Datos

Los parametros de conexion estan en `ConexionBD.java`:
```
Host: localhost
Port: 5432
Database: skyairline_db
User: postgres
Password: 123123123
```

Para cambiar estas credenciales, editar el archivo `src/main/java/com/skyairlines/config/ConexionBD.java`.

## Solucion de Problemas

| Error | Solucion |
|-------|----------|
| `No se pudo conectar a la base de datos` | Verificar que PostgreSQL este corriendo en puerto 5432 |
| `Error: database "skyairline_db" does not exist` | Ejecutar `CREATE DATABASE skyairline_db;` |
| `Credenciales incorrectas` | Verificar que el usuario admin fue creado con el script `setup_admin.sql` |
| `ClassNotFoundException: org.postgresql.Driver` | Recompilar con `mvn clean package -DskipTests` |
| Pantalla negra o sin respuesta | Verificar que JDK 17+ este instalado: `java -version` |

## Tecnologias Utilizadas

- Java 17+
- Java Swing (UI)
- PostgreSQL (Base de datos)
- JDBC (Conexion a BD)
- BCrypt via jbcrypt (Seguridad)
- Maven (Build tool)
- FlatLaf (Look and Feel moderno)

## Licencia

Proyecto academico - Sky Airlines Peru
