package com.clinicaveterinaria.veterinaria.dao;

import com.clinicaveterinaria.veterinaria.modelo.Mascota;
import com.clinicaveterinaria.veterinaria.util.ConexionBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion JDBC de {@link MascotaDAO}. Utiliza unicamente
 * PreparedStatement para prevenir inyeccion SQL y try-with-resources para
 * garantizar el cierre de conexiones, statements y result sets.
 */
public class MascotaDAOImpl implements MascotaDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO mascotas (nom_mascota, estado, especie, edad, raza) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_CONSULTAR_TODAS =
            "SELECT id_mascota, nom_mascota, estado, especie, edad, raza FROM mascotas ORDER BY id_mascota";
    private static final String SQL_CONSULTAR_POR_ID =
            "SELECT id_mascota, nom_mascota, estado, especie, edad, raza FROM mascotas WHERE id_mascota = ?";
    private static final String SQL_ACTUALIZAR =
            "UPDATE mascotas SET nom_mascota = ?, estado = ?, especie = ?, edad = ?, raza = ? WHERE id_mascota = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM mascotas WHERE id_mascota = ?";

    @Override
    public Mascota insertarMascota(Mascota mascota) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {

            sentencia.setString(1, mascota.getNomMascota());
            sentencia.setString(2, mascota.getEstado());
            sentencia.setString(3, mascota.getEspecie());
            sentencia.setString(4, mascota.getEdad());
            sentencia.setString(5, mascota.getRaza());
            sentencia.executeUpdate();

            try (ResultSet clavesGeneradas = sentencia.getGeneratedKeys()) {
                if (clavesGeneradas.next()) {
                    mascota.setIdMascota(clavesGeneradas.getLong(1));
                }
            }
        }
        return mascota;
    }

    @Override
    public List<Mascota> consultarTodas() throws SQLException {
        List<Mascota> listaMascotas = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_TODAS);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                listaMascotas.add(mapearMascota(resultado));
            }
        }
        return listaMascotas;
    }

    @Override
    public Mascota consultarPorId(Long idMascota) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_POR_ID)) {

            sentencia.setLong(1, idMascota);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return mapearMascota(resultado);
                }
            }
        }
        return null;
    }

    @Override
    public boolean actualizarMascota(Mascota mascota) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {

            sentencia.setString(1, mascota.getNomMascota());
            sentencia.setString(2, mascota.getEstado());
            sentencia.setString(3, mascota.getEspecie());
            sentencia.setString(4, mascota.getEdad());
            sentencia.setString(5, mascota.getRaza());
            sentencia.setLong(6, mascota.getIdMascota());

            return sentencia.executeUpdate() > 0;
        }
    }

    @Override
    public boolean eliminarMascota(Long idMascota) throws SQLException {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {

            sentencia.setLong(1, idMascota);
            return sentencia.executeUpdate() > 0;
        }
    }

    /**
     * Mapea la fila actual de un ResultSet a un objeto Mascota.
     */
    private Mascota mapearMascota(ResultSet resultado) throws SQLException {
        Mascota mascota = new Mascota();
        mascota.setIdMascota(resultado.getLong("id_mascota"));
        mascota.setNomMascota(resultado.getString("nom_mascota"));
        mascota.setEstado(resultado.getString("estado"));
        mascota.setEspecie(resultado.getString("especie"));
        mascota.setEdad(resultado.getString("edad"));
        mascota.setRaza(resultado.getString("raza"));
        return mascota;
    }
}
