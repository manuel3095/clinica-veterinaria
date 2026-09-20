package com.clinicaveterinaria.veterinaria.controller;

import com.clinicaveterinaria.veterinaria.model.Mascota;
import com.clinicaveterinaria.veterinaria.services.MascotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST del modulo Mascotas.
 *
 * Expone los endpoints HTTP que permiten crear, consultar, modificar y
 * eliminar mascotas, delegando toda la logica de negocio a
 * MascotaService (inyectado mediante @Autowired).
 *
 * Ruta base: /mascotas
 */
@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    /**
     * Crea una nueva mascota.
     * POST /mascotas/nuevo
     */
    @PostMapping("/nuevo")
    public Mascota nuevaMascota(@RequestBody Mascota nuevaMascota) {
        return this.mascotaService.nuevaMascota(nuevaMascota);
    }

    /**
     * Retorna el listado completo de mascotas registradas.
     * GET /mascotas/mostrar
     */
    @GetMapping("/mostrar")
    public Iterable<Mascota> obtenerTodas() {
        return mascotaService.obtenerTodas();
    }

    /**
     * Actualiza los datos de una mascota existente.
     * POST /mascotas/modificar
     */
    @PostMapping("/modificar")
    public Mascota modificarMascota(@RequestBody Mascota mascota) {
        return this.mascotaService.modificarMascota(mascota);
    }

    /**
     * Elimina una mascota a partir de su identificador.
     * POST /mascotas/{id}
     */
    @PostMapping("/{id}")
    public Boolean eliminarMascota(@PathVariable(value = "id") Long idMascota) {
        return this.mascotaService.eliminarMascota(idMascota);
    }
}
