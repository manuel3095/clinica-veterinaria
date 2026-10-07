import com.clinicaveterinaria.veterinaria.auth.AutenticacionLogica;
import com.clinicaveterinaria.veterinaria.validacion.MascotaValidador;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servidor HTTP de verificacion de la API de la Clinica Veterinaria
 * (evidencia GA7-220501096-AA5-EV02).
 *
 * NOTA DE HONESTIDAD: es un equivalente de AuthController.java y
 * MascotaController.java construido solo con el JDK (com.sun.net.httpserver),
 * porque Spring Boot no pudo descargarse/ejecutarse en el entorno donde se
 * prepararon las pruebas. Reutiliza las clases REALES del proyecto
 * (AutenticacionLogica y MascotaValidador) y la base de datos MariaDB real,
 * y respeta las mismas rutas, metodos, codigos HTTP y mensajes. Con Spring
 * Boot en marcha, las mismas peticiones de la coleccion Postman se envian
 * al mismo puerto 3000 sin ningun cambio.
 *
 * Uso: java -cp ... ServidorPruebasAPI [puerto]
 */
public class ServidorPruebasAPI {

    static final String URL = "jdbc:mariadb://localhost:3306/clinicaveterinaria";
    static final String USR = "root";
    static final String PWD = "root";

    public static void main(String[] args) throws Exception {
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : 3000;
        HttpServer s = HttpServer.create(new InetSocketAddress(puerto), 0);
        s.createContext("/auth/registro", ex -> seguro(ex, "POST", ServidorPruebasAPI::registro));
        s.createContext("/auth/login", ex -> seguro(ex, "POST", ServidorPruebasAPI::login));
        s.createContext("/mascotas/nuevo", ex -> seguro(ex, "POST", ServidorPruebasAPI::nuevaMascota));
        s.createContext("/mascotas/mostrar", ex -> seguro(ex, "GET", ServidorPruebasAPI::mostrar));
        s.createContext("/mascotas/modificar", ex -> seguro(ex, "POST", ServidorPruebasAPI::modificar));
        s.createContext("/auth/usuarios", ex -> seguro(ex, "GET", ServidorPruebasAPI::usuarios));
        s.createContext("/mascotas/propietario", ex -> seguro(ex, "GET", ServidorPruebasAPI::porPropietario));
        s.createContext("/mascotas/estado/", ex -> seguro(ex, "GET", ServidorPruebasAPI::porEstado));
        s.createContext("/mascotas/", ex -> {
            String p = ex.getRequestURI().getPath();
            if (p.matches("/mascotas/\\d+")) {
                if ("GET".equals(ex.getRequestMethod())) seguro(ex, "GET", ServidorPruebasAPI::porId);
                else seguro(ex, "POST", ServidorPruebasAPI::eliminar);
            } else responder(ex, 404, "Not Found", "text/plain");
        });
        s.start();
        System.out.println("Servidor de pruebas escuchando en http://localhost:" + puerto);
    }

    interface Manejador { void tratar(HttpExchange ex, String cuerpo) throws Exception; }

    static void seguro(HttpExchange ex, String metodo, Manejador m) throws IOException {
        try {
            if (!metodo.equals(ex.getRequestMethod())) {
                responder(ex, 405, "Method Not Allowed", "text/plain");
                return;
            }
            m.tratar(ex, leer(ex.getRequestBody()));
        } catch (Exception e) {
            responder(ex, 500, "Error interno: " + e.getMessage(), "text/plain");
        }
    }

