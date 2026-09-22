import java.sql.*;

/**
 * Arnes de verificacion independiente de Spring, en la misma linea
 * usada en las evidencias anteriores: valida contra una base de datos
 * MariaDB real que el esquema extendido de la tabla "mascotas"
 * (incluyendo las columnas nuevas fecha_ingreso y correo_propietario,
 * agregadas para esta evidencia) y las operaciones equivalentes a las
 * que generaria Spring Data JPA funcionan correctamente.
 */
public class VerificacionEsquemaMascota {
    public static void main(String[] args) throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/clinicaveterinaria?useSSL=false";
        try (Connection con = DriverManager.getConnection(url, "root", "root")) {

            System.out.println("=== 1) INSERT equivalente a mascotaRepository.save(nuevaMascota) ===");
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO mascotas (nom_mascota, estado, especie, edad, raza, fecha_ingreso, correo_propietario) "
                    + "VALUES (?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Luna");
                ps.setString(2, "Estable");
                ps.setString(3, "Felis catus");
                ps.setString(4, "4");
                ps.setString(5, "Siames");
                ps.setDate(6, Date.valueOf("2021-06-01"));
                ps.setString(7, "luna.duena@email.com");
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) System.out.println("Insertado con id_mascota = " + rs.getLong(1));
                }
            }

            System.out.println("=== 2) SELECT equivalente a mascotaRepository.findAll() ===");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(
                         "SELECT id_mascota, nom_mascota, fecha_ingreso, correo_propietario FROM mascotas")) {
                while (rs.next()) {
                    System.out.printf("  id=%d nombre=%s fecha_ingreso=%s correo_propietario=%s%n",
                            rs.getLong(1), rs.getString(2), rs.getDate(3), rs.getString(4));
                }
            }

            System.out.println("=== 3) UPDATE equivalente a modificarMascota() (findById + save) ===");
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE mascotas SET estado=? WHERE nom_mascota=?")) {
                ps.setString(1, "Critico");
                ps.setString(2, "Luna");
                System.out.println("Filas actualizadas: " + ps.executeUpdate());
            }

            System.out.println("=== 4) DELETE equivalente a mascotaRepository.deleteById() ===");
            try (PreparedStatement ps = con.prepareStatement("DELETE FROM mascotas WHERE nom_mascota=?")) {
                ps.setString(1, "Luna");
                System.out.println("Filas eliminadas: " + ps.executeUpdate());
            }

            System.out.println("=== 5) Estado final de la tabla ===");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM mascotas")) {
                while (rs.next()) {
                    System.out.printf("  id=%d nombre=%s estado=%s fecha_ingreso=%s correo_propietario=%s%n",
                            rs.getLong("id_mascota"), rs.getString("nom_mascota"),
                            rs.getString("estado"), rs.getDate("fecha_ingreso"),
                            rs.getString("correo_propietario"));
                }
            }
            System.out.println("VERIFICACION COMPLETA: OK");
        }
    }
}
