-- ============================================================================
-- SCRIPT DE DATOS DE PRUEBA: SKY AIRLINES PERU
-- Incluye: Usuarios, Aeropuertos, Rutas, Aeronaves, Vuelos, Pasajeros,
--          Boletos, Equipajes, Alertas y todo lo necesario para usar el sistema.
-- ============================================================================
-- Ejecutar: psql -U postgres -d skyairline_db -f seed_data.sql
-- ============================================================================

-- ============================================================================
-- 1. USUARIOS
-- ============================================================================
INSERT INTO usuarios (email, password_hash, rol, activo) VALUES
('admin@skyairline.com',          '$2a$10$K0VEWk7YUzbpIziUB7VSVuZNOf2o/b24i9fNfShWo9j/CAHpMCici', 'ADMINISTRADOR', TRUE),
('operaciones@skyairline.com',    '$2a$10$e9xjNKvCXuf8hbhmKm3MOelpw3pJHuyAdxeX9.oADJ7GkJ7Q/48fm', 'OPERACIONES',   TRUE),
('carlos.mendoza@skyairline.com', '$2a$10$6J/DKeq7QOskN5UYeMwdv.YOANQRsXZ8Jcan4BZeNqBPYWS/aImLW', 'OPERACIONES',   TRUE),
('maria.garcia@mail.com',         '$2a$10$b6LzjF33lRGHACIplrXw2e9DWlK9huzJH8uyCrAB1YnMXg8EMT/ai', 'CLIENTE',       TRUE),
('juan.lopez@mail.com',           '$2a$10$ybYS9EeQu74WOagicE2K8e0VJE3HtqxfygKZUjwjMqdPy9c/dPGfu', 'CLIENTE',       TRUE);

-- ============================================================================
-- 2. CLIENTES
-- ============================================================================
INSERT INTO clientes (id_usuario, nombre, apellido, doc_identidad, telefono) VALUES
(4, 'Maria',   'Garcia',    '40123456', '999111222'),
(5, 'Juan',    'Lopez',     '40789012', '988777666');

-- ============================================================================
-- 3. EMPLEADOS
-- ============================================================================
INSERT INTO empleados (id_usuario, codigo_empleado, nombre, apellido, cargo) VALUES
(2, 'EMP001', 'Carlos',  'Mendoza', 'Piloto'),
(3, 'EMP002', 'Ana',     'Torres',  'Copiloto'),
(1, 'EMP003', 'Luis',    'Ramirez', 'Azafata');

-- ============================================================================
-- 4. AEROPUERTOS
-- ============================================================================
INSERT INTO aeropuertos (codigo_iata, nombre, ciudad, pais, zona_horaria) VALUES
('LIM', 'Aeropuerto Internacional Jorge Chavez',       'Lima',     'Peru', 'America/Lima'),
('CIX', 'Aeropuerto Internacional Cap. Jose A. Quinones', 'Chiclayo', 'Peru', 'America/Lima'),
('CUS', 'Aeropuerto Internacional Alejandro Velasco Astete', 'Cusco',   'Peru', 'America/Lima'),
('AQP', 'Aeropuerto Internacional Rodriguez Ballon',    'Arequipa', 'Peru', 'America/Lima'),
('TRU', 'Aeropuerto Internacional Cap. FAP Carlos Martinez', 'Trujillo', 'Peru', 'America/Lima'),
('PIU', 'Aeropuerto Internacional Cap. FAP Guillermo Concha', 'Piura',  'Peru', 'America/Lima');

-- ============================================================================
-- 5. RUTAS
-- ============================================================================
INSERT INTO rutas (codigo_ruta, id_aeropuerto_origen, id_aeropuerto_destino, duracion_estimada_min) VALUES
('LIM-CIX', 1, 2, 90),
('LIM-CUS', 1, 3, 110),
('LIM-AQP', 1, 4, 100),
('CIX-LIM', 2, 1, 90),
('LIM-TRU', 1, 5, 85);

-- ============================================================================
-- 6. AERONAVES
-- ============================================================================
INSERT INTO aeronaves (matricula, modelo, capacidad_pasajeros, estado) VALUES
('OC-ABQ', 'Airbus A320', 160, 'ACTIVO'),
('OC-XYZ', 'Airbus A320', 160, 'ACTIVO'),
('OC-DEF', 'Airbus A319', 120, 'ACTIVO');

