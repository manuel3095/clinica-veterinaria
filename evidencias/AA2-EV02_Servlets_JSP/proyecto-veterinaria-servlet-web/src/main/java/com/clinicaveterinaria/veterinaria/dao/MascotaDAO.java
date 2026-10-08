package com.clinicaveterinaria.veterinaria.dao;

import com.clinicaveterinaria.veterinaria.modelo.Mascota;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato de acceso a datos para la entidad Mascota. Define las cuatro
 * operaciones basicas del CRUD (Create, Read, Update, Delete) que debe
 * implementar la capa de persistencia.
 */
public interface MascotaDAO {

    Mascota insertarMascota(Mascota mascota) throws SQLException;

    List<Mascota> consultarTodas() throws SQLException;

    Mascota consultarPorId(Long idMascota) throws SQLException;

    boolean actualizarMascota(Mascota mascota) throws SQLException;

    boolean eliminarMascota(Long idMascota) throws SQLException;
}
