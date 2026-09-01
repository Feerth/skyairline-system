-- ============================================================================
-- SCRIPT DE CONFIGURACION: USUARIO ADMINISTRADOR
-- SKY AIRLINES PERU - Desktop Application v2.4.1
-- ============================================================================
-- Ejecutar: psql -U postgres -d skyairline_db -f setup_admin.sql
-- ============================================================================

-- Verificar si el usuario ya existe
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM usuarios WHERE email = 'admin@skyairline.com') THEN
        INSERT INTO usuarios (email, password_hash, rol, activo)
        VALUES (
            'admin@skyairline.com',
            '$2a$10$KDnGmk2dsaxHXreaYiiK8OViv5chk720y0HB3FaS74nIVtMNEAUcq',
            'ADMINISTRADOR',
            TRUE
        );
        RAISE NOTICE 'Usuario administrador creado exitosamente.';
        RAISE NOTICE '  Email:    admin@skyairline.com';
        RAISE NOTICE '  Contrasena: admin123';
    ELSE
        RAISE NOTICE 'El usuario admin@skyairline.com ya existe. No se inserto duplicado.';
    END IF;
END
$$;

-- Verificar que la tabla de usuarios tiene registros
SELECT id, email, rol, activo, created_at FROM usuarios;
