USE alquiler_vehiculos;
SET NAMES utf8mb4;

-- ==============================================================================
-- DATOS INICIALES: roles
-- ==============================================================================
INSERT INTO roles (nombre, descripcion)
VALUES
    ('ADMIN', 'Usuario administrador con acceso completo a la aplicación'),
    ('USER', 'Usuario estándar con acceso limitado a sus alquileres');

-- ==============================================================================
-- DATOS INICIALES: usuarios
-- ==============================================================================
-- IMPORTANTE:
-- Las contraseñas deben guardarse cifradas con BCrypt.
-- De momento usamos hashes de ejemplo y más adelante los revisaremos
-- cuando configuremos Spring Security.

INSERT INTO usuarios (username, email, password_hash, enabled, id_rol)
VALUES
    ('admin', 'admin@alquilervehiculos.com', '$2a$10$0y9RDCziGEC2HVEnzFlXquKx67nLHM8H4EgJ0HIk1UyYx59lTC2TS', TRUE, 1),
    ('xavi', 'xavi@alquilervehiculos.com', '$2a$10$Bi77gM1XSwsrpjKizdaOIOjfSjGdFWQRBIT.gKAcGsPBnK2J.HZRa', TRUE, 2);

-- ==============================================================================
-- DATOS INICIALES: clientes
-- ==============================================================================
-- El primer cliente queda asociado al usuario xavi.
-- El segundo cliente no tiene usuario asociado.
INSERT INTO clientes (nombre, apellidos, dni, telefono, id_usuario)
VALUES
    ('Xavi', 'Miró Carrera', '12345678A', '600111222', 2),
    ('Ana', 'López García', '87654321B', '600333444', NULL);

-- ==============================================================================
-- DATOS INICIALES: vehiculos
-- ==============================================================================
INSERT INTO vehiculos (matricula, marca, modelo, tipo, precio_dia, estado)
VALUES
    ('1234ABC', 'Toyota', 'Corolla', 'Turismo', 45.00, 'DISPONIBLE'),
    ('5678DEF', 'Ford', 'Transit', 'Furgoneta', 80.00, 'DISPONIBLE');

-- ==============================================================================
-- DATOS INICIALES: alquileres
-- ==============================================================================
-- creado_por y modificado_por hacen referencia a usuarios.
-- En estos datos iniciales dejamos que el administrador haya creado/modificado los alquileres.
INSERT INTO alquileres (
    fecha_inicio,
    fecha_fin,
    estado,
    dias,
    precio_dia_aplicado,
    total,
    id_cliente,
    id_vehiculo,
    creado_por,
    modificado_por
)
VALUES
    ('2026-04-10', '2026-04-12', 'FINALIZADO', 3, 45.00, 135.00, 1, 1, 1, 1),
    ('2026-04-15', '2026-04-20', 'EN_CURSO', 2, 80.00, 160.00, 2, 2, 1, 1);