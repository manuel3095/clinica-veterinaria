package com.clinicaveterinaria.veterinaria.repository;

import com.clinicaveterinaria.veterinaria.model.Mascota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para la entidad Mascota.
 *
 * Al extender de JpaRepository, Spring Data JPA genera automaticamente
 * en tiempo de ejecucion la implementacion de las operaciones basicas
 * de persistencia (guardar, buscar por id, buscar todos, eliminar),
 * sin necesidad de escribir manualmente ninguna sentencia SQL.
 */
public interface MascotaRepository extends JpaRepository<Mascota, Long> {

    // Consulta derivada: SELECT ... WHERE estado = ?
    List<Mascota> findByEstado(String estado);

    // Consulta derivada: SELECT ... WHERE correo_propietario = ?
    List<Mascota> findByCorreoPropietario(String correoPropietario);
}
