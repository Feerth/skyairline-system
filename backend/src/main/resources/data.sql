INSERT INTO aeropuertos (codigo_iata, nombre, ciudad, pais) VALUES
('LIM', 'Aeropuerto Internacional Jorge Chavez', 'Lima', 'Peru'),
('CUZ', 'Aeropuerto Internacional Alejandro Velasco Astete', 'Cusco', 'Peru'),
('AQP', 'Aeropuerto Internacional Rodriguez Ballon', 'Arequipa', 'Peru'),
('PIU', 'Aeropuerto Internacional Capitan FAP Guillermo Concha Iberico', 'Piura', 'Peru'),
('TRU', 'Aeropuerto Internacional Capitan FAP Carlos Martinez de Pinillos', 'Trujillo', 'Peru'),
('IQT', 'Aeropuerto Internacional Coronel FAP Francisco Secada Vignetta', 'Iquitos', 'Peru'),
('TCQ', 'Aeropuerto Internacional Coronel FAP Carlos Ciriani Santa Rosa', 'Tacna', 'Peru'),
('CIX', 'Aeropuerto Internacional Capitan FAP Jose A. Quinones Gonzales', 'Chiclayo', 'Peru');

INSERT INTO vuelos (numero_vuelo, origen_id, destino_id, fecha_salida, hora_salida, hora_llegada, precio, asientos_disponibles, estado) VALUES
('SKY-501', 1, 2, '2026-09-05', '06:15:00', '07:35:00', 189.00, 168, 'PROGRAMADO'),
('SKY-502', 2, 1, '2026-09-05', '08:20:00', '09:40:00', 195.00, 168, 'PROGRAMADO'),
('SKY-611', 1, 3, '2026-09-06', '07:00:00', '08:25:00', 175.00, 168, 'PROGRAMADO'),
('SKY-612', 3, 1, '2026-09-06', '09:10:00', '10:35:00', 179.00, 168, 'PROGRAMADO'),
('SKY-720', 1, 6, '2026-09-08', '11:45:00', '13:35:00', 220.00, 168, 'PROGRAMADO'),
('SKY-810', 1, 4, '2026-09-10', '14:00:00', '15:20:00', 165.00, 168, 'PROGRAMADO'),
('SKY-905', 1, 5, '2026-09-12', '16:30:00', '17:40:00', 150.00, 168, 'PROGRAMADO'),
('SKY-330', 1, 7, '2026-09-14', '05:50:00', '07:45:00', 210.00, 168, 'PROGRAMADO');
