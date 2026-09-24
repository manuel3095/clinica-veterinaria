/**
 * Logica del front-end de gestion de mascotas.
 *
 * Consume la API REST del backend Spring Boot (MascotaController):
 *   GET  /mascotas/mostrar     -> listar mascotas
 *   POST /mascotas/nuevo       -> crear una mascota
 *   POST /mascotas/modificar   -> actualizar una mascota
 *   POST /mascotas/{id}        -> eliminar una mascota
 *
 * Antes de enviar los datos al servidor, se validan en el cliente
 * usando ValidacionesMascota (validaciones.js), que replica las
 * mismas reglas del backend (MascotaValidador.java), mostrando el
 * error directamente bajo cada campo del formulario.
 */
(function () {
    "use strict";

    const URL_BASE = "/mascotas";

    const formulario = document.getElementById("formularioMascota");
    const cuerpoTabla = document.getElementById("cuerpoTablaMascotas");
    const mensajeServidor = document.getElementById("mensajeServidor");
    const tituloFormulario = document.getElementById("tituloFormulario");
    const botonCancelar = document.getElementById("botonCancelar");

    document.addEventListener("DOMContentLoaded", cargarMascotas);
    formulario.addEventListener("submit", manejarEnvioFormulario);
    botonCancelar.addEventListener("click", reiniciarFormulario);

    /**
     * Consulta el listado de mascotas en el backend y las renderiza
     * en la tabla.
     */
    function cargarMascotas() {
        fetch(URL_BASE + "/mostrar")
            .then(function (respuesta) { return respuesta.json(); })
            .then(renderizarTabla)
            .catch(function (error) {
                cuerpoTabla.innerHTML =
                    '<tr><td colspan="9" class="sin-datos">No se pudo conectar con el servidor: '
                    + error.message + "</td></tr>";
            });
    }

    function renderizarTabla(mascotas) {
        if (!mascotas || mascotas.length === 0) {
            cuerpoTabla.innerHTML =
                '<tr><td colspan="9" class="sin-datos">No hay mascotas registradas todavía.</td></tr>';
            return;
        }

        cuerpoTabla.innerHTML = "";
        mascotas.forEach(function (mascota) {
            const fila = document.createElement("tr");
            fila.innerHTML =
                "<td>" + mascota.idMascota + "</td>" +
                "<td>" + escaparHtml(mascota.nomMascota) + "</td>" +
                "<td>" + escaparHtml(mascota.estado) + "</td>" +
                "<td>" + escaparHtml(mascota.especie) + "</td>" +
                "<td>" + escaparHtml(mascota.edad) + "</td>" +
                "<td>" + escaparHtml(mascota.raza) + "</td>" +
                "<td>" + (mascota.fechaIngreso || "") + "</td>" +
                "<td>" + escaparHtml(mascota.correoPropietario) + "</td>" +
                '<td><button class="boton-editar" data-id="' + mascota.idMascota + '">Editar</button>' +
                '<button class="boton-eliminar" data-id="' + mascota.idMascota + '">Eliminar</button></td>';
            cuerpoTabla.appendChild(fila);
        });

        // Se enlazan los botones de cada fila despues de renderizarla.
        cuerpoTabla.querySelectorAll(".boton-editar").forEach(function (boton) {
            boton.addEventListener("click", function () { cargarMascotaEnFormulario(boton.dataset.id, mascotas); });
        });
        cuerpoTabla.querySelectorAll(".boton-eliminar").forEach(function (boton) {
            boton.addEventListener("click", function () { eliminarMascota(boton.dataset.id); });
        });
    }

    function cargarMascotaEnFormulario(id, mascotas) {
        const mascota = mascotas.find(function (m) { return String(m.idMascota) === String(id); });
        if (!mascota) {
            return;
        }
        document.getElementById("idMascota").value = mascota.idMascota;
        document.getElementById("nomMascota").value = mascota.nomMascota;
        document.getElementById("estado").value = mascota.estado;
        document.getElementById("especie").value = mascota.especie;
        document.getElementById("edad").value = mascota.edad;
        document.getElementById("raza").value = mascota.raza;
        document.getElementById("fechaIngreso").value = mascota.fechaIngreso || "";
        document.getElementById("correoPropietario").value = mascota.correoPropietario;
        tituloFormulario.textContent = "Editar mascota";
        limpiarErrores();
    }

    function reiniciarFormulario() {
        formulario.reset();
        document.getElementById("idMascota").value = "";
        tituloFormulario.textContent = "Registrar nueva mascota";
        limpiarErrores();
        mostrarMensajeServidor("", "");
    }

    /**
     * Toma los valores del formulario, los valida en el cliente y,
     * si son validos, los envia al backend (crear o actualizar segun
     * si hay un idMascota presente).
     */
    function manejarEnvioFormulario(evento) {
        evento.preventDefault();
        limpiarErrores();

        const mascota = {
            nomMascota: document.getElementById("nomMascota").value.trim(),
            estado: document.getElementById("estado").value,
            especie: document.getElementById("especie").value.trim(),
            edad: document.getElementById("edad").value.trim(),
            raza: document.getElementById("raza").value.trim(),
            fechaIngreso: document.getElementById("fechaIngreso").value,
            correoPropietario: document.getElementById("correoPropietario").value.trim()
        };

        const errores = window.ValidacionesMascota.validarMascota(mascota);
        if (errores.length > 0) {
            mostrarErroresEnFormulario(errores);
            mostrarMensajeServidor("Revisa los campos marcados en rojo.", "fallo");
            return;
        }

        const idMascota = document.getElementById("idMascota").value;
        const accion = idMascota ? "modificar" : "nuevo";
        if (idMascota) {
            mascota.idMascota = Number(idMascota);
        }

        fetch(URL_BASE + "/" + accion, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(mascota)
        })
            .then(function (respuesta) {
                if (!respuesta.ok) {
                    return respuesta.json().then(function (cuerpo) {
                        throw new Error(Array.isArray(cuerpo) ? cuerpo.join(" | ") : JSON.stringify(cuerpo));
                    });
                }
                return respuesta.json();
            })
            .then(function () {
                mostrarMensajeServidor("Mascota guardada correctamente.", "exito");
                reiniciarFormulario();
                cargarMascotas();
            })
            .catch(function (error) {
                mostrarMensajeServidor("El servidor rechazó los datos: " + error.message, "fallo");
            });
    }

    function eliminarMascota(id) {
        if (!confirm("¿Eliminar esta mascota?")) {
            return;
        }
        fetch(URL_BASE + "/" + id, { method: "POST" })
            .then(function () { cargarMascotas(); })
            .catch(function (error) {
                mostrarMensajeServidor("No se pudo eliminar: " + error.message, "fallo");
            });
    }

    function mostrarErroresEnFormulario(errores) {
        errores.forEach(function (error) {
            const nombreCampo = error.split(":")[0];
            const elementoError = document.getElementById(
                "error" + nombreCampo.charAt(0).toUpperCase() + nombreCampo.slice(1));
            const campo = document.getElementById(nombreCampo);
            if (elementoError) {
                elementoError.textContent = error;
            }
            if (campo) {
                campo.classList.add("campo-invalido");
            }
        });
    }

    function limpiarErrores() {
        document.querySelectorAll(".error").forEach(function (el) { el.textContent = ""; });
        document.querySelectorAll(".campo-invalido").forEach(function (el) {
            el.classList.remove("campo-invalido");
        });
    }

    function mostrarMensajeServidor(texto, tipo) {
        mensajeServidor.textContent = texto;
        mensajeServidor.className = "mensaje-servidor" + (tipo ? " " + tipo : "");
    }

    function escaparHtml(texto) {
        if (texto === null || texto === undefined) {
            return "";
        }
        return String(texto)
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;");
    }
})();
