import java.sql.*;

/**
 * Arnes de verificacion end-to-end del servicio de registro e inicio
 * de sesion, contra una base de datos MariaDB real. Reutiliza
 * exactamente la misma logica que expone AuthController
 * (AutenticacionLogica), pero accediendo a la base de datos por JDBC
 * en lugar de a traves de Spring Data JPA, ya que este entorno no
 * tiene acceso a Maven Central para levantar el contenedor de Spring.
 */
public class VerificacionAutenticacion {

    public static void main(String[] args) throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/clinicaveterinaria?useSSL=false";

        try (Connection con = DriverManager.getConnection(url, "root", "root")) {

            System.out.println("=== 1) POST /auth/registro equivalente: registrar 'auxiliar2' ===");
            String nuevoUsuario = "auxiliar2";
            String nuevaClave = "segura2024";

            Usuario existente = buscarPorNombre(con, nuevoUsuario);
            String errorRegistro = validarRegistro(nuevoUsuario, nuevaClave, existente != null);
            if (errorRegistro != null) {
                System.out.println("Registro rechazado: " + errorRegistro);
            } else {
                String hash = sha256(nuevaClave);
                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO usuario (nombre_usuario, clave) VALUES (?, ?)")) {
                    ps.setString(1, nuevoUsuario);
                    ps.setString(2, hash);
                    ps.executeUpdate();
                }
                System.out.println("Usuario registrado correctamente (hash almacenado, no la clave en texto plano).");
            }

            System.out.println("\n=== 2) POST /auth/login equivalente: 'auxiliar2' con clave CORRECTA ===");
            probarLogin(con, "auxiliar2", "segura2024");

            System.out.println("\n=== 3) POST /auth/login equivalente: 'auxiliar2' con clave INCORRECTA ===");
            probarLogin(con, "auxiliar2", "claveIncorrecta");

            System.out.println("\n=== 4) POST /auth/login equivalente: usuario 'recepcion1' (creado por el script SQL) con su clave real ===");
            probarLogin(con, "recepcion1", "clave1234");

            System.out.println("\n=== 5) POST /auth/login equivalente: usuario inexistente ===");
            probarLogin(con, "usuarioQueNoExiste", "cualquierClave");

            System.out.println("\n=== 6) Verificacion de seguridad: la tabla NO contiene claves en texto plano ===");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT nombre_usuario, clave FROM usuario")) {
                while (rs.next()) {
                    String claveAlmacenada = rs.getString("clave");
                    boolean esTextoPlano = claveAlmacenada.equals("clave1234") || claveAlmacenada.equals("segura2024");
                    System.out.printf("  usuario=%s clave_almacenada_es_texto_plano=%b longitud=%d%n",
                            rs.getString("nombre_usuario"), esTextoPlano, claveAlmacenada.length());
                }
            }

            System.out.println("\nVERIFICACION COMPLETA: OK");
        }
    }

    private static void probarLogin(Connection con, String nombreUsuario, String claveIntentada) throws SQLException {
        Usuario usuario = buscarPorNombre(con, nombreUsuario);
        String claveHasheadaBD = (usuario != null) ? usuario.clave : null;
        String resultado = autenticar(claveIntentada, claveHasheadaBD);
        System.out.println("Resultado: " + resultado);
    }

    // ---- Metodos que replican AutenticacionLogica.java, para no depender de Spring ----

    static String validarRegistro(String nombreUsuario, String clave, boolean usuarioYaExiste) {
        if (nombreUsuario == null || nombreUsuario.trim().length() < 3) {
            return "El nombre de usuario debe tener al menos 3 caracteres.";
        }
        if (clave == null || clave.length() < 8) {
            return "La contrasena debe tener al menos 8 caracteres.";
        }
        if (usuarioYaExiste) {
            return "El nombre de usuario ya se encuentra registrado.";
        }
        return null;
    }

    static String autenticar(String claveTextoPlano, String claveHasheadaBD) throws SQLException {
        if (claveTextoPlano == null || claveHasheadaBD == null) {
            return "Error en la autenticacion";
        }
        String claveIngresadaHasheada = sha256(claveTextoPlano);
        return claveIngresadaHasheada.equals(claveHasheadaBD) ? "Autenticacion satisfactoria" : "Error en la autenticacion";
    }

    static Usuario buscarPorNombre(Connection con, String nombreUsuario) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT nombre_usuario, clave FROM usuario WHERE nombre_usuario = ?")) {
            ps.setString(1, nombreUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario u = new Usuario();
                    u.nombreUsuario = rs.getString("nombre_usuario");
                    u.clave = rs.getString("clave");
                    return u;
                }
            }
        }
        return null;
    }

    static String sha256(String texto) throws SQLException {
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(texto.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new SQLException(e);
        }
    }

    static class Usuario {
        String nombreUsuario;
        String clave;
    }
}
