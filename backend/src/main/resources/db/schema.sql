-- ============================================================================
-- SCRIPT DE BASE DE DATOS DEFINITIVO: SKY AIRLINES PERU
-- MOTOR: POSTGRESQL
-- ============================================================================

BEGIN;

-- ============================================================================
-- 1. TIPOS ENUM (DOMINIOS DE DATOS)
-- ============================================================================
CREATE TYPE enum_rol_usuario AS ENUM ('ADMINISTRADOR', 'OPERACIONES', 'CLIENTE');
CREATE TYPE enum_estado_asiento_vuelo AS ENUM ('DISPONIBLE', 'RESERVADO', 'VENDIDO', 'BLOQUEADO');
CREATE TYPE enum_estado_reserva AS ENUM ('PENDIENTE', 'CONFIRMADA', 'EXPIRADA', 'CANCELADA');
CREATE TYPE enum_estado_equipaje AS ENUM ('REGISTRADO', 'CONTROL_SEGURIDAD', 'EMBARCADO', 'DESCARGADO', 'DISTRIBUCION_CINTA', 'ENTREGADO', 'RETENIDO');
CREATE TYPE enum_categoria_equipaje AS ENUM ('LIGERO', 'NORMAL', 'PESADO');

-- ============================================================================
-- 2. INFRAESTRUCTURA AERONAUTICA Y GEOGRAFICA
-- ============================================================================
CREATE TABLE aeropuertos (
    id SERIAL PRIMARY KEY,
    codigo_iata VARCHAR(3) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    ciudad VARCHAR(100) NOT NULL,
    pais VARCHAR(100) NOT NULL,
    zona_horaria VARCHAR(50) NOT NULL DEFAULT 'America/Lima'
);

CREATE TABLE rutas (
    id SERIAL PRIMARY KEY,
    codigo_ruta VARCHAR(10) UNIQUE NOT NULL,
    id_aeropuerto_origen INT NOT NULL REFERENCES aeropuertos(id) ON DELETE RESTRICT,
    id_aeropuerto_destino INT NOT NULL REFERENCES aeropuertos(id) ON DELETE RESTRICT,
    duracion_estimada_min INT NOT NULL CHECK (duracion_estimada_min > 0),
    CONSTRAINT chk_rutas_distintas CHECK (id_aeropuerto_origen <> id_aeropuerto_destino)
);

CREATE TABLE aeronaves (
    id SERIAL PRIMARY KEY,
    matricula VARCHAR(15) UNIQUE NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    capacidad_pasajeros INT NOT NULL CHECK (capacidad_pasajeros > 0),
    estado VARCHAR(20) DEFAULT 'ACTIVO' CHECK (estado IN ('ACTIVO', 'MANTENIMIENTO', 'INACTIVO'))
);

CREATE TABLE asientos_aeronave (
    id SERIAL PRIMARY KEY,
    id_aeronave INT NOT NULL REFERENCES aeronaves(id) ON DELETE CASCADE,
    codigo_asiento VARCHAR(5) NOT NULL,
    clase VARCHAR(20) NOT NULL CHECK (clase IN ('ECONOMICA', 'EJECUTIVA', 'PRIMERA')),
    es_emergencia BOOLEAN DEFAULT FALSE,
    CONSTRAINT uq_aeronave_codigo_asiento UNIQUE (id_aeronave, codigo_asiento)
);

-- ============================================================================
-- 3. USUARIOS, SEGURIDAD E IDENTIDADES (RBAC + SEGREGACION DE PASAJEROS)
-- ============================================================================
CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    rol enum_rol_usuario NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    id_usuario INT UNIQUE REFERENCES usuarios(id) ON DELETE CASCADE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    doc_identidad VARCHAR(20) UNIQUE NOT NULL,
    telefono VARCHAR(20)
);

CREATE TABLE empleados (
    id SERIAL PRIMARY KEY,
    id_usuario INT UNIQUE NOT NULL REFERENCES usuarios(id) ON DELETE RESTRICT,
    codigo_empleado VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    cargo VARCHAR(50) NOT NULL
);

