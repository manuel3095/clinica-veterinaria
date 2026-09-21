import java.sql.*;

/**
 * Arnes de verificacion independiente de Spring. No reemplaza una
 * ejecucion real de la aplicacion Spring Boot (que requiere Maven
 * Central, no disponible en este entorno), pero valida con una base
 * de datos MariaDB real que el esquema de la tabla "mascotas" y las
 * operaciones equivalentes a las que generaria Spring Data JPA
 * (save/findAll/findById/deleteById) funcionan correctamente contra
 * las columnas mapeadas en la entidad Mascota.
 */
public class VerificacionEsquema {
    public static void main(String[] args) throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/clinicaveterinaria?useSSL=false";
        try (Connection con = DriverManager.getConnection(url, "root", "root")) {

            System.out.println("=== 1) INSERT equivalente a mascotaRepository.save(nuevaMascota) ===");
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO mascotas (nom_mascota, estado, especie, edad, raza) VALUES (?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Luna");
                ps.setString(2, "Estable");
                ps.setString(3, "Felis catus");
                ps.setString(4, "1");
                ps.setString(5, "Siames");
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) System.out.println("Insertado con id_mascota = " + rs.getLong(1));
                }
            }

            System.out.println("=== 2) SELECT equivalente a mascotaRepository.findAll() ===");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT id_mascota, nom_mascota, estado FROM mascotas")) {
                while (rs.next()) {
                    System.out.printf("  id=%d nombre=%s estado=%s%n",
                            rs.getLong(1), rs.getString(2), rs.getString(3));
                }
            }

            System.out.println("=== 3) UPDATE equivalente a modificarMascota() (findById + save) ===");
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE mascotas SET estado=? WHERE nom_mascota=?")) {
                ps.setString(1, "Critico");
                ps.setString(2, "Luna");
                int filas = ps.executeUpdate();
                System.out.println("Filas actualizadas: " + filas);
            }

            System.out.println("=== 4) DELETE equivalente a mascotaRepository.deleteById() ===");
            try (PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM mascotas WHERE nom_mascota=?")) {
                ps.setString(1, "Luna");
                int filas = ps.executeUpdate();
                System.out.println("Filas eliminadas: " + filas);
            }

            System.out.println("=== 5) Estado final de la tabla ===");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM mascotas")) {
                while (rs.next()) {
                    System.out.printf("  id=%d nombre=%s estado=%s especie=%s edad=%s raza=%s%n",
                            rs.getLong("id_mascota"), rs.getString("nom_mascota"),
                            rs.getString("estado"), rs.getString("especie"),
                            rs.getString("edad"), rs.getString("raza"));
                }
            }
            System.out.println("VERIFICACION COMPLETA: OK");
        }
    }
}
