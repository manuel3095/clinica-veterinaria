<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Clinica Veterinaria - Error</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/recursos/css/estilos.css">
</head>
<body>
    <div class="contenedor">
        <h1>Se produjo un error</h1>
        <p class="mensaje-error">${mensajeError}</p>
        <p><a class="boton" href="${pageContext.request.contextPath}/mascotas?accion=listar">Volver al listado</a></p>
    </div>
</body>
</html>
