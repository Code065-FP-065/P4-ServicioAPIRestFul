DROP DATABASE IF EXISTS alquiler_vehiculos;

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS alquiler_vehiculos
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE alquiler_vehiculos;

-- ==============================================================================
-- TABLA: roles
-- ==============================================================================
CREATE TABLE roles (
    id_rol BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- ==============================================================================
-- TABLA: usuarios
-- ==============================================================================
CREATE TABLE usuarios (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    id_rol BIGINT NOT NULL,

    CONSTRAINT fk_usuario_rol
        FOREIGN KEY (id_rol)
        REFERENCES roles(id_rol)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- ==============================================================================
-- TABLA: clientes
-- ==============================================================================
CREATE TABLE clientes (
	id_cliente BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellidos VARCHAR(150) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    id_usuario BIGINT NULL UNIQUE,

    CONSTRAINT fk_cliente_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- ==============================================================================
-- TABLA: vehiculos
-- ==============================================================================
CREATE TABLE vehiculos (
	id_vehiculo BIGINT AUTO_INCREMENT PRIMARY KEY,
    matricula VARCHAR(20) NOT NULL UNIQUE,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    tipo VARCHAR(100) NOT NULL,
    precio_dia DECIMAL(10,2) NOT NULL,
    estado ENUM ('DISPONIBLE', 'ALQUILADO', 'MANTENIMIENTO') NOT NULL DEFAULT 'DISPONIBLE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT chk_vehiculo_precio_dia CHECK (precio_dia >= 0)
);

-- ==============================================================================
-- TABLA: alquileres
-- ==============================================================================
CREATE TABLE alquileres (
	id_alquiler BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    estado ENUM ('PENDIENTE', 'EN_CURSO', 'FINALIZADO') NOT NULL DEFAULT 'PENDIENTE',
    dias INT NOT NULL,
    precio_dia_aplicado DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    id_cliente BIGINT NOT NULL,
    id_vehiculo BIGINT NOT NULL,

    creado_por BIGINT NULL,
    modificado_por BIGINT NULL,

    CONSTRAINT fk_alquier_cliente
        FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_alquiler_vehiculo
        FOREIGN KEY (id_vehiculo)
        REFERENCES vehiculos(id_vehiculo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_alquiler_creado_por
        FOREIGN KEY (creado_por)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE SET NULL,

    CONSTRAINT fk_alquiler_modificado_por
        FOREIGN KEY (modificado_por)
        REFERENCES usuarios(id_usuario)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    
    CONSTRAINT chk_alquiler_dias CHECK (dias > 0),
    CONSTRAINT chk_alquiler_precio_dia CHECK (precio_dia_aplicado >= 0),
    CONSTRAINT chk_alquiler_total CHECK (total >= 0),
    CONSTRAINT chk_alquiler_fechas CHECK (fecha_fin IS NULL OR fecha_fin >= fecha_inicio)
);

    
    
