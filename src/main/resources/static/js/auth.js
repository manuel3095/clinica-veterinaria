/**
 * Logica del front-end del servicio de registro e inicio de sesion.
 *
 * Envia los datos del formulario a los endpoints /auth/registro y
 * /auth/login del backend, y muestra en pantalla el mensaje exacto
 * devuelto por el servidor ("Autenticacion satisfactoria" o
 * "Error en la autenticacion"), exigido por la evidencia.
 */
(function () {
    "use strict";

    const formularioRegistro = document.getElementById("formularioRegistro");
    const formularioLogin = document.getElementById("formularioLogin");
    const mensajeRegistro = document.getElementById("mensajeRegistro");
    const mensajeLogin = document.getElementById("mensajeLogin");

    formularioRegistro.addEventListener("submit", function (evento) {
        evento.preventDefault();
        const datos = {
            nombreUsuario: document.getElementById("usuarioRegistro").value.trim(),
            clave: document.getElementById("claveRegistro").value
        };

        fetch("/auth/registro", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        })
            .then(function (respuesta) {
                return respuesta.text().then(function (texto) {
                    return { ok: respuesta.ok, texto: texto };
                });
            })
            .then(function (resultado) {
                mostrarMensaje(mensajeRegistro, resultado.texto, resultado.ok);
            })
            .catch(function (error) {
                mostrarMensaje(mensajeRegistro, "No se pudo conectar con el servidor: " + error.message, false);
            });
    });

    formularioLogin.addEventListener("submit", function (evento) {
        evento.preventDefault();
        const datos = {
            nombreUsuario: document.getElementById("usuarioLogin").value.trim(),
            clave: document.getElementById("claveLogin").value
        };

        fetch("/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(datos)
        })
            .then(function (respuesta) {
                return respuesta.text().then(function (texto) {
                    return { ok: respuesta.ok, texto: texto };
                });
            })
            .then(function (resultado) {
                mostrarMensaje(mensajeLogin, resultado.texto, resultado.ok);
            })
            .catch(function (error) {
                mostrarMensaje(mensajeLogin, "No se pudo conectar con el servidor: " + error.message, false);
            });
    });

    function mostrarMensaje(elemento, texto, exito) {
        elemento.textContent = texto;
        elemento.className = "mensaje-servidor " + (exito ? "exito" : "fallo");
    }
})();