-- ============================================================================
-- 7. ASIENTOS DE AERONAVE (pre-cargar antes de vuelos)
-- ============================================================================

-- A320 OC-ABQ (id=1): 100 ECONOMICA + 40 EJECUTIVA + 20 PRIMERA = 160
-- Fila 1-5: PRIMERA (1A-1F, 2A-2F... 5A-5F = 20 asientos)
INSERT INTO asientos_aeronave (id_aeronave, codigo_asiento, clase, es_emergencia) VALUES
(1, '1A', 'PRIMERA', FALSE), (1, '1B', 'PRIMERA', FALSE), (1, '1C', 'PRIMERA', FALSE), (1, '1D', 'PRIMERA', FALSE),
(1, '2A', 'PRIMERA', FALSE), (1, '2B', 'PRIMERA', FALSE), (1, '2C', 'PRIMERA', FALSE), (1, '2D', 'PRIMERA', FALSE),
(1, '3A', 'PRIMERA', FALSE), (1, '3B', 'PRIMERA', FALSE), (1, '3C', 'PRIMERA', FALSE), (1, '3D', 'PRIMERA', FALSE),
(1, '4A', 'PRIMERA', FALSE), (1, '4B', 'PRIMERA', FALSE), (1, '4C', 'PRIMERA', FALSE), (1, '4D', 'PRIMERA', FALSE),
(1, '5A', 'PRIMERA', FALSE), (1, '5B', 'PRIMERA', FALSE), (1, '5C', 'PRIMERA', FALSE), (1, '5D', 'PRIMERA', FALSE);

-- Fila 6-15: EJECUTIVA (40 asientos, 4 por fila)
INSERT INTO asientos_aeronave (id_aeronave, codigo_asiento, clase, es_emergencia) VALUES
(1, '6A', 'EJECUTIVA', FALSE), (1, '6B', 'EJECUTIVA', FALSE), (1, '6C', 'EJECUTIVA', FALSE), (1, '6D', 'EJECUTIVA', FALSE),
(1, '7A', 'EJECUTIVA', FALSE), (1, '7B', 'EJECUTIVA', FALSE), (1, '7C', 'EJECUTIVA', FALSE), (1, '7D', 'EJECUTIVA', FALSE),
(1, '8A', 'EJECUTIVA', FALSE), (1, '8B', 'EJECUTIVA', FALSE), (1, '8C', 'EJECUTIVA', FALSE), (1, '8D', 'EJECUTIVA', FALSE),
(1, '9A', 'EJECUTIVA', FALSE), (1, '9B', 'EJECUTIVA', FALSE), (1, '9C', 'EJECUTIVA', FALSE), (1, '9D', 'EJECUTIVA', FALSE),
(1, '10A', 'EJECUTIVA', FALSE), (1, '10B', 'EJECUTIVA', FALSE), (1, '10C', 'EJECUTIVA', FALSE), (1, '10D', 'EJECUTIVA', FALSE),
(1, '11A', 'EJECUTIVA', FALSE), (1, '11B', 'EJECUTIVA', FALSE), (1, '11C', 'EJECUTIVA', FALSE), (1, '11D', 'EJECUTIVA', FALSE),
(1, '12A', 'EJECUTIVA', FALSE), (1, '12B', 'EJECUTIVA', FALSE), (1, '12C', 'EJECUTIVA', FALSE), (1, '12D', 'EJECUTIVA', FALSE),
(1, '13A', 'EJECUTIVA', FALSE), (1, '13B', 'EJECUTIVA', FALSE), (1, '13C', 'EJECUTIVA', FALSE), (1, '13D', 'EJECUTIVA', FALSE),
(1, '14A', 'EJECUTIVA', FALSE), (1, '14B', 'EJECUTIVA', FALSE), (1, '14C', 'EJECUTIVA', FALSE), (1, '14D', 'EJECUTIVA', FALSE),
(1, '15A', 'EJECUTIVA', FALSE), (1, '15B', 'EJECUTIVA', FALSE), (1, '15C', 'EJECUTIVA', FALSE), (1, '15D', 'EJECUTIVA', FALSE);

