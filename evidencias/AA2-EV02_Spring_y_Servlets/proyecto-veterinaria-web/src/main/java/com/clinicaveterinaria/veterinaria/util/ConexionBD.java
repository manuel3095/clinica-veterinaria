package com.clinicaveterinaria.veterinaria.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase utilitaria encargada de abrir la conexion JDBC hacia la base de
 * datos MySQL/MariaDB "clinicaveterinaria".
 *
 * Los parametros de conexion se dejaron como constantes para facilitar la
 * evidencia academica; en un entorno real se recomienda externalizarlos en
 * un archivo de propiedades (application.properties / context.xml).
 */
public class ConexionBD {

    private static final String URL_BASE_DATOS =
            "jdbc:mysql://localhost:3306/clinicaveterinaria?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO_BASE_DATOS = "veterinaria_user";
    private static final String CLAVE_BASE_DATOS = "Veterinaria2024*";
    private static final String DRIVER_JDBC_MYSQL = "com.mysql.cj.jdbc.Driver";
    private static final String DRIVER_JDBC_MARIADB = "org.mariadb.jdbc.Driver";

    private ConexionBD() {
        // Clase utilitaria: no debe instanciarse.
    }

    /**
     * Abre y retorna una nueva conexion JDBC hacia la base de datos.
     *
     * Intenta primero cargar el driver oficial de MySQL Connector/J
     * (com.mysql.cj.jdbc.Driver). Si el proyecto se ejecuta con el
     * conector de MariaDB en su lugar, se hace una segunda tentativa con
     * org.mariadb.jdbc.Driver, ya que ambos aceptan URL con el prefijo
     * "jdbc:mysql:".
     *
     * @return conexion activa hacia la base de datos clinicaveterinaria.
     * @throws SQLException si ocurre un error al establecer la conexion.
     */
    public static Connection obtenerConexion() throws SQLException {
        try {
            Class.forName(DRIVER_JDBC_MYSQL);
        } catch (ClassNotFoundException excepcionMySql) {
            try {
                Class.forName(DRIVER_JDBC_MARIADB);
            } catch (ClassNotFoundException excepcionMariaDb) {
                throw new SQLException("No se encontro un driver JDBC compatible con MySQL/MariaDB en el classpath.", excepcionMariaDb);
            }
        }
        return DriverManager.getConnection(URL_BASE_DATOS, USUARIO_BASE_DATOS, CLAVE_BASE_DATOS);
    }
}
