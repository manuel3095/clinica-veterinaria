package com.clinicaveterinaria.veterinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de arranque de la aplicacion Spring Boot.
 *
 * Al ejecutar el metodo main, Spring Boot levanta el contenedor de
 * inyeccion de dependencias, configura automaticamente la conexion a la
 * base de datos (a partir de application.properties) y expone los
 * endpoints REST definidos en el paquete controller.
 */
@SpringBootApplication
public class VeterinariaApplication {

    public static void main(String[] args) {
        SpringApplication.run(VeterinariaApplication.class, args);
    }
}