-- Fila 16-40: ECONOMICA (100 asientos, 4 por fila)
INSERT INTO asientos_aeronave (id_aeronave, codigo_asiento, clase, es_emergencia) VALUES
(1, '16A', 'ECONOMICA', FALSE), (1, '16B', 'ECONOMICA', FALSE), (1, '16C', 'ECONOMICA', FALSE), (1, '16D', 'ECONOMICA', FALSE),
(1, '17A', 'ECONOMICA', FALSE), (1, '17B', 'ECONOMICA', FALSE), (1, '17C', 'ECONOMICA', FALSE), (1, '17D', 'ECONOMICA', FALSE),
(1, '18A', 'ECONOMICA', FALSE), (1, '18B', 'ECONOMICA', FALSE), (1, '18C', 'ECONOMICA', FALSE), (1, '18D', 'ECONOMICA', FALSE),
(1, '19A', 'ECONOMICA', FALSE), (1, '19B', 'ECONOMICA', FALSE), (1, '19C', 'ECONOMICA', FALSE), (1, '19D', 'ECONOMICA', FALSE),
(1, '20A', 'ECONOMICA', FALSE), (1, '20B', 'ECONOMICA', FALSE), (1, '20C', 'ECONOMICA', FALSE), (1, '20D', 'ECONOMICA', FALSE),
(1, '21A', 'ECONOMICA', FALSE), (1, '21B', 'ECONOMICA', FALSE), (1, '21C', 'ECONOMICA', FALSE), (1, '21D', 'ECONOMICA', FALSE),
(1, '22A', 'ECONOMICA', FALSE), (1, '22B', 'ECONOMICA', FALSE), (1, '22C', 'ECONOMICA', FALSE), (1, '22D', 'ECONOMICA', FALSE),
(1, '23A', 'ECONOMICA', FALSE), (1, '23B', 'ECONOMICA', FALSE), (1, '23C', 'ECONOMICA', FALSE), (1, '23D', 'ECONOMICA', FALSE),
(1, '24A', 'ECONOMICA', FALSE), (1, '24B', 'ECONOMICA', FALSE), (1, '24C', 'ECONOMICA', FALSE), (1, '24D', 'ECONOMICA', FALSE),
(1, '25A', 'ECONOMICA', FALSE), (1, '25B', 'ECONOMICA', FALSE), (1, '25C', 'ECONOMICA', FALSE), (1, '25D', 'ECONOMICA', FALSE),
(1, '26A', 'ECONOMICA', FALSE), (1, '26B', 'ECONOMICA', FALSE), (1, '26C', 'ECONOMICA', FALSE), (1, '26D', 'ECONOMICA', FALSE),
(1, '27A', 'ECONOMICA', FALSE), (1, '27B', 'ECONOMICA', FALSE), (1, '27C', 'ECONOMICA', FALSE), (1, '27D', 'ECONOMICA', FALSE),
(1, '28A', 'ECONOMICA', FALSE), (1, '28B', 'ECONOMICA', FALSE), (1, '28C', 'ECONOMICA', FALSE), (1, '28D', 'ECONOMICA', FALSE),
(1, '29A', 'ECONOMICA', FALSE), (1, '29B', 'ECONOMICA', FALSE), (1, '29C', 'ECONOMICA', FALSE), (1, '29D', 'ECONOMICA', FALSE),
(1, '30A', 'ECONOMICA', FALSE), (1, '30B', 'ECONOMICA', FALSE), (1, '30C', 'ECONOMICA', FALSE), (1, '30D', 'ECONOMICA', FALSE),
(1, '31A', 'ECONOMICA', FALSE), (1, '31B', 'ECONOMICA', FALSE), (1, '31C', 'ECONOMICA', FALSE), (1, '31D', 'ECONOMICA', FALSE),
(1, '32A', 'ECONOMICA', FALSE), (1, '32B', 'ECONOMICA', FALSE), (1, '32C', 'ECONOMICA', FALSE), (1, '32D', 'ECONOMICA', FALSE),
(1, '33A', 'ECONOMICA', FALSE), (1, '33B', 'ECONOMICA', FALSE), (1, '33C', 'ECONOMICA', FALSE), (1, '33D', 'ECONOMICA', FALSE),
(1, '34A', 'ECONOMICA', FALSE), (1, '34B', 'ECONOMICA', FALSE), (1, '34C', 'ECONOMICA', FALSE), (1, '34D', 'ECONOMICA', FALSE),
(1, '35A', 'ECONOMICA', FALSE), (1, '35B', 'ECONOMICA', FALSE), (1, '35C', 'ECONOMICA', FALSE), (1, '35D', 'ECONOMICA', FALSE),
(1, '36A', 'ECONOMICA', FALSE), (1, '36B', 'ECONOMICA', FALSE), (1, '36C', 'ECONOMICA', FALSE), (1, '36D', 'ECONOMICA', FALSE),
(1, '37A', 'ECONOMICA', FALSE), (1, '37B', 'ECONOMICA', FALSE), (1, '37C', 'ECONOMICA', FALSE), (1, '37D', 'ECONOMICA', FALSE),
(1, '38A', 'ECONOMICA', FALSE), (1, '38B', 'ECONOMICA', FALSE), (1, '38C', 'ECONOMICA', FALSE), (1, '38D', 'ECONOMICA', FALSE),
(1, '39A', 'ECONOMICA', FALSE), (1, '39B', 'ECONOMICA', FALSE), (1, '39C', 'ECONOMICA', FALSE), (1, '39D', 'ECONOMICA', FALSE),
(1, '40A', 'ECONOMICA', FALSE), (1, '40B', 'ECONOMICA', FALSE), (1, '40C', 'ECONOMICA', FALSE), (1, '40D', 'ECONOMICA', FALSE);

