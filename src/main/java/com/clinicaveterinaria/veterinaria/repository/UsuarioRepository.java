package com.clinicaveterinaria.veterinaria.repository;

import com.clinicaveterinaria.veterinaria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de acceso a datos para la entidad Usuario.
 *
 * Al extender de JpaRepository, Spring Data JPA genera automaticamente
 * en tiempo de ejecucion las operaciones basicas de persistencia
 * (guardar, buscar por id, buscar todos, eliminar). El metodo
 * findByNombreUsuario se genera automaticamente a partir de su firma,
 * siguiendo la convencion de nombramiento de Spring Data JPA.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por su nombre de usuario (utilizado en login y registro).
    Usuario findByNombreUsuario(String nombreUsuario);
}
