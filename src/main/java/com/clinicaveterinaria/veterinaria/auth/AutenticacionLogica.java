package com.clinicaveterinaria.veterinaria.auth;

import com.clinicaveterinaria.veterinaria.util.UtilidadClave;

/**
 * Logica pura de registro e inicio de sesion, separada de Spring y de
 * la base de datos, para que pueda probarse de forma aislada y
 * confiable (ver PruebasAutenticacion.java).
 *
 * Esta clase no conoce JPA ni el repositorio: recibe los datos que
 * necesita como parametros (por ejemplo, si el usuario ya existe, o
 * cual es su clave hasheada almacenada) y retorna un resultado
 * (AutenticacionResultado) con el mensaje exigido por la evidencia:
 * "Autenticacion satisfactoria" o "Error en la autenticacion".
 */
public class AutenticacionLogica {

    public static final String MENSAJE_AUTENTICACION_EXITOSA = "Autenticacion satisfactoria";
    public static final String MENSAJE_AUTENTICACION_FALLIDA = "Error en la autenticacion";
    private static final int LONGITUD_MINIMA_CLAVE = 8;
    private static final int LONGITUD_MINIMA_USUARIO = 3;

    private AutenticacionLogica() {
    }

    /**
     * Valida los datos de un nuevo registro (usuario y clave), sin
     * conocer todavia si el nombre de usuario esta disponible en la
     * base de datos (eso lo determina el llamador, via
     * usuarioYaExiste).
     *
     * @return null si el registro es valido; un mensaje de error en
     *         caso contrario.
     */
    public static String validarRegistro(String nombreUsuario, String clave, boolean usuarioYaExiste) {
        if (nombreUsuario == null || nombreUsuario.trim().length() < LONGITUD_MINIMA_USUARIO) {
            return "El nombre de usuario debe tener al menos " + LONGITUD_MINIMA_USUARIO + " caracteres.";
        }
        if (clave == null || clave.length() < LONGITUD_MINIMA_CLAVE) {
            return "La contrasena debe tener al menos " + LONGITUD_MINIMA_CLAVE + " caracteres.";
        }
        if (usuarioYaExiste) {
            return "El nombre de usuario ya se encuentra registrado.";
        }
        return null;
    }

    /**
     * Calcula el hash que debe almacenarse para un nuevo usuario
     * registrado.
     */
    public static String prepararClaveParaAlmacenar(String claveTextoPlano) {
        return UtilidadClave.hashear(claveTextoPlano);
    }

    /**
     * Autentica un intento de inicio de sesion.
     *
     * @param claveTextoPlano   la clave ingresada por el usuario, en texto plano.
     * @param claveHasheadaBD   la clave hasheada almacenada en la base de datos
     *                          para ese usuario (null si el usuario no existe).
     * @return MENSAJE_AUTENTICACION_EXITOSA si las credenciales son
     *         correctas; MENSAJE_AUTENTICACION_FALLIDA en caso
     *         contrario (usuario inexistente o clave incorrecta).
     */
    public static String autenticar(String claveTextoPlano, String claveHasheadaBD) {
        if (claveTextoPlano == null || claveHasheadaBD == null) {
            return MENSAJE_AUTENTICACION_FALLIDA;
        }
        String claveIngresadaHasheada = UtilidadClave.hashear(claveTextoPlano);
        if (claveIngresadaHasheada.equals(claveHasheadaBD)) {
            return MENSAJE_AUTENTICACION_EXITOSA;
        }
        return MENSAJE_AUTENTICACION_FALLIDA;
    }
}
