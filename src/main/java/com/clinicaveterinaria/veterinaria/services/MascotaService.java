package com.clinicaveterinaria.veterinaria.services;

import com.clinicaveterinaria.veterinaria.model.Mascota;
import java.util.List;

/**
 * Contrato de la capa de servicios para la entidad Mascota.
 *
 * Define las cuatro operaciones del CRUD (Create, Read, Update, Delete)
 * que expondra el controlador REST, desacoplando la logica de negocio
 * de la implementacion concreta del acceso a datos.
 */
public interface MascotaService {

    // Registra una nueva mascota en la base de datos.
    Mascota nuevaMascota(Mascota nuevaMascota);

    // Retorna el listado completo de mascotas registradas.
    Iterable<Mascota> obtenerTodas();

    // Actualiza los datos de una mascota existente.
    Mascota modificarMascota(Mascota mascota);

    // Elimina una mascota por su identificador. Retorna true si existia y se elimino.
    Boolean eliminarMascota(Long idMascota);

    // Busca una mascota por su identificador; retorna null si no existe.
    Mascota obtenerPorId(Long idMascota);

    // Lista las mascotas que se encuentran en un estado clinico dado.
    List<Mascota> obtenerPorEstado(String estado);

    // Lista las mascotas asociadas al correo de un propietario.
    List<Mascota> obtenerPorPropietario(String correoPropietario);
}
