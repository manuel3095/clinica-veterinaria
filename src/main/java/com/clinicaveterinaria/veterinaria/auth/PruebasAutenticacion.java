package com.clinicaveterinaria.veterinaria.auth;

/**
 * Programa de pruebas de la logica de registro e inicio de sesion.
 *
 * Ejecuta casos de prueba positivos y negativos sobre
 * AutenticacionLogica, cubriendo el requisito central de la
 * evidencia: registro de usuario y clave, y autenticacion exitosa o
 * fallida al iniciar sesion. No depende de Spring ni de la base de
 * datos: puede compilarse y ejecutarse con las herramientas basicas
 * del JDK (javac / java).
 */
public class PruebasAutenticacion {

    private static int casosTotales = 0;
    private static int casosAprobados = 0;

    public static void main(String[] args) {

        System.out.println("=========================================================");
        System.out.println(" PRUEBAS DE AUTENTICACION - Registro e Inicio de Sesion");
        System.out.println("=========================================================\n");

        System.out.println("--- Categoria: REGISTRO ---");
        casoRegistro("Registro valido ('recepcion1' / 'clave1234')",
                AutenticacionLogica.validarRegistro("recepcion1", "clave1234", false), true);
        casoRegistro("Usuario ya existente debe rechazarse",
                AutenticacionLogica.validarRegistro("recepcion1", "clave1234", true), false);
        casoRegistro("Nombre de usuario demasiado corto ('re') debe rechazarse",
                AutenticacionLogica.validarRegistro("re", "clave1234", false), false);
        casoRegistro("Contrasena demasiado corta ('123') debe rechazarse",
                AutenticacionLogica.validarRegistro("recepcion2", "123", false), false);
        casoRegistro("Nombre de usuario vacio debe rechazarse",
                AutenticacionLogica.validarRegistro("", "clave1234", false), false);

        System.out.println("\n--- Categoria: INICIO DE SESION (autenticacion correcta) ---");
        String hashAlmacenado = AutenticacionLogica.prepararClaveParaAlmacenar("clave1234");
        casoLogin("Clave correcta debe autenticar satisfactoriamente",
                AutenticacionLogica.autenticar("clave1234", hashAlmacenado),
                AutenticacionLogica.MENSAJE_AUTENTICACION_EXITOSA);

        System.out.println("\n--- Categoria: INICIO DE SESION (autenticacion fallida) ---");
        casoLogin("Clave incorrecta debe devolver error en la autenticacion",
                AutenticacionLogica.autenticar("claveIncorrecta", hashAlmacenado),
                AutenticacionLogica.MENSAJE_AUTENTICACION_FALLIDA);
        casoLogin("Usuario inexistente (sin hash almacenado) debe devolver error",
                AutenticacionLogica.autenticar("cualquierClave", null),
                AutenticacionLogica.MENSAJE_AUTENTICACION_FALLIDA);
        casoLogin("Clave vacia debe devolver error en la autenticacion",
                AutenticacionLogica.autenticar("", hashAlmacenado),
                AutenticacionLogica.MENSAJE_AUTENTICACION_FALLIDA);

        System.out.println("\n--- Categoria: SEGURIDAD (la clave nunca se almacena en texto plano) ---");
        boolean claveNoEsTextoPlano = !hashAlmacenado.equals("clave1234");
        boolean hashTieneLongitudEsperada = hashAlmacenado.length() == 64; // SHA-256 en hexadecimal
        casoBooleano("El hash almacenado es distinto de la clave en texto plano", claveNoEsTextoPlano);
        casoBooleano("El hash SHA-256 tiene la longitud hexadecimal esperada (64)", hashTieneLongitudEsperada);

        System.out.println("\n=========================================================");
        System.out.println(" RESULTADO FINAL: " + casosAprobados + " / " + casosTotales + " casos aprobados");
        System.out.println("=========================================================");

        if (casosAprobados != casosTotales) {
            System.exit(1);
        }
    }

    private static void casoRegistro(String descripcion, String resultadoValidacion, boolean debeSerValido) {
        casosTotales++;
        boolean esValido = (resultadoValidacion == null);
        boolean aprobado = esValido == debeSerValido;
        if (aprobado) casosAprobados++;
        System.out.println((aprobado ? "[APROBADO] " : "[FALLIDO]  ") + descripcion);
        if (!esValido) {
            System.out.println("           -> " + resultadoValidacion);
        }
    }

    private static void casoLogin(String descripcion, String resultadoObtenido, String resultadoEsperado) {
        casosTotales++;
        boolean aprobado = resultadoObtenido.equals(resultadoEsperado);
        if (aprobado) casosAprobados++;
        System.out.println((aprobado ? "[APROBADO] " : "[FALLIDO]  ") + descripcion
                + " -> " + resultadoObtenido);
    }

    private static void casoBooleano(String descripcion, boolean condicion) {
        casosTotales++;
        if (condicion) casosAprobados++;
        System.out.println((condicion ? "[APROBADO] " : "[FALLIDO]  ") + descripcion);
    }
}
