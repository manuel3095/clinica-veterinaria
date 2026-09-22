package com.clinicaveterinaria.veterinaria.validacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Validador de los datos de una mascota antes de ser persistidos.
 *
 * Cubre las cinco categorias de validacion exigidas por la evidencia
 * GA7-220501096-AA3-EV02: textos (nombre/especie/raza), caracteres
 * especiales (correo del propietario), numeros (edad), fechas (fecha
 * de ingreso) y longitudes (todos los campos de texto).
 *
 * Cada metodo publico valida un campo especifico y agrega un mensaje
 * de error a la lista recibida cuando el valor no es valido. El
 * controlador utiliza validarMascota() para obtener la lista completa
 * de errores antes de guardar o actualizar una mascota.
 */
public class MascotaValidador {

    private static final Pattern PATRON_SOLO_LETRAS =
            Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,50}$");

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PATRON_SOLO_DIGITOS =
            Pattern.compile("^[0-9]{1,2}$");

    private static final List<String> ESTADOS_VALIDOS =
            Arrays.asList("Estable", "Critico", "Hospitalizado");

    private static final int EDAD_MAXIMA_MASCOTA = 40;
    private static final int LONGITUD_MINIMA_RAZA = 2;
    private static final int LONGITUD_MAXIMA_RAZA = 50;

    private MascotaValidador() {
    }

    /**
     * Valida todos los campos de una mascota y retorna la lista de
     * mensajes de error encontrados. Una lista vacia significa que
     * los datos son validos.
     */
    public static List<String> validarMascota(String nomMascota, String estado, String especie,
            String edad, String raza, LocalDate fechaIngreso, String correoPropietario) {

        List<String> errores = new ArrayList<>();

        validarTexto(nomMascota, "nomMascota", errores);
        validarEstado(estado, errores);
        validarTexto(especie, "especie", errores);
        validarEdad(edad, errores);
        validarLongitud(raza, "raza", LONGITUD_MINIMA_RAZA, LONGITUD_MAXIMA_RAZA, errores);
        validarFechaIngreso(fechaIngreso, errores);
        validarCorreoPropietario(correoPropietario, errores);

        return errores;
    }

    // ---- Validacion de TEXTOS ----
    public static void validarTexto(String valor, String nombreCampo, List<String> errores) {
        if (valor == null || valor.trim().isEmpty()) {
            errores.add(nombreCampo + ": es obligatorio.");
            return;
        }
        if (!PATRON_SOLO_LETRAS.matcher(valor).matches()) {
            errores.add(nombreCampo + ": solo debe contener letras y espacios (2 a 50 caracteres), "
                    + "no se permiten numeros ni caracteres especiales.");
        }
    }

    // ---- Validacion de un campo de dominio cerrado (estado clinico) ----
    public static void validarEstado(String estado, List<String> errores) {
        if (estado == null || estado.trim().isEmpty()) {
            errores.add("estado: es obligatorio.");
            return;
        }
        if (!ESTADOS_VALIDOS.contains(estado)) {
            errores.add("estado: valor no permitido. Debe ser uno de " + ESTADOS_VALIDOS + ".");
        }
    }

    // ---- Validacion de NUMEROS (edad) ----
    public static void validarEdad(String edad, List<String> errores) {
        if (edad == null || edad.trim().isEmpty()) {
            errores.add("edad: es obligatoria.");
            return;
        }
        if (!PATRON_SOLO_DIGITOS.matcher(edad).matches()) {
            errores.add("edad: debe ser un numero de 1 o 2 digitos, sin letras ni simbolos.");
            return;
        }
        int valorEdad = Integer.parseInt(edad);
        if (valorEdad < 0 || valorEdad > EDAD_MAXIMA_MASCOTA) {
            errores.add("edad: fuera de rango permitido (0 a " + EDAD_MAXIMA_MASCOTA + " anios).");
        }
    }

    // ---- Validacion de FECHAS (fecha de ingreso) ----
    public static void validarFechaIngreso(LocalDate fechaIngreso, List<String> errores) {
        if (fechaIngreso == null) {
            errores.add("fechaIngreso: es obligatoria.");
            return;
        }
        if (fechaIngreso.isAfter(LocalDate.now())) {
            errores.add("fechaIngreso: no puede ser una fecha futura.");
        }
        if (fechaIngreso.isBefore(LocalDate.now().minusYears(40))) {
            errores.add("fechaIngreso: fecha demasiado antigua (mas de 40 anios atras).");
        }
    }

    // ---- Validacion de LONGITUDES (generica, reutilizable) ----
    public static void validarLongitud(String valor, String nombreCampo, int minimo, int maximo,
            List<String> errores) {
        if (valor == null || valor.trim().isEmpty()) {
            errores.add(nombreCampo + ": es obligatorio.");
            return;
        }
        if (valor.length() < minimo || valor.length() > maximo) {
            errores.add(nombreCampo + ": debe tener entre " + minimo + " y " + maximo + " caracteres "
                    + "(longitud actual: " + valor.length() + ").");
        }
    }

    // ---- Validacion de CARACTERES ESPECIALES (formato correo del propietario) ----
    public static void validarCorreoPropietario(String correo, List<String> errores) {
        if (correo == null || correo.trim().isEmpty()) {
            errores.add("correoPropietario: es obligatorio.");
            return;
        }
        if (!PATRON_CORREO.matcher(correo).matches()) {
            errores.add("correoPropietario: formato invalido. Debe tener la forma usuario@dominio.com, "
                    + "sin espacios ni caracteres especiales no permitidos.");
        }
    }
}
