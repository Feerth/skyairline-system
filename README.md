# Sky Airline Perú — Sistema de escritorio + Web

Proyecto de curso (UTP) para una aerolínea nacional peruana. Un solo backend (API
REST) alimenta **dos clientes distintos**: un sistema de escritorio en JavaFX para
uso interno del personal, y una web pública para que los pasajeros vean vuelos y
reserven pasajes.

```
┌─────────────────────┐        ┌──────────────────────────────┐
│  WEB (HTML/CSS/JS)   │──────▶ │                                │
│  búsqueda y reserva  │        │   BACKEND (Spring Boot)       │
│  de vuelos           │        │   API REST + base de datos    │
└─────────────────────┘        │   /api/aeropuertos            │
┌─────────────────────┐        │   /api/vuelos                  │
│  DESKTOP (JavaFX)     │──────▶│   /api/pasajeros               │
│  panel administrativo │        │   /api/reservas                │
│  CRUD completo        │        │   /api/pagos                   │
└─────────────────────┘        └──────────────────────────────┘
```

## ¿Por qué esta arquitectura?

- **Un solo backend, dos clientes**: el personal de la aerolínea administra todo
  (aeropuertos, vuelos, pasajeros, reservas, pagos) desde el sistema de escritorio,
  mientras los pasajeros ven y reservan vuelos desde la web — ambos consumen la
  misma API REST, evitando duplicar lógica de negocio.
- **Sistema de escritorio (JavaFX)** = "back office": CRUD completo de las 5
  entidades, pensado para el personal de la aerolínea (crear rutas, programar
  vuelos, gestionar reservas y pagos).
- **Web (HTML/CSS/JS)** = portal público de vuelos con reserva en línea.

## Entidades y relaciones

`Aeropuerto` (código IATA, ciudad, país) — un vuelo tiene un aeropuerto de
**origen** y uno de **destino**. `Vuelo` (número, fecha, horas, precio, asientos,
estado). `Pasajero` (datos personales). `Reserva` (pasajero + vuelo + número de
pasajes + estado). `Pago` (asociado a una reserva).

## Arranque rápido (un solo comando)

**Linux / macOS (Fedora incluido):**
```bash
chmod +x start.sh   # solo la primera vez
./start.sh
```

**Windows:**
```
start.bat
```

Requisitos: JDK 17+, Maven y Python 3 accesibles desde la terminal.

## 1. Backend (`/backend`) — Spring Boot

```bash
cd backend
mvn spring-boot:run
```

Corre en `http://localhost:8080`. Usa H2 en memoria con datos de ejemplo ya
cargados: 8 aeropuertos peruanos y 8 vuelos nacionales de muestra (Lima, Cusco,
Arequipa, Piura, Trujillo, Iquitos, Tacna, Chiclayo). Consola H2 en
`http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:skyairline`, usuario
`sa`, sin contraseña).

Para usar MySQL en vez de H2, descomenta el bloque correspondiente en
`application.properties` y la dependencia `mysql-connector-j` en el `pom.xml`.

## 2. Sistema de escritorio (`/desktop`) — JavaFX

```bash
cd desktop
mvn javafx:run
```

Pestañas: Aeropuertos, Vuelos, Pasajeros, Reservas y Pagos, cada una con CRUD
completo (Crear / Editar / Eliminar / Refrescar) contra la API REST.

## 3. Web (`/web`) — HTML/CSS/JS

```bash
cd web
python3 -m http.server 5500
# abre http://localhost:5500
```

Muestra los vuelos disponibles (filtrables por ciudad destino) con un formulario
de reserva que registra al pasajero y crea la reserva en la misma base de datos
que usa el sistema de escritorio.

## Notas para la sustentación

- **Patrón Template Method**: `CrudPanel<T>` (en `desktop/.../ui/CrudPanel.java`)
  define el flujo común de un CRUD (cargar tabla, crear, editar, eliminar, mostrar
  errores); cada panel concreto solo implementa las partes específicas de su
  entidad.
- **Arquitectura por capas en el backend**: `model` (entidades JPA) → `repository`
  (Spring Data JPA) → `controller` (REST).
- El total de la reserva se calcula en el backend (precio del vuelo × número de
  pasajes), manteniendo una sola fuente de verdad.
