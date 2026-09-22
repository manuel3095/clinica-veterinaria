package com.clinicaveterinaria.veterinaria.controller;

import com.clinicaveterinaria.veterinaria.model.Mascota;
import com.clinicaveterinaria.veterinaria.services.MascotaService;
import com.clinicaveterinaria.veterinaria.validacion.MascotaValidador;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
 * Antes de guardar o actualizar, los datos recibidos se validan con
 * MascotaValidador; si se encuentra algun error (fechas, numeros,
 * textos, caracteres especiales o longitudes fuera de rango), se
 * retorna 400 Bad Request con el detalle de los errores encontrados,
 * en lugar de persistir datos invalidos.
 *
 * Ruta base: /mascotas
 */
@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    @Autowired
    private MascotaService mascotaService;

    /**
     * Crea una nueva mascota, validando previamente sus datos.
     * POST /mascotas/nuevo
     */
    @PostMapping("/nuevo")
    public ResponseEntity<?> nuevaMascota(@RequestBody Mascota nuevaMascota) {
        List<String> errores = validar(nuevaMascota);
        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(this.mascotaService.nuevaMascota(nuevaMascota));
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
     * Actualiza los datos de una mascota existente, validando
     * previamente sus datos.
     * POST /mascotas/modificar
     */
    @PostMapping("/modificar")
    public ResponseEntity<?> modificarMascota(@RequestBody Mascota mascota) {
        List<String> errores = validar(mascota);
        if (!errores.isEmpty()) {
            return ResponseEntity.badRequest().body(errores);
        }
        return ResponseEntity.ok(this.mascotaService.modificarMascota(mascota));
    }

    /**
     * Elimina una mascota a partir de su identificador.
     * POST /mascotas/{id}
     */
    @PostMapping("/{id}")
    public Boolean eliminarMascota(@PathVariable(value = "id") Long idMascota) {
        return this.mascotaService.eliminarMascota(idMascota);
    }

    /**
     * Ejecuta MascotaValidador sobre los campos de la mascota recibida.
     */
    private List<String> validar(Mascota mascota) {
        return MascotaValidador.validarMascota(
                mascota.getNomMascota(), mascota.getEstado(), mascota.getEspecie(),
                mascota.getEdad(), mascota.getRaza(), mascota.getFechaIngreso(),
                mascota.getCorreoPropietario());
    }
}

