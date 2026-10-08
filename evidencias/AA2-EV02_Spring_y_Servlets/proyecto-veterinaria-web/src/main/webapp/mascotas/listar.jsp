<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clinica Veterinaria - Listado de Mascotas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/recursos/css/estilos.css">
</head>
<body>
    <div class="contenedor">
        <h1>Clinica Veterinaria - Mascotas</h1>

        <p><a class="boton" href="${pageContext.request.contextPath}/mascotas?accion=nuevo">+ Nueva mascota</a></p>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Estado</th>
                    <th>Especie</th>
                    <th>Edad</th>
                    <th>Raza</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${empty listaMascotas}">
                        <tr>
                            <td colspan="7" class="sin-datos">No hay mascotas registradas todavia.</td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="mascota" items="${listaMascotas}">
                            <tr>
                                <td>${mascota.idMascota}</td>
                                <td>${mascota.nomMascota}</td>
                                <td>${mascota.estado}</td>
                                <td>${mascota.especie}</td>
                                <td>${mascota.edad}</td>
                                <td>${mascota.raza}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/mascotas?accion=editar&id=${mascota.idMascota}">Editar</a>
                                    &nbsp;|&nbsp;
                                    <form action="${pageContext.request.contextPath}/mascotas" method="post" style="display:inline">
                                        <input type="hidden" name="accion" value="eliminar">
                                        <input type="hidden" name="id" value="${mascota.idMascota}">
                                        <button type="submit" class="enlace-eliminar"
                                                onclick="return confirm('¿Eliminar esta mascota?');">Eliminar</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</body>
</html>
