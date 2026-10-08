package com.clinicaveterinaria.veterinaria.modelo;

/**
 * Clase de entidad que representa una mascota registrada en la clinica
 * veterinaria.
 *
 * Convenciones de codificacion aplicadas:
 * - Clase en PascalCase (Mascota).
 * - Atributos y metodos en camelCase (idMascota, getNomMascota, etc.).
 * - Paquete en minusculas (com.clinicaveterinaria.veterinaria.modelo).
 */
public class Mascota {

    private Long idMascota;
    private String nomMascota;
    private String estado;
    private String especie;
    private String edad;
    private String raza;

    public Mascota() {
    }

    public Mascota(Long idMascota, String nomMascota, String estado, String especie, String edad, String raza) {
        this.idMascota = idMascota;
        this.nomMascota = nomMascota;
        this.estado = estado;
        this.especie = especie;
        this.edad = edad;
        this.raza = raza;
    }

    public Long getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Long idMascota) {
        this.idMascota = idMascota;
    }

    public String getNomMascota() {
        return nomMascota;
    }

    public void setNomMascota(String nomMascota) {
        this.nomMascota = nomMascota;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    @Override
    public String toString() {
        return "Mascota{idMascota=" + idMascota + ", nomMascota=" + nomMascota
                + ", estado=" + estado + ", especie=" + especie
                + ", edad=" + edad + ", raza=" + raza + '}';
    }
}