    static String leer(InputStream in) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        in.transferTo(b);
        return b.toString(StandardCharsets.UTF_8);
    }

    static void responder(HttpExchange ex, int codigo, String cuerpo, String tipo) throws IOException {
        byte[] d = cuerpo.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", tipo + "; charset=UTF-8");
        ex.sendResponseHeaders(codigo, d.length == 0 ? -1 : d.length);
        if (d.length > 0) ex.getResponseBody().write(d);
        ex.close();
    }

    /** Extrae un campo de un JSON plano; null si no existe o es null. */
    static String campo(String json, String nombre) {
        Matcher m = Pattern.compile("\"" + nombre + "\"\\s*:\\s*(?:\"((?:[^\"\\\\]|\\\\.)*)\"|(-?\\d+))").matcher(json);
        if (!m.find()) return null;
        return m.group(1) != null ? m.group(1).replace("\\\"", "\"") : m.group(2);
    }

    static Connection con() throws Exception {
        return DriverManager.getConnection(URL, USR, PWD);
    }

    // ---------------------------------------------------------------- AUTH
    static String buscarClave(String usuario) throws Exception {
        if (usuario == null) return null;
        try (Connection c = con(); PreparedStatement ps = c.prepareStatement(
                "SELECT clave FROM usuario WHERE nombre_usuario=?")) {
            ps.setString(1, usuario);
            ResultSet r = ps.executeQuery();
            return r.next() ? r.getString(1) : null;
        }
    }

    static void registro(HttpExchange ex, String cuerpo) throws Exception {
        String u = campo(cuerpo, "nombreUsuario"), k = campo(cuerpo, "clave");
        String err = AutenticacionLogica.validarRegistro(u, k, buscarClave(u) != null);
        if (err != null) { responder(ex, 400, err, "text/plain"); return; }
        try (Connection c = con(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO usuario (nombre_usuario, clave) VALUES (?,?)")) {
            ps.setString(1, u);
            ps.setString(2, AutenticacionLogica.prepararClaveParaAlmacenar(k));
            ps.executeUpdate();
        }
        responder(ex, 200, "Usuario registrado correctamente", "text/plain");
    }

    static void login(HttpExchange ex, String cuerpo) throws Exception {
        String r = AutenticacionLogica.autenticar(campo(cuerpo, "clave"), buscarClave(campo(cuerpo, "nombreUsuario")));
        boolean ok = AutenticacionLogica.MENSAJE_AUTENTICACION_EXITOSA.equals(r);
        responder(ex, ok ? 200 : 401, r, "text/plain");
    }

    // ------------------------------------------------------------ MASCOTAS
    static List<String> validar(String b) {
        LocalDate f = null;
        String fs = campo(b, "fechaIngreso");
        try { if (fs != null) f = LocalDate.parse(fs); } catch (Exception e) { f = null; }
        return MascotaValidador.validarMascota(campo(b, "nomMascota"), campo(b, "estado"),
                campo(b, "especie"), campo(b, "edad"), campo(b, "raza"), f, campo(b, "correoPropietario"));
    }

    static String json(String s) { return s == null ? "null" : "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\""; }

    static String jsonErrores(List<String> e) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < e.size(); i++) sb.append(i > 0 ? "," : "").append(json(e.get(i)));
        return sb.append("]").toString();
    }

    static String fila(ResultSet r) throws Exception {
        return "{\"idMascota\":" + r.getLong("id_mascota") + ",\"nomMascota\":" + json(r.getString("nom_mascota"))
                + ",\"estado\":" + json(r.getString("estado")) + ",\"especie\":" + json(r.getString("especie"))
                + ",\"edad\":" + json(r.getString("edad")) + ",\"raza\":" + json(r.getString("raza"))
                + ",\"fechaIngreso\":" + json(r.getString("fecha_ingreso"))
                + ",\"correoPropietario\":" + json(r.getString("correo_propietario")) + "}";
    }

    static void nuevaMascota(HttpExchange ex, String b) throws Exception {
        List<String> e = validar(b);
        if (!e.isEmpty()) { responder(ex, 400, jsonErrores(e), "application/json"); return; }
        try (Connection c = con(); PreparedStatement ps = c.prepareStatement(
                "INSERT INTO mascotas (nom_mascota,estado,especie,edad,raza,fecha_ingreso,correo_propietario) VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, campo(b, "nomMascota")); ps.setString(2, campo(b, "estado"));
            ps.setString(3, campo(b, "especie")); ps.setString(4, campo(b, "edad"));
            ps.setString(5, campo(b, "raza")); ps.setDate(6, Date.valueOf(campo(b, "fechaIngreso")));
            ps.setString(7, campo(b, "correoPropietario"));
            ps.executeUpdate();
            ResultSet k = ps.getGeneratedKeys(); k.next();
            try (PreparedStatement q = c.prepareStatement("SELECT * FROM mascotas WHERE id_mascota=?")) {
                q.setLong(1, k.getLong(1)); ResultSet r = q.executeQuery(); r.next();
                responder(ex, 200, fila(r), "application/json");
            }
        }
    }

    static void mostrar(HttpExchange ex, String b) throws Exception {
        StringBuilder sb = new StringBuilder("[");
        try (Connection c = con(); Statement st = c.createStatement()) {
            ResultSet r = st.executeQuery("SELECT * FROM mascotas ORDER BY id_mascota");
            boolean primero = true;
            while (r.next()) { sb.append(primero ? "" : ",").append(fila(r)); primero = false; }
        }
        responder(ex, 200, sb.append("]").toString(), "application/json");
    }

    static void modificar(HttpExchange ex, String b) throws Exception {
        List<String> e = validar(b);
        if (!e.isEmpty()) { responder(ex, 400, jsonErrores(e), "application/json"); return; }
        String id = campo(b, "idMascota");
        try (Connection c = con(); PreparedStatement ps = c.prepareStatement(
                "UPDATE mascotas SET nom_mascota=?,estado=?,especie=?,edad=?,raza=?,fecha_ingreso=?,correo_propietario=? WHERE id_mascota=?")) {
            ps.setString(1, campo(b, "nomMascota")); ps.setString(2, campo(b, "estado"));
            ps.setString(3, campo(b, "especie")); ps.setString(4, campo(b, "edad"));
            ps.setString(5, campo(b, "raza")); ps.setDate(6, Date.valueOf(campo(b, "fechaIngreso")));
            ps.setString(7, campo(b, "correoPropietario")); ps.setLong(8, id == null ? -1 : Long.parseLong(id));
            ps.executeUpdate();
            try (PreparedStatement q = c.prepareStatement("SELECT * FROM mascotas WHERE id_mascota=?")) {
                q.setLong(1, id == null ? -1 : Long.parseLong(id)); ResultSet r = q.executeQuery();
                responder(ex, 200, r.next() ? fila(r) : "null", "application/json");
            }
        }
    }

    static void eliminar(HttpExchange ex, String b) throws Exception {
        long id = Long.parseLong(ex.getRequestURI().getPath().substring("/mascotas/".length()));
        try (Connection c = con(); PreparedStatement ps = c.prepareStatement("DELETE FROM mascotas WHERE id_mascota=?")) {
            ps.setLong(1, id);
            responder(ex, 200, String.valueOf(ps.executeUpdate() > 0), "application/json");
        }
    }

    // ------------------------------------------------- servicios EV03
    static void usuarios(HttpExchange ex, String b) throws Exception {
        StringBuilder sb = new StringBuilder("[");
        try (Connection c = con(); Statement st = c.createStatement()) {
            ResultSet r = st.executeQuery("SELECT id_usuario, nombre_usuario FROM usuario ORDER BY id_usuario");
            boolean p = true;
            while (r.next()) {
                sb.append(p ? "" : ",").append("{\"idUsuario\":").append(r.getLong(1))
                  .append(",\"nombreUsuario\":").append(json(r.getString(2))).append("}");
                p = false;
            }
        }
        responder(ex, 200, sb.append("]").toString(), "application/json");
    }

    static void listar(HttpExchange ex, String where, String valor) throws Exception {
        StringBuilder sb = new StringBuilder("[");
        try (Connection c = con(); PreparedStatement ps = c.prepareStatement(
                "SELECT * FROM mascotas WHERE " + where + "=? ORDER BY id_mascota")) {
            ps.setString(1, valor);
            ResultSet r = ps.executeQuery();
            boolean p = true;
            while (r.next()) { sb.append(p ? "" : ",").append(fila(r)); p = false; }
        }
        responder(ex, 200, sb.append("]").toString(), "application/json");
    }

    static void porEstado(HttpExchange ex, String b) throws Exception {
        String est = java.net.URLDecoder.decode(ex.getRequestURI().getPath().substring("/mascotas/estado/".length()), "UTF-8");
        listar(ex, "estado", est);
    }

    static void porPropietario(HttpExchange ex, String b) throws Exception {
        String q = ex.getRequestURI().getQuery();
        Matcher m = Pattern.compile("(?:^|&)correo=([^&]*)").matcher(q == null ? "" : q);
        if (!m.find()) { responder(ex, 400, "Falta el parametro correo", "text/plain"); return; }
        listar(ex, "correo_propietario", java.net.URLDecoder.decode(m.group(1), "UTF-8"));
    }

    static void porId(HttpExchange ex, String b) throws Exception {
        long id = Long.parseLong(ex.getRequestURI().getPath().substring("/mascotas/".length()));
        try (Connection c = con(); PreparedStatement ps = c.prepareStatement("SELECT * FROM mascotas WHERE id_mascota=?")) {
            ps.setLong(1, id);
            ResultSet r = ps.executeQuery();
            if (r.next()) responder(ex, 200, fila(r), "application/json");
            else responder(ex, 404, "", "application/json");
        }
    }
}
