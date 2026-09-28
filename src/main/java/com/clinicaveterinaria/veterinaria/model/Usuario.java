package com.clinicaveterinaria.veterinaria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Entidad JPA que representa un usuario del personal de la clinica
 * (recepcionista o administrador) con acceso al sistema mediante
 * registro e inicio de sesion.
 *
 * La contrasena nunca se almacena en texto plano: siempre se guarda
 * su hash SHA-256, calculado con UtilidadClave.
 */
@Entity
@Data
public class Usuario {

    // Identificador unico del usuario, autogenerado por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    // Nombre de usuario, unico, utilizado para iniciar sesion.
    @Column(name = "nombre_usuario", unique = true)
    private String nombreUsuario;

    // Hash SHA-256 de la contrasena (nunca se almacena en texto plano).
    @Column(name = "clave")
    private String clave;
}
