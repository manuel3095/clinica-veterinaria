package com.clinicaveterinaria.veterinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Entidad JPA que representa una mascota atendida en la clinica
 * veterinaria. Cada instancia se mapea a una fila de la tabla
 * "mascotas" en la base de datos clinicaveterinaria.
 *
 * La anotacion @Data (Lombok) genera automaticamente los metodos
 * getters, setters, equals, hashCode y toString, evitando codigo
 * repetitivo (boilerplate).
 */
@Entity
@Data
public class Mascota {

    // Identificador unico de la mascota, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_mascota")
    private Long idMascota;

    // Nombre de la mascota (por ejemplo: "Max", "Apolo").
    @Column(name = "nom_mascota")
    private String nomMascota;

    // Estado clinico actual de la mascota (Estable, Critico, Hospitalizado).
    @Column
    private String estado;

    // Especie a la que pertenece la mascota (por ejemplo: "Canis lupus familiaris").
    @Column
    private String especie;

    // Edad de la mascota, en anios.
    @Column
    private String edad;

    // Raza de la mascota (por ejemplo: "Husky", "San Bernardo").
    @Column
    private String raza;
}
