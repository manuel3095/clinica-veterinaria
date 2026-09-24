/**
 * Validaciones del formulario de mascotas en el front-end.
 *
 * Este modulo replica, en el lado del cliente, las mismas cinco
 * categorias de validacion que exige la evidencia y que ya estan
 * implementadas en el backend (clase MascotaValidador.java):
 * textos, caracteres especiales, numeros, fechas y longitudes.
 * Validar tambien en el cliente evita peticiones innecesarias al
 * servidor y mejora la experiencia de usuario, mostrando el error
 * de inmediato en el formulario.
 *
 * El modulo se escribio para funcionar tanto en el navegador (se
 * expone en window.ValidacionesMascota) como en Node.js (se exporta
 * con module.exports), de forma que las mismas funciones puedan
 * probarse automaticamente sin necesidad de un navegador real.
 */
(function (raiz) {
    "use strict";

    var PATRON_SOLO_LETRAS = /^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,50}$/;
    var PATRON_CORREO = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    var PATRON_SOLO_DIGITOS = /^[0-9]{1,2}$/;
    var ESTADOS_VALIDOS = ["Estable", "Critico", "Hospitalizado"];
    var EDAD_MAXIMA_MASCOTA = 40;
    var LONGITUD_MINIMA_RAZA = 2;
    var LONGITUD_MAXIMA_RAZA = 50;

    // ---- Validacion de TEXTOS ----
    function validarTexto(valor, nombreCampo) {
        if (!valor || valor.trim() === "") {
            return nombreCampo + ": es obligatorio.";
        }
        if (!PATRON_SOLO_LETRAS.test(valor)) {
            return nombreCampo + ": solo debe contener letras y espacios (2 a 50 caracteres), "
                + "no se permiten numeros ni caracteres especiales.";
        }
        return null;
    }

    // ---- Validacion de un campo de dominio cerrado (estado clinico) ----
    function validarEstado(estado) {
        if (!estado || estado.trim() === "") {
            return "estado: es obligatorio.";
        }
        if (ESTADOS_VALIDOS.indexOf(estado) === -1) {
            return "estado: valor no permitido. Debe ser uno de [" + ESTADOS_VALIDOS.join(", ") + "].";
        }
        return null;
    }

    // ---- Validacion de NUMEROS (edad) ----
    function validarEdad(edad) {
        if (!edad || String(edad).trim() === "") {
            return "edad: es obligatoria.";
        }
        var texto = String(edad).trim();
        if (!PATRON_SOLO_DIGITOS.test(texto)) {
            return "edad: debe ser un numero de 1 o 2 digitos, sin letras ni simbolos.";
        }
        var valorEdad = parseInt(texto, 10);
        if (valorEdad < 0 || valorEdad > EDAD_MAXIMA_MASCOTA) {
            return "edad: fuera de rango permitido (0 a " + EDAD_MAXIMA_MASCOTA + " anios).";
        }
        return null;
    }

    // ---- Validacion de FECHAS (fecha de ingreso, formato AAAA-MM-DD) ----
    function validarFechaIngreso(fechaTexto) {
        if (!fechaTexto || fechaTexto.trim() === "") {
            return "fechaIngreso: es obligatoria.";
        }
        var fecha = new Date(fechaTexto + "T00:00:00");
        if (isNaN(fecha.getTime())) {
            return "fechaIngreso: formato de fecha invalido.";
        }
        var hoy = new Date();
        hoy.setHours(0, 0, 0, 0);
        if (fecha.getTime() > hoy.getTime()) {
            return "fechaIngreso: no puede ser una fecha futura.";
        }
        var hace40Anios = new Date();
        hace40Anios.setFullYear(hace40Anios.getFullYear() - 40);
        if (fecha.getTime() < hace40Anios.getTime()) {
            return "fechaIngreso: fecha demasiado antigua (mas de 40 anios atras).";
        }
        return null;
    }

    // ---- Validacion de LONGITUDES (raza) ----
    function validarLongitud(valor, nombreCampo, minimo, maximo) {
        if (!valor || valor.trim() === "") {
            return nombreCampo + ": es obligatorio.";
        }
        if (valor.length < minimo || valor.length > maximo) {
            return nombreCampo + ": debe tener entre " + minimo + " y " + maximo + " caracteres "
                + "(longitud actual: " + valor.length + ").";
        }
        return null;
    }

    // ---- Validacion de CARACTERES ESPECIALES (correo del propietario) ----
    function validarCorreoPropietario(correo) {
        if (!correo || correo.trim() === "") {
            return "correoPropietario: es obligatorio.";
        }
        if (!PATRON_CORREO.test(correo)) {
            return "correoPropietario: formato invalido. Debe tener la forma usuario@dominio.com, "
                + "sin espacios ni caracteres especiales no permitidos.";
        }
        return null;
    }

    /**
     * Valida todos los campos de una mascota y retorna un arreglo con
     * los mensajes de error encontrados. Un arreglo vacio significa
     * que los datos son validos.
     */
    function validarMascota(mascota) {
        var errores = [];
        var candidatos = [
            validarTexto(mascota.nomMascota, "nomMascota"),
            validarEstado(mascota.estado),
            validarTexto(mascota.especie, "especie"),
            validarEdad(mascota.edad),
            validarLongitud(mascota.raza, "raza", LONGITUD_MINIMA_RAZA, LONGITUD_MAXIMA_RAZA),
            validarFechaIngreso(mascota.fechaIngreso),
            validarCorreoPropietario(mascota.correoPropietario)
        ];
        candidatos.forEach(function (error) {
            if (error) {
                errores.push(error);
            }
        });
        return errores;
    }

    var ValidacionesMascota = {
        validarMascota: validarMascota,
        validarTexto: validarTexto,
        validarEstado: validarEstado,
        validarEdad: validarEdad,
        validarFechaIngreso: validarFechaIngreso,
        validarLongitud: validarLongitud,
        validarCorreoPropietario: validarCorreoPropietario
    };

    if (typeof module !== "undefined" && module.exports) {
        module.exports = ValidacionesMascota;
    } else {
        raiz.ValidacionesMascota = ValidacionesMascota;
    }
})(typeof window !== "undefined" ? window : this);
