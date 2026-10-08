package com.clinicaveterinaria.veterinaria.controlador;

import com.clinicaveterinaria.veterinaria.dao.MascotaDAO;
import com.clinicaveterinaria.veterinaria.dao.MascotaDAOImpl;
import com.clinicaveterinaria.veterinaria.modelo.Mascota;
import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet controlador del modulo de Mascotas. Centraliza las operaciones
 * del CRUD (crear, listar, actualizar, eliminar) atendiendo peticiones
 * HTTP GET (para navegacion y consulta) y POST (para el envio de los
 * formularios HTML), de acuerdo con lo exigido en la evidencia.
 *
 * Rutas atendidas (parametro "accion"):
 *   GET  /mascotas?accion=listar         -> lista todas las mascotas
 *   GET  /mascotas?accion=nuevo          -> muestra el formulario de creacion
 *   GET  /mascotas?accion=editar&id=N    -> muestra el formulario de edicion
 *   POST /mascotas?accion=guardar        -> inserta una nueva mascota
 *   POST /mascotas?accion=actualizar     -> actualiza una mascota existente
 *   POST /mascotas?accion=eliminar       -> elimina una mascota por id
 */
@WebServlet(name = "MascotaServlet", urlPatterns = {"/mascotas"})
public class MascotaServlet extends HttpServlet {

    private final MascotaDAO mascotaDAO = new MascotaDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty()) {
            accion = "listar";
        }

        try {
            switch (accion) {
                case "nuevo":
                    mostrarFormularioCreacion(request, response);
                    break;
                case "editar":
                    mostrarFormularioEdicion(request, response);
                    break;
                case "listar":
                default:
                    listarMascotas(request, response);
                    break;
            }
        } catch (SQLException excepcion) {
            request.setAttribute("mensajeError", "Error de base de datos: " + excepcion.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "";
        }

        try {
            switch (accion) {
                case "actualizar":
                    actualizarMascota(request, response);
                    break;
                case "eliminar":
                    eliminarMascota(request, response);
                    break;
                case "guardar":
                default:
                    guardarMascota(request, response);
                    break;
            }
        } catch (SQLException excepcion) {
            request.setAttribute("mensajeError", "Error de base de datos: " + excepcion.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * Consulta todas las mascotas (metodo GET) y las envia a la vista
     * listar.jsp para su presentacion en una tabla HTML.
     */
    private void listarMascotas(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        request.setAttribute("listaMascotas", mascotaDAO.consultarTodas());
        request.getRequestDispatcher("/mascotas/listar.jsp").forward(request, response);
    }

    /**
     * Muestra el formulario HTML vacio para registrar una nueva mascota
     * (metodo GET).
     */
    private void mostrarFormularioCreacion(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("modoFormulario", "crear");
        request.setAttribute("mascota", new Mascota());
        request.getRequestDispatcher("/mascotas/formulario.jsp").forward(request, response);
    }

    /**
     * Consulta la mascota indicada por el parametro "id" y muestra el
     * formulario HTML precargado con sus datos (metodo GET).
     */
    private void mostrarFormularioEdicion(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        Long idMascota = Long.valueOf(request.getParameter("id"));
        Mascota mascota = mascotaDAO.consultarPorId(idMascota);

        request.setAttribute("modoFormulario", "editar");
        request.setAttribute("mascota", mascota);
        request.getRequestDispatcher("/mascotas/formulario.jsp").forward(request, response);
    }

    /**
     * Recibe los datos del formulario HTML (metodo POST) y crea un nuevo
     * registro de mascota en la base de datos.
     */
    private void guardarMascota(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Mascota mascota = leerMascotaDesdeParametros(request);
        mascotaDAO.insertarMascota(mascota);
        response.sendRedirect("mascotas?accion=listar");
    }

    /**
     * Recibe los datos del formulario HTML (metodo POST) y actualiza el
     * registro de mascota correspondiente.
     */
    private void actualizarMascota(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Mascota mascota = leerMascotaDesdeParametros(request);
        mascota.setIdMascota(Long.valueOf(request.getParameter("idMascota")));
        mascotaDAO.actualizarMascota(mascota);
        response.sendRedirect("mascotas?accion=listar");
    }

    /**
     * Elimina el registro de mascota indicado por el parametro "id".
     */
    private void eliminarMascota(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        Long idMascota = Long.valueOf(request.getParameter("id"));
        mascotaDAO.eliminarMascota(idMascota);
        response.sendRedirect("mascotas?accion=listar");
    }

    /**
     * Construye un objeto Mascota a partir de los parametros enviados
     * desde el formulario HTML (formulario.jsp).
     */
    private Mascota leerMascotaDesdeParametros(HttpServletRequest request) {
        Mascota mascota = new Mascota();
        mascota.setNomMascota(request.getParameter("nomMascota"));
        mascota.setEstado(request.getParameter("estado"));
        mascota.setEspecie(request.getParameter("especie"));
        mascota.setEdad(request.getParameter("edad"));
        mascota.setRaza(request.getParameter("raza"));
        return mascota;
    }
}
