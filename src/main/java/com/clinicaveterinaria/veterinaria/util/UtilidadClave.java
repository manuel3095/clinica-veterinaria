package com.clinicaveterinaria.veterinaria.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utilidad para el hash de contrasenas de usuario mediante SHA-256,
 * de forma que la clave nunca se almacene en texto plano en la base
 * de datos (requisito de seguridad exigido por la evidencia).
 */
public class UtilidadClave {

    private UtilidadClave() {
        // Clase utilitaria: no debe instanciarse.
    }

    /**
     * Calcula el hash SHA-256 de una clave en texto plano y lo
     * retorna como una cadena hexadecimal.
     */
    public static String hashear(String claveTextoPlano) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(claveTextoPlano.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder();
            for (byte b : hash) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException excepcion) {
            throw new IllegalStateException("Algoritmo SHA-256 no disponible en esta JVM.", excepcion);
        }
    }
}
