-- ============================================================
-- Script de creacion de la base de datos de la Clinica Veterinaria
-- Modulo: Mascotas (GA7-220501096-AA2-EV02)
-- ============================================================

CREATE DATABASE IF NOT EXISTS clinicaveterinaria
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE clinicaveterinaria;

CREATE TABLE IF NOT EXISTS mascotas (
    id_mascota   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_mascota  VARCHAR(255) NOT NULL,
    estado       VARCHAR(255) NOT NULL,
    especie      VARCHAR(255) NOT NULL,
    edad         VARCHAR(255) NOT NULL,
    raza         VARCHAR(255) NOT NULL
);

-- Usuario de aplicacion con permisos unicamente sobre esta base de datos.
CREATE USER IF NOT EXISTS 'veterinaria_user'@'localhost' IDENTIFIED BY 'Veterinaria2024*';
GRANT ALL PRIVILEGES ON clinicaveterinaria.* TO 'veterinaria_user'@'localhost';
FLUSH PRIVILEGES;

-- Datos de ejemplo (opcional, util para verificar la instalacion).
INSERT INTO mascotas (nom_mascota, estado, especie, edad, raza) VALUES
    ('Max', 'Estable', 'Canis lupus familiaris', '3', 'Husky'),
    ('Apolo', 'Hospitalizado', 'Canis lupus familiaris', '2', 'San Bernardo');
