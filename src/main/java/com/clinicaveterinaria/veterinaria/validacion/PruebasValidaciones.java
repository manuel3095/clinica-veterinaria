package com.clinicaveterinaria.veterinaria.validacion;

import java.time.LocalDate;
import java.util.List;

/**
 * Programa de pruebas de las validaciones del modulo de Mascotas.
 *
 * Ejecuta casos de prueba positivos (datos validos, deben pasar sin
 * errores) y negativos (datos invalidos, deben ser rechazados con un
 * mensaje de error especifico) sobre cada una de las categorias de
 * validacion exigidas por la evidencia: fechas, numeros, textos,
 * caracteres especiales y longitudes.
 *
 * No depende de Spring ni de la base de datos: ejercita directamente
 * la clase MascotaValidador, por lo que puede compilarse y ejecutarse
 * con las herramientas basicas del JDK (javac / java).
 */
public class PruebasValidaciones {

    private static int casosTotales = 0;
    private static int casosAprobados = 0;

    public static void main(String[] args) {

        System.out.println("=========================================================");
        System.out.println(" PRUEBAS DE VALIDACION - Modulo de Mascotas (AA3-EV02)");
        System.out.println("=========================================================\n");

        // ---------- TEXTOS ----------
        System.out.println("--- Categoria: TEXTOS (nombre/especie) ---");
        caso("Nombre y especie validos ('Max' / 'Canis lupus familiaris')",
                MascotaValidador.validarMascota("Max", "Estable", "Canis lupus familiaris",
                        "3", "Husky", LocalDate.of(2023, 5, 15), "propietario@email.com"),
                true);
        caso("Nombre con numeros ('Max2') debe rechazarse",
                MascotaValidador.validarMascota("Max2", "Estable", "Canis lupus familiaris",
                        "3", "Husky", LocalDate.of(2023, 5, 15), "propietario@email.com"),
                false);
        caso("Nombre vacio debe rechazarse",
                MascotaValidador.validarMascota("", "Estable", "Canis lupus familiaris",
                        "3", "Husky", LocalDate.of(2023, 5, 15), "propietario@email.com"),
                false);
        caso("Estado fuera del dominio permitido ('Perdido') debe rechazarse",
                MascotaValidador.validarMascota("Max", "Perdido", "Canis lupus familiaris",
                        "3", "Husky", LocalDate.of(2023, 5, 15), "propietario@email.com"),
                false);

        // ---------- CARACTERES ESPECIALES (correo del propietario) ----------
        System.out.println("\n--- Categoria: CARACTERES ESPECIALES (correo del propietario) ---");
        caso("Correo del propietario valido ('duena@email.com')",
                MascotaValidador.validarMascota("Apolo", "Hospitalizado", "Canis lupus familiaris",
                        "2", "San Bernardo", LocalDate.of(2022, 1, 10), "duena@email.com"),
                true);
        caso("Correo sin arroba ('duenaemail.com') debe rechazarse",
                MascotaValidador.validarMascota("Apolo", "Hospitalizado", "Canis lupus familiaris",
                        "2", "San Bernardo", LocalDate.of(2022, 1, 10), "duenaemail.com"),
                false);
        caso("Correo con caracteres invalidos ('duena@@ema#il.com') debe rechazarse",
                MascotaValidador.validarMascota("Apolo", "Hospitalizado", "Canis lupus familiaris",
                        "2", "San Bernardo", LocalDate.of(2022, 1, 10), "duena@@ema#il.com"),
                false);

        // ---------- NUMEROS (edad) ----------
        System.out.println("\n--- Categoria: NUMEROS (edad) ---");
        caso("Edad valida ('4')",
                MascotaValidador.validarMascota("Luna", "Estable", "Felis catus",
                        "4", "Siames", LocalDate.of(2021, 6, 1), "luna.duena@email.com"),
                true);
        caso("Edad con letras ('4a') debe rechazarse",
                MascotaValidador.validarMascota("Luna", "Estable", "Felis catus",
                        "4a", "Siames", LocalDate.of(2021, 6, 1), "luna.duena@email.com"),
                false);
        caso("Edad fuera de rango ('99') debe rechazarse",
                MascotaValidador.validarMascota("Luna", "Estable", "Felis catus",
                        "99", "Siames", LocalDate.of(2021, 6, 1), "luna.duena@email.com"),
                false);

        // ---------- FECHAS (fecha de ingreso) ----------
        System.out.println("\n--- Categoria: FECHAS (fecha de ingreso) ---");
        caso("Fecha de ingreso valida (2023-05-15)",
                MascotaValidador.validarMascota("Rocky", "Estable", "Canis lupus familiaris",
                        "5", "Labrador", LocalDate.of(2023, 5, 15), "rocky.duena@email.com"),
                true);
        caso("Fecha de ingreso futura debe rechazarse",
                MascotaValidador.validarMascota("Rocky", "Estable", "Canis lupus familiaris",
                        "5", "Labrador", LocalDate.now().plusDays(1), "rocky.duena@email.com"),
                false);
        caso("Fecha de ingreso hace 50 anios (fuera de rango) debe rechazarse",
                MascotaValidador.validarMascota("Rocky", "Estable", "Canis lupus familiaris",
                        "5", "Labrador", LocalDate.now().minusYears(50), "rocky.duena@email.com"),
                false);

        // ---------- LONGITUDES (raza) ----------
        System.out.println("\n--- Categoria: LONGITUDES (raza) ---");
        caso("Raza con longitud valida ('Golden Retriever')",
                MascotaValidador.validarMascota("Toby", "Estable", "Canis lupus familiaris",
                        "2", "Golden Retriever", LocalDate.of(2024, 2, 1), "toby.duena@email.com"),
                true);
        caso("Raza demasiado corta ('S') debe rechazarse",
                MascotaValidador.validarMascota("Toby", "Estable", "Canis lupus familiaris",
                        "2", "S", LocalDate.of(2024, 2, 1), "toby.duena@email.com"),
                false);

        System.out.println("\n=========================================================");
        System.out.println(" RESULTADO FINAL: " + casosAprobados + " / " + casosTotales + " casos aprobados");
        System.out.println("=========================================================");

        if (casosAprobados != casosTotales) {
            System.exit(1);
        }
    }

    private static void caso(String descripcion, List<String> errores, boolean debeSerValido) {
        casosTotales++;
        boolean esValido = errores.isEmpty();
        boolean aprobado = (esValido == debeSerValido);
        if (aprobado) {
            casosAprobados++;
        }
        System.out.println((aprobado ? "[APROBADO] " : "[FALLIDO]  ") + descripcion);
        if (!esValido) {
            for (String error : errores) {
                System.out.println("           -> " + error);
            }
        }
    }
}