CREATE TABLE pasajeros (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    doc_identidad VARCHAR(20) UNIQUE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    nacionalidad VARCHAR(50) NOT NULL
);

-- ============================================================================
-- 4. PLANIFICACION DE VUELOS E INVENTARIO EN VIVO (CONCURRENCIA OPTIMISTA)
-- ============================================================================
CREATE TABLE vuelos (
    id SERIAL PRIMARY KEY,
    codigo_vuelo VARCHAR(10) UNIQUE NOT NULL,
    id_ruta INT NOT NULL REFERENCES rutas(id) ON DELETE RESTRICT,
    id_aeronave INT NOT NULL REFERENCES aeronaves(id) ON DELETE RESTRICT,
    fecha_salida_programada TIMESTAMPTZ NOT NULL,
    fecha_llegada_programada TIMESTAMPTZ NOT NULL,
    estado VARCHAR(20) DEFAULT 'PROGRAMADO' CHECK (estado IN ('PROGRAMADO', 'EMBARCANDO', 'EN_VUELO', 'COMPLETADO', 'CANCELADO', 'RETRASADO')),
    CONSTRAINT chk_fechas_vuelo CHECK (fecha_llegada_programada > fecha_salida_programada)
);

CREATE TABLE vuelo_asientos (
    id SERIAL PRIMARY KEY,
    id_vuelo INT NOT NULL REFERENCES vuelos(id) ON DELETE CASCADE,
    id_asiento_aeronave INT NOT NULL REFERENCES asientos_aeronave(id) ON DELETE RESTRICT,
    estado enum_estado_asiento_vuelo DEFAULT 'DISPONIBLE',
    precio NUMERIC(10,2) NOT NULL CHECK (precio >= 0),
    version INT NOT NULL DEFAULT 1,
    CONSTRAINT uq_vuelo_asiento_instancia UNIQUE (id_vuelo, id_asiento_aeronave)
);