-- OC-XYZ (id=2): mismos asientos
INSERT INTO asientos_aeronave (id_aeronave, codigo_asiento, clase, es_emergencia)
SELECT 2, codigo_asiento, clase, es_emergencia FROM asientos_aeronave WHERE id_aeronave = 1;

-- OC-DEF A319 (id=3): 75 ECONOMICA + 30 EJECUTIVA + 15 PRIMERA = 120
-- PRIMERA (15): filas 1-4 (4 por fila) + fila 5 (3)
INSERT INTO asientos_aeronave (id_aeronave, codigo_asiento, clase, es_emergencia) VALUES
(3, '1A', 'PRIMERA', FALSE), (3, '1B', 'PRIMERA', FALSE), (3, '1C', 'PRIMERA', FALSE), (3, '1D', 'PRIMERA', FALSE),
(3, '2A', 'PRIMERA', FALSE), (3, '2B', 'PRIMERA', FALSE), (3, '2C', 'PRIMERA', FALSE), (3, '2D', 'PRIMERA', FALSE),
(3, '3A', 'PRIMERA', FALSE), (3, '3B', 'PRIMERA', FALSE), (3, '3C', 'PRIMERA', FALSE), (3, '3D', 'PRIMERA', FALSE),
(3, '4A', 'PRIMERA', FALSE), (3, '4B', 'PRIMERA', FALSE), (3, '4C', 'PRIMERA', FALSE);

-- EJECUTIVA (30): filas 5-11 (4 por fila)
INSERT INTO asientos_aeronave (id_aeronave, codigo_asiento, clase, es_emergencia) VALUES
(3, '5A', 'EJECUTIVA', FALSE), (3, '5B', 'EJECUTIVA', FALSE), (3, '5C', 'EJECUTIVA', FALSE), (3, '5D', 'EJECUTIVA', FALSE),
(3, '6A', 'EJECUTIVA', FALSE), (3, '6B', 'EJECUTIVA', FALSE), (3, '6C', 'EJECUTIVA', FALSE), (3, '6D', 'EJECUTIVA', FALSE),
(3, '7A', 'EJECUTIVA', FALSE), (3, '7B', 'EJECUTIVA', FALSE), (3, '7C', 'EJECUTIVA', FALSE), (3, '7D', 'EJECUTIVA', FALSE),
(3, '8A', 'EJECUTIVA', FALSE), (3, '8B', 'EJECUTIVA', FALSE), (3, '8C', 'EJECUTIVA', FALSE), (3, '8D', 'EJECUTIVA', FALSE),
(3, '9A', 'EJECUTIVA', FALSE), (3, '9B', 'EJECUTIVA', FALSE), (3, '9C', 'EJECUTIVA', FALSE), (3, '9D', 'EJECUTIVA', FALSE),
(3, '10A', 'EJECUTIVA', FALSE), (3, '10B', 'EJECUTIVA', FALSE), (3, '10C', 'EJECUTIVA', FALSE), (3, '10D', 'EJECUTIVA', FALSE),
(3, '11A', 'EJECUTIVA', FALSE), (3, '11B', 'EJECUTIVA', FALSE), (3, '11C', 'EJECUTIVA', FALSE), (3, '11D', 'EJECUTIVA', FALSE),
(3, '12A', 'EJECUTIVA', FALSE), (3, '12B', 'EJECUTIVA', FALSE);

