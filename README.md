# Sky Airlines Peru - Backend

Aplicacion de escritorio para gestion administrativa de vuelos, pasajeros, equipaje y personal.  
Arquitectura MVC + DAO, PostgreSQL con JDBC, Swing con FlatLaf.

---

## Requisitos

| Componente | Version minima |
|-----------|---------------|
| JDK       | 17            |
| Maven     | 3.8           |
| PostgreSQL| 14            |

---

## Instalacion y Ejecucion

### 1. Clonar el repositorio
```bash
git clone https://github.com/Feerth/skyairline-system.git
cd skyairline-system/backend
```

### 2. Compilar
```bash
mvn clean package -DskipTests
```

### 3. Base de Datos

Crear la base de datos, tablas y datos de prueba:
```bash
psql -U postgres -c "CREATE DATABASE skyairline_db;"
psql -U postgres -d skyairline_db -f src/main/resources/db/schema.sql
psql -U postgres -d skyairline_db -f src/main/resources/db/seed_data.sql
```

> **Si su contrasena de PostgreSQL no es `postgres`:**  
> Editar `src/main/resources/db/config.properties` y cambiar:
> ```properties
> db.password=SU_CONTRASENA
> ```

### 4. Ejecutar la aplicacion
```bash
java -jar target/skyairlines-desktop-2.4.1.jar
```

---

## Credenciales de usuario

La aplicacion viene con usuarios de prueba precargados:

| Usuario                    | Contrasena | Rol             |
|---------------------------|-----------|-----------------|
| admin@skyairline.com       | admin123  | ADMINISTRADOR   |
| operaciones@skyairline.com | opera123  | OPERACIONES     |
| maria.garcia@mail.com      | maria123  | CLIENTE         |
| juan.lopez@mail.com        | juan123   | CLIENTE         |

### Roles y permisos
- **ADMINISTRADOR** - Acceso total a todos los modulos
- **OPERACIONES** - Gestion de vuelos, equipaje, asientos y pasajeros
- **CLIENTE** - Solo puede consultar vuelos y sus propias reservas

---

## Modulos de la aplicacion

| Modulo       | Funcionalidad |
|-------------|--------------|
| **Dashboard**    | Metricas en tiempo real: vuelos por estado, equipaje, alertas operativas |
| **Vuelos**       | CRUD completo con detalles de ruta y aeronave, simulador de cancelacion |
| **Equipaje**     | Trazabilidad completa con flujo de estados y historial |
| **Asientos**     | Inventario en vivo por vuelo, mapa de asientos, cancelacion |
| **Pasajeros**    | Lista de pasajeros por vuelo (solo lectura) |
| **Clientes**     | ABM con usuario asociado |
| **Personal**     | ABM de empleados con roles |
| **Usuarios**     | Gestion de usuarios del sistema (solo admin) |
| **Reportes**     | En construccion |

---

## Datos de prueba incluidos

El script `seed_data.sql` crea:

- **6 aeropuertos peruanos**: Lima, Chiclayo, Cusco, Arequipa, Trujillo, Piura
- **5 rutas**: LIM-CIX, LIM-CUS, LIM-AQP, CIX-LIM, LIM-TRU
- **3 aeronaves**: 2x Airbus A320 (160 asientos) + 1x Airbus A319 (120 asientos)
- **~440 asientos** pre-cargados por clase (ECONOMICA, EJECUTIVA, PRIMERA)
- **5 vuelos** con estados variados: EN_VUELO, PROGRAMADO, COMPLETADO, RETRASADO
- **8 pasajeros** con documentos y datos personales
- **6 boletos** emitidos con etiquetas de equipaje
- **6 equipajes** en distintos estados: REGISTRADO, EMBARCADO, DISTRIBUCION_CINTA, DESCARGADO, ENTREGADO
- **4 alertas operativas**: 1 CRITICO, 1 ADVERTENCIA, 2 INFO

---

## Arquitectura del proyecto

```
com.skyairline
├── Main.java                          # Punto de entrada
├── config/
│   └── ConexionBD.java                # Conexion a PostgreSQL (DriverManager)
├── model/
│   ├── entity/                        # 16 entidades POJO
│   ├── enums/                         # 5 enums (roles, estados, etc.)
│   └── tablemodel/                    # 9 TableModel customizados
├── dao/
│   ├── api/                           # 16 interfaces DAO
│   └── impl/                          # 15 implementaciones JDBC
├── controller/                        # Controladores de negocio
├── view/
│   ├── auth/LoginFrame.java           # Pantalla de login (BCrypt)
│   ├── main/MainFrame.java            # Ventana principal con CardLayout
│   ├── components/                    # Componentes reutilizables
│   └── panels/                        # 11 paneles de navegacion
└── resources/db/
    ├── config.properties              # Credenciales de BD
    ├── schema.sql                     # Estructura de tablas + triggers
    └── seed_data.sql                  # Datos de prueba
```

---

## Datos de conexion por defecto

| Parametro  | Valor              |
|-----------|-------------------|
| Host       | localhost          |
| Puerto     | 5432               |
| Base datos | skyairline_db      |
| Usuario    | postgres           |
| Contrasena | postgres           |

---

## Solucion de problemas

### Error: "password authentication failed"
Editar `config.properties` y poner la contrasena correcta de PostgreSQL.

### Error: "database does not exist"
Ejecutar los scripts SQL en orden: `schema.sql` primero, luego `seed_data.sql`.

### Error: "relation does not exist"
Asegurarse de ejecutar `schema.sql` antes de `seed_data.sql`.

### La aplicacion no encuentra el JAR
Ejecutar desde la carpeta `backend` despues de compilar:
```bash
java -jar target/skyairlines-desktop-2.4.1.jar
```