-- ============================================================================
-- 5. TRANSACCIONES, RESERVAS Y EMISION DE BOLETOS
-- ============================================================================
CREATE TABLE compras (
    id SERIAL PRIMARY KEY,
    id_cliente INT NOT NULL REFERENCES clientes(id) ON DELETE RESTRICT,
    codigo_transaccion VARCHAR(50) UNIQUE NOT NULL,
    monto_total NUMERIC(10,2) NOT NULL CHECK (monto_total >= 0),
    estado_pago VARCHAR(20) DEFAULT 'PENDIENTE' CHECK (estado_pago IN ('PENDIENTE', 'APROBADO', 'RECHAZADO', 'REEMBOLSADO')),
    fecha_compra TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE reservas (
    id SERIAL PRIMARY KEY,
    id_compra INT REFERENCES compras(id) ON DELETE SET NULL,
    id_vuelo_asiento INT UNIQUE NOT NULL REFERENCES vuelo_asientos(id) ON DELETE RESTRICT,
    id_pasajero INT NOT NULL REFERENCES pasajeros(id) ON DELETE RESTRICT,
    expira_en TIMESTAMPTZ NOT NULL,
    estado enum_estado_reserva DEFAULT 'PENDIENTE'
);

CREATE TABLE boletos (
    id SERIAL PRIMARY KEY,
    codigo_eticket VARCHAR(20) UNIQUE NOT NULL,
    id_reserva INT UNIQUE NOT NULL REFERENCES reservas(id) ON DELETE RESTRICT,
    id_vuelo_asiento INT UNIQUE NOT NULL REFERENCES vuelo_asientos(id) ON DELETE RESTRICT,
    id_pasajero INT NOT NULL REFERENCES pasajeros(id) ON DELETE RESTRICT,
    estado VARCHAR(20) DEFAULT 'EMITIDO' CHECK (estado IN ('EMITIDO', 'CHECKED_IN', 'ABORDADO', 'CANCELADO')),
    fecha_emision TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 6. TRAZABILIDAD LOGISTICA DE EQUIPAJES
-- ============================================================================
CREATE TABLE equipajes (
    id SERIAL PRIMARY KEY,
    id_boleto INT NOT NULL REFERENCES boletos(id) ON DELETE RESTRICT,
    codigo_etiqueta_bag VARCHAR(50) UNIQUE NOT NULL,
    categoria_peso enum_categoria_equipaje NOT NULL,
    peso_kg NUMERIC(5,2) NOT NULL CHECK (peso_kg > 0),
    estado_actual enum_estado_equipaje DEFAULT 'REGISTRADO',
    cinta_carrusel_actual VARCHAR(20)
);

CREATE TABLE historial_equipaje (
    id SERIAL PRIMARY KEY,
    id_equipaje INT NOT NULL REFERENCES equipajes(id) ON DELETE CASCADE,
    estado enum_estado_equipaje NOT NULL,
    ubicacion_aeropuerto_id INT NOT NULL REFERENCES aeropuertos(id) ON DELETE RESTRICT,
    cinta_carrusel VARCHAR(20),
    observaciones TEXT,
    id_empleado INT REFERENCES empleados(id) ON DELETE SET NULL,
    fecha_registro TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 7. TABLA DE ALERTAS OPERATIVAS (para el Dashboard)
-- ============================================================================
CREATE TABLE alertas_operativas (
    id SERIAL PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descripcion TEXT NOT NULL,
    nivel VARCHAR(20) DEFAULT 'INFO' CHECK (nivel IN ('INFO', 'ADVERTENCIA', 'CRITICO')),
    activa BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- 8. OPTIMIZACION E INDEXACION ESTRATEGICA (ALTA CONCURRENCIA)
-- ============================================================================
CREATE INDEX idx_vuelos_busqueda ON vuelos(id_ruta, fecha_salida_programada, estado);
CREATE INDEX idx_vuelo_asientos_disp ON vuelo_asientos(id_vuelo, estado);
CREATE INDEX idx_reservas_expiracion ON reservas(expira_en) WHERE estado = 'PENDIENTE';
CREATE INDEX idx_equipaje_rastreo ON equipajes(codigo_etiqueta_bag);
CREATE INDEX idx_historial_equipaje_fch ON historial_equipaje(id_equipaje, fecha_registro DESC);

-- ============================================================================
-- 9. AUTOMATIZACIONES MEDIANTE TRIGGERS (LOGICA OPERATIVA)
-- ============================================================================

-- A) Generador Automatico de Mapa de Asientos por Vuelo
CREATE OR REPLACE FUNCTION trg_generar_asientos_vuelo()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO vuelo_asientos (id_vuelo, id_asiento_aeronave, estado, precio)
    SELECT NEW.id, a.id, 'DISPONIBLE', 150.00
    FROM asientos_aeronave a
    WHERE a.id_aeronave = NEW.id_aeronave;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_post_crear_vuelo
    AFTER INSERT ON vuelos
    FOR EACH ROW
    EXECUTE FUNCTION trg_generar_asientos_vuelo();

-- B) Auditoria Historica Automatizada de Trazabilidad de Equipaje
CREATE OR REPLACE FUNCTION trg_registrar_historial_equipaje()
RETURNS TRIGGER AS $$
DECLARE
    v_aeropuerto_id INT;
BEGIN
    SELECT r.id_aeropuerto_origen INTO v_aeropuerto_id
    FROM boletos b
    JOIN vuelo_asientos va ON b.id_vuelo_asiento = va.id
    JOIN vuelos v ON va.id_vuelo = v.id
    JOIN rutas r ON v.id_ruta = r.id
    WHERE b.id = NEW.id_boleto;

    INSERT INTO historial_equipaje (
        id_equipaje, estado, ubicacion_aeropuerto_id,
        cinta_carrusel, observaciones, fecha_registro
    ) VALUES (
        NEW.id, NEW.estado_actual, v_aeropuerto_id,
        NEW.cinta_carrusel_actual,
        'Cambio logistico de estado registrado por el personal operativo de counter/pista.',
        CURRENT_TIMESTAMP
    );
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER tg_equipaje_audit
    AFTER INSERT OR UPDATE OF estado_actual, cinta_carrusel_actual ON equipajes
    FOR EACH ROW
    EXECUTE FUNCTION trg_registrar_historial_equipaje();

COMMIT;