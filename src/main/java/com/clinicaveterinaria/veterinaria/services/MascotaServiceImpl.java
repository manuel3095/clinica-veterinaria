package com.clinicaveterinaria.veterinaria.services;

import com.clinicaveterinaria.veterinaria.model.Mascota;
import com.clinicaveterinaria.veterinaria.repository.MascotaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de los servicios del CRUD de Mascota.
 *
 * Esta clase contiene la logica de negocio: se apoya en
 * MascotaRepository (inyectado mediante @Autowired) para persistir y
 * consultar la informacion en la base de datos MySQL.
 */
@Service
public class MascotaServiceImpl implements MascotaService {

    @Autowired
    private MascotaRepository mascotaRepository;

    @Override
    public Mascota nuevaMascota(Mascota nuevaMascota) {
        // save() inserta el registro si no tiene id, o lo actualiza si ya existe.
        return mascotaRepository.save(nuevaMascota);
    }

    @Override
    public Iterable<Mascota> obtenerTodas() {
        return mascotaRepository.findAll();
    }

    @Override
    public Mascota modificarMascota(Mascota mascota) {
        // Se busca primero la mascota existente por su identificador.
        Optional<Mascota> mascotaEncontrada = mascotaRepository.findById(mascota.getIdMascota());

        if (mascotaEncontrada.isPresent()) {
            // Se actualizan uno a uno los campos con los nuevos valores recibidos.
            Mascota mascotaActualizar = mascotaEncontrada.get();
            mascotaActualizar.setNomMascota(mascota.getNomMascota());
            mascotaActualizar.setEstado(mascota.getEstado());
            mascotaActualizar.setEspecie(mascota.getEspecie());
            mascotaActualizar.setEdad(mascota.getEdad());
            mascotaActualizar.setRaza(mascota.getRaza());
            mascotaActualizar.setFechaIngreso(mascota.getFechaIngreso());
            mascotaActualizar.setCorreoPropietario(mascota.getCorreoPropietario());
            return mascotaRepository.save(mascotaActualizar);
        }
        // Si la mascota no existe, no se realiza ninguna modificacion.
        return null;
    }

    @Override
    public Boolean eliminarMascota(Long idMascota) {
        // Si el identificador no existe no hay nada que eliminar.
        if (!mascotaRepository.existsById(idMascota)) {
            return false;
        }
        mascotaRepository.deleteById(idMascota);
        return true;
    }

    @Override
    public Mascota obtenerPorId(Long idMascota) {
        return mascotaRepository.findById(idMascota).orElse(null);
    }

    @Override
    public List<Mascota> obtenerPorEstado(String estado) {
        return mascotaRepository.findByEstado(estado);
    }

    @Override
    public List<Mascota> obtenerPorPropietario(String correoPropietario) {
        return mascotaRepository.findByCorreoPropietario(correoPropietario);
    }
}