-- ECONOMICA (75): filas 13-31 (4 por fila)
INSERT INTO asientos_aeronave (id_aeronave, codigo_asiento, clase, es_emergencia)
SELECT 3, 
       (13 + (s.i / 4))::TEXT || CHR(65 + (s.i % 4)),
       'ECONOMICA',
       FALSE
FROM generate_series(0, 74) AS s(i);

-- ============================================================================
-- 8. PASAJEROS
-- ============================================================================
INSERT INTO pasajeros (nombre, apellido, doc_identidad, fecha_nacimiento, nacionalidad) VALUES
('Carlos',  'Perez',     '10234567', '1985-03-15', 'Peruano'),
('Ana',     'Martinez',  '10345678', '1990-07-22', 'Peruano'),
('Roberto', 'Sanchez',   '10456789', '1978-11-10', 'Peruano'),
('Laura',   'Fernandez', '10567890', '1995-01-30', 'Peruano'),
('Pedro',   'Gutierrez', '10678901', '1988-09-05', 'Peruano'),
('Sofia',   'Torres',    '10789012', '1992-06-18', 'Peruano'),
('Miguel',  'Rivera',    '10890123', '1980-12-25', 'Peruano'),
('Camila',  'Vargas',    '10901234', '1998-04-12', 'Peruano');

-- ============================================================================
-- 9. PASAJEROS ADICIONALES (para clientes registrados)
-- ============================================================================
INSERT INTO pasajeros (nombre, apellido, doc_identidad, fecha_nacimiento, nacionalidad) VALUES
('Maria',   'Garcia',    '40123456', '1988-05-20', 'Peruano'),
('Juan',    'Lopez',     '40789012', '1992-08-14', 'Peruano');

-- ============================================================================
-- 10. VUELOS (fechas relativas: hoy y manana)
-- ============================================================================
-- SP101: LIM->CIX HOY EN_VUELO (despego hace 1 hora)
INSERT INTO vuelos (codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado)
VALUES ('SP101', 1, 1,
        CURRENT_TIMESTAMP - INTERVAL '1 hour',
        CURRENT_TIMESTAMP + INTERVAL '1 hour 30 minutes',
        'EN_VUELO');

-- SP205: LIM->CUS HOY PROGRAMADO (sale en 3 horas)
INSERT INTO vuelos (codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado)
VALUES ('SP205', 2, 2,
        CURRENT_TIMESTAMP + INTERVAL '3 hours',
        CURRENT_TIMESTAMP + INTERVAL '4 hours 50 minutes',
        'PROGRAMADO');

-- SP310: LIM->AQP HOY COMPLETADO (llego hace 2 horas)
INSERT INTO vuelos (codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado)
VALUES ('SP310', 3, 1,
        CURRENT_TIMESTAMP - INTERVAL '3 hours',
        CURRENT_TIMESTAMP - INTERVAL '2 hours 40 minutes',
        'COMPLETADO');

-- SP150: CIX->LIM MANANA PROGRAMADO
INSERT INTO vuelos (codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado)
VALUES ('SP150', 4, 3,
        CURRENT_TIMESTAMP + INTERVAL '1 day 2 hours',
        CURRENT_TIMESTAMP + INTERVAL '1 day 3 hours 30 minutes',
        'PROGRAMADO');

