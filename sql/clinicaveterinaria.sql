-- ============================================================
-- Script de creacion de la base de datos de la Clinica Veterinaria
-- Modulos: Mascotas (AA3-EV02) y Usuarios/Autenticacion (AA5-EV01)
-- ============================================================

CREATE DATABASE IF NOT EXISTS clinicaveterinaria
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE clinicaveterinaria;

CREATE TABLE IF NOT EXISTS mascotas (
    id_mascota         BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_mascota        VARCHAR(255) NOT NULL,
    estado             VARCHAR(255) NOT NULL,
    especie            VARCHAR(255) NOT NULL,
    edad               VARCHAR(255) NOT NULL,
    raza               VARCHAR(255) NOT NULL,
    fecha_ingreso      DATE NOT NULL,
    correo_propietario VARCHAR(255) NOT NULL
);

INSERT INTO mascotas (nom_mascota, estado, especie, edad, raza, fecha_ingreso, correo_propietario) VALUES
    ('Max', 'Estable', 'Canis lupus familiaris', '3', 'Husky', '2023-05-15', 'propietario@email.com'),
    ('Apolo', 'Hospitalizado', 'Canis lupus familiaris', '2', 'San Bernardo', '2022-01-10', 'duena@email.com');

-- Tabla de usuarios del personal de la clinica (registro e inicio de
-- sesion, evidencia GA7-220501096-AA5-EV01). La columna "clave"
-- almacena siempre el hash SHA-256 de la contrasena, nunca su texto
-- plano.
CREATE TABLE IF NOT EXISTS usuario (
    id_usuario      BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario  VARCHAR(100) NOT NULL UNIQUE,
    clave           VARCHAR(64)  NOT NULL
);

-- Usuario de ejemplo: "recepcion1" / clave en texto plano "clave1234"
-- (el valor de abajo ya es su hash SHA-256, no la clave en texto plano).
INSERT INTO usuario (nombre_usuario, clave) VALUES
    ('recepcion1', '86b4d07558aaafc1d468cb56efeb860cc1f994d9265feb03cda1653817585f8d');

