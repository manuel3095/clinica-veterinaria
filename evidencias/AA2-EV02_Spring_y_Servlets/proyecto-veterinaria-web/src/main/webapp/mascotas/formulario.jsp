<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clinica Veterinaria - <c:out value="${modoFormulario == 'editar' ? 'Editar' : 'Nueva'}"/> Mascota</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/recursos/css/estilos.css">
</head>
<body>
    <div class="contenedor">
        <h1>
            <% if ("editar".equals(request.getAttribute("modoFormulario"))) { %>
                Editar Mascota
            <% } else { %>
                Registrar Nueva Mascota
            <% } %>
        </h1>

        <%-- Formulario HTML atendido por el servlet MascotaServlet mediante POST --%>
        <form class="formulario" action="${pageContext.request.contextPath}/mascotas" method="post">

            <% if ("editar".equals(request.getAttribute("modoFormulario"))) { %>
                <input type="hidden" name="accion" value="actualizar">
                <input type="hidden" name="idMascota" value="${mascota.idMascota}">
            <% } else { %>
                <input type="hidden" name="accion" value="guardar">
            <% } %>

            <label for="nomMascota">Nombre:</label>
            <input type="text" id="nomMascota" name="nomMascota" value="${mascota.nomMascota}" required>

            <label for="estado">Estado:</label>
            <select id="estado" name="estado" required>
                <option value="Estable"     ${mascota.estado == 'Estable' ? 'selected' : ''}>Estable</option>
                <option value="Critico"     ${mascota.estado == 'Critico' ? 'selected' : ''}>Critico</option>
                <option value="Hospitalizado" ${mascota.estado == 'Hospitalizado' ? 'selected' : ''}>Hospitalizado</option>
            </select>

            <label for="especie">Especie:</label>
            <input type="text" id="especie" name="especie" value="${mascota.especie}" required>

            <label for="edad">Edad:</label>
            <input type="text" id="edad" name="edad" value="${mascota.edad}" required>

            <label for="raza">Raza:</label>
            <input type="text" id="raza" name="raza" value="${mascota.raza}" required>

            <div class="acciones-formulario">
                <button type="submit" class="boton">Guardar</button>
                <a class="boton boton-secundario" href="${pageContext.request.contextPath}/mascotas?accion=listar">Cancelar</a>
            </div>
        </form>
    </div>
</body>
</html>