-- SP420: LIM->TRU MANANA RETRASADO
INSERT INTO vuelos (codigo_vuelo, id_ruta, id_aeronave, fecha_salida_programada, fecha_llegada_programada, estado)
VALUES ('SP420', 5, 2,
        CURRENT_TIMESTAMP + INTERVAL '1 day 5 hours',
        CURRENT_TIMESTAMP + INTERVAL '1 day 6 hours 25 minutes',
        'RETRASADO');

-- ============================================================================
-- 11. VUELO_ASIENTOS (distribucion de ventas por vuelo)
-- Los asientos se generan automaticamente con el TRIGGER, solo cambiamos estados.
-- SP101 (id=1): ~120 VENDIDOS, 40 DISPONIBLES
UPDATE vuelo_asientos SET estado = 'VENDIDO', precio = 189.99
WHERE id_vuelo = 1
AND id_asiento_aeronave IN (
    SELECT id FROM asientos_aeronave
    WHERE id_aeronave = 1
    ORDER BY id
    LIMIT 120
);

UPDATE vuelo_asientos SET estado = 'VENDIDO', precio = 289.99
WHERE id_vuelo = 1
AND id_asiento_aeronave IN (
    SELECT id FROM asientos_aeronave
    WHERE id_aeronave = 1 AND clase IN ('EJECUTIVA', 'PRIMERA')
);

UPDATE vuelo_asientos SET estado = 'RESERVADO', precio = 450.00
WHERE id_vuelo = 1
AND id_asiento_aeronave IN (
    SELECT id FROM asientos_aeronave
    WHERE id_aeronave = 1 AND clase = 'PRIMERA'
    ORDER BY id LIMIT 5
);

-- SP205 (id=2): ~40 VENDIDOS, 120 DISPONIBLES
UPDATE vuelo_asientos SET estado = 'VENDIDO', precio = 245.00
WHERE id_vuelo = 2
AND id_asiento_aeronave IN (
    SELECT id FROM asientos_aeronave
    WHERE id_aeronave = 2
    ORDER BY id
    LIMIT 40
);

UPDATE vuelo_asientos SET estado = 'VENDIDO', precio = 399.99
WHERE id_vuelo = 2
AND id_asiento_aeronave IN (
    SELECT id FROM asientos_aeronave
    WHERE id_aeronave = 2 AND clase = 'EJECUTIVA'
    ORDER BY id LIMIT 10
);

-- SP310 (id=3): ~150 VENDIDOS (vuelo completado, casi lleno)
UPDATE vuelo_asientos SET estado = 'VENDIDO', precio = 175.00
WHERE id_vuelo = 3
AND id_asiento_aeronave IN (
    SELECT id FROM asientos_aeronave
    WHERE id_aeronave = 1
    ORDER BY id
    LIMIT 150
);

-- SP150 (id=4): ~20 VENDIDOS
UPDATE vuelo_asientos SET estado = 'VENDIDO', precio = 165.00
WHERE id_vuelo = 4
AND id_asiento_aeronave IN (
    SELECT id FROM asientos_aeronave
    WHERE id_aeronave = 3
    ORDER BY id
    LIMIT 20
);

-- SP420 (id=5): ~60 VENDIDOS
UPDATE vuelo_asientos SET estado = 'VENDIDO', precio = 199.99
WHERE id_vuelo = 5
AND id_asiento_aeronave IN (
    SELECT id FROM asientos_aeronave
    WHERE id_aeronave = 2
    ORDER BY id
    LIMIT 60
);

