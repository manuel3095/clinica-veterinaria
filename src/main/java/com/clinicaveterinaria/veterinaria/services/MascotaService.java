package com.clinicaveterinaria.veterinaria.services;

import com.clinicaveterinaria.veterinaria.model.Mascota;

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

    // Elimina una mascota por su identificador. Retorna true si se elimino con exito.
    Boolean eliminarMascota(Long idMascota);
}
