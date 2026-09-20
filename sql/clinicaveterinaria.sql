-- ============================================================
-- Script de creacion de la base de datos de la Clinica Veterinaria
-- Modulo: Mascotas (GA7-220501096-AA3-EV01)
-- ============================================================

CREATE DATABASE IF NOT EXISTS clinicaveterinaria
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_general_ci;

USE clinicaveterinaria;

-- Spring Data JPA puede crear esta tabla automaticamente
-- (spring.jpa.hibernate.ddl-auto=update), pero se deja el script
-- explicito para trazabilidad y para poblar datos de ejemplo.
CREATE TABLE IF NOT EXISTS mascotas (
    id_mascota   BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom_mascota  VARCHAR(255) NOT NULL,
    estado       VARCHAR(255) NOT NULL,
    especie      VARCHAR(255) NOT NULL,
    edad         VARCHAR(255) NOT NULL,
    raza         VARCHAR(255) NOT NULL
);

INSERT INTO mascotas (nom_mascota, estado, especie, edad, raza) VALUES
    ('Max', 'Estable', 'Canis lupus familiaris', '3', 'Husky'),
    ('Apolo', 'Hospitalizado', 'Canis lupus familiaris', '2', 'San Bernardo');