-- ============================================================================
-- 12. COMPRAS
-- ============================================================================
INSERT INTO compras (id_cliente, codigo_transaccion, monto_total, estado_pago, fecha_compra) VALUES
(1, 'TXN-2026-0001', 569.98, 'APROBADO', CURRENT_TIMESTAMP - INTERVAL '2 days'),
(1, 'TXN-2026-0002', 245.00, 'APROBADO', CURRENT_TIMESTAMP - INTERVAL '1 day'),
(2, 'TXN-2026-0003', 399.99, 'APROBADO', CURRENT_TIMESTAMP - INTERVAL '3 hours'),
(1, 'TXN-2026-0004', 165.00, 'PENDIENTE', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
(2, 'TXN-2026-0005', 199.99, 'APROBADO', CURRENT_TIMESTAMP - INTERVAL '30 minutes'),
(2, 'TXN-2026-0006', 175.00, 'APROBADO', CURRENT_TIMESTAMP - INTERVAL '4 hours');

-- ============================================================================
-- 13. PASAJEROS PARA RESERVAS
-- ============================================================================
-- Maria Garcia (id=9) y Juan Lopez (id=10) ya estan en pasajeros

-- ============================================================================
-- 14. RESERVAS
-- ============================================================================
-- SP101: Maria y Juan reservaron
INSERT INTO reservas (id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado) VALUES
(1, (SELECT id FROM vuelo_asientos WHERE id_vuelo = 1 AND estado = 'VENDIDO' LIMIT 1 OFFSET 0), 9,
 CURRENT_TIMESTAMP + INTERVAL '24 hours', 'CONFIRMADA'),
(1, (SELECT id FROM vuelo_asientos WHERE id_vuelo = 1 AND estado = 'VENDIDO' LIMIT 1 OFFSET 1), 10,
 CURRENT_TIMESTAMP + INTERVAL '24 hours', 'CONFIRMADA');

-- SP205: Juan compro
INSERT INTO reservas (id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado) VALUES
(2, (SELECT id FROM vuelo_asientos WHERE id_vuelo = 2 AND estado = 'VENDIDO' LIMIT 1 OFFSET 0), 10,
 CURRENT_TIMESTAMP + INTERVAL '24 hours', 'CONFIRMADA');

-- SP310: Maria viajo
INSERT INTO reservas (id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado) VALUES
(6, (SELECT id FROM vuelo_asientos WHERE id_vuelo = 3 AND estado = 'VENDIDO' LIMIT 1 OFFSET 0), 9,
 CURRENT_TIMESTAMP - INTERVAL '1 hour', 'CONFIRMADA');

-- SP150: pendiente
INSERT INTO reservas (id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado) VALUES
(4, (SELECT id FROM vuelo_asientos WHERE id_vuelo = 4 AND estado = 'VENDIDO' LIMIT 1 OFFSET 0), 9,
 CURRENT_TIMESTAMP + INTERVAL '2 days', 'PENDIENTE');

-- SP420: Juan tiene reserva
INSERT INTO reservas (id_compra, id_vuelo_asiento, id_pasajero, expira_en, estado) VALUES
(5, (SELECT id FROM vuelo_asientos WHERE id_vuelo = 5 AND estado = 'VENDIDO' LIMIT 1 OFFSET 0), 10,
 CURRENT_TIMESTAMP + INTERVAL '2 days', 'CONFIRMADA');

-- ============================================================================
-- 15. BOLETOS
-- ============================================================================
INSERT INTO boletos (codigo_eticket, id_reserva, id_vuelo_asiento, id_pasajero, estado, fecha_emision) VALUES
('SK-00001', 1, (SELECT id_vuelo_asiento FROM reservas WHERE id = 1), 9, 'EMITIDO', CURRENT_TIMESTAMP - INTERVAL '2 days'),
('SK-00002', 2, (SELECT id_vuelo_asiento FROM reservas WHERE id = 2), 10, 'EMITIDO', CURRENT_TIMESTAMP - INTERVAL '2 days'),
('SK-00003', 3, (SELECT id_vuelo_asiento FROM reservas WHERE id = 3), 10, 'EMITIDO', CURRENT_TIMESTAMP - INTERVAL '1 day'),
('SK-00004', 4, (SELECT id_vuelo_asiento FROM reservas WHERE id = 4), 9, 'EMITIDO', CURRENT_TIMESTAMP - INTERVAL '4 hours'),
('SK-00005', 5, (SELECT id_vuelo_asiento FROM reservas WHERE id = 5), 9, 'EMITIDO', CURRENT_TIMESTAMP - INTERVAL '1 hour'),
('SK-00006', 6, (SELECT id_vuelo_asiento FROM reservas WHERE id = 6), 10, 'CHECKED_IN', CURRENT_TIMESTAMP - INTERVAL '3 hours');

-- ============================================================================
-- 16. EQUIPAJES
-- ============================================================================
INSERT INTO equipajes (id_boleto, codigo_etiqueta_bag, categoria_peso, peso_kg, estado_actual, cinta_carrusel_actual) VALUES
(1, 'BAG-SP101-001', 'NORMAL',  18.50, 'REGISTRADO',       NULL),
(2, 'BAG-SP101-002', 'LIGERO',  7.20,  'REGISTRADO',       NULL),
(3, 'BAG-SP205-001', 'PESADO',  23.00, 'EMBARCADO',        NULL),
(4, 'BAG-SP310-001', 'NORMAL',  15.80, 'DISTRIBUCION_CINTA', 'CINTA-03'),
(5, 'BAG-SP150-001', 'LIGERO',  5.50,  'DESCARGADO',       NULL),
(6, 'BAG-SP420-001', 'NORMAL',  16.20, 'ENTREGADO',        NULL);

-- ============================================================================
-- 17. HISTORIAL DE EQUIPAJE
-- ============================================================================
INSERT INTO historial_equipaje (id_equipaje, estado, ubicacion_aeropuerto_id, cinta_carrusel, observaciones, id_empleado, fecha_registro) VALUES
(1, 'REGISTRADO',      1, NULL, 'Equipaje registrado en counter principal', 1, CURRENT_TIMESTAMP - INTERVAL '3 hours'),
(2, 'REGISTRADO',      1, NULL, 'Equipaje registrado en counter principal', 1, CURRENT_TIMESTAMP - INTERVAL '3 hours'),
(3, 'REGISTRADO',      1, NULL, 'Equipaje registrado', 2, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
(3, 'CONTROL_SEGURIDAD', 1, NULL, 'Equipaje paso por control de seguridad - OK', 2, CURRENT_TIMESTAMP - INTERVAL '1 hour 50 minutes'),
(3, 'EMBARCADO',       1, NULL, 'Equipaje cargado en avion OC-ABQ', 3, CURRENT_TIMESTAMP - INTERVAL '1 hour 30 minutes'),
(4, 'REGISTRADO',      1, NULL, 'Equipaje registrado en counter', 1, CURRENT_TIMESTAMP - INTERVAL '5 hours'),
(4, 'EMBARCADO',       1, NULL, 'Cargado en avion', 3, CURRENT_TIMESTAMP - INTERVAL '4 hours'),
(4, 'DESCARGADO',      1, NULL, 'Descargado del avion en destino', 2, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
(4, 'DISTRIBUCION_CINTA', 1, 'CINTA-03', 'Colocado en cinta de distribucion 3', 1, CURRENT_TIMESTAMP - INTERVAL '1 hour 50 minutes'),
(5, 'REGISTRADO',      2, NULL, 'Equipaje registrado en Chiclayo', 1, CURRENT_TIMESTAMP - INTERVAL '1 day 3 hours'),
(5, 'DESCARGADO',      1, NULL, 'Descargado en Lima', 2, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
(6, 'REGISTRADO',      1, NULL, 'Equipaje registrado', 1, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
(6, 'EMBARCADO',       1, NULL, 'Cargado', 3, CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- ============================================================================
-- 18. ALERTAS OPERATIVAS
-- ============================================================================
INSERT INTO alertas_operativas (titulo, descripcion, nivel, activa) VALUES
('Vuelo SP420 retrasado 45 minutos',
 'Clima adverso en la zona de Trujillo ha causado un retraso en la salida del vuelo SP420. Se estima nueva hora de salida en 45 minutos.',
 'CRITICO', TRUE),
('Aeronave OC-XYZ requiere inspeccion',
 'La aeronave OC-XYZ tiene programada una inspeccion de rutina antes de su proximo vuelo. Mantenimiento estimado: 2 horas.',
 'ADVERTENCIA', TRUE),
('Sistema actualizado a v2.4.1',
 'El sistema de gestion administrativa ha sido actualizado exitosamente. Nuevas funcionalidades disponibles.',
 'INFO', TRUE),
('Operaciones normales en terminal nacional',
 'Todos los vuelos nacionales operan con normalidad. Sin retrasos adicionales reportados.',
 'INFO', TRUE);
