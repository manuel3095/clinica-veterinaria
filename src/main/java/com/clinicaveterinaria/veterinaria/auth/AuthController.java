package com.clinicaveterinaria.veterinaria.auth;

import com.clinicaveterinaria.veterinaria.model.Usuario;
import com.clinicaveterinaria.veterinaria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicio web de registro e inicio de sesion, requerido por la
 * evidencia GA7-220501096-AA5-EV01.
 *
 * El servicio recibe un usuario y una contrasena: si la autenticacion
 * es correcta, retorna el mensaje "Autenticacion satisfactoria"; en
 * caso contrario, retorna "Error en la autenticacion". Toda la logica
 * de validacion y comparacion de credenciales se delega en
 * AutenticacionLogica, que puede probarse de forma independiente
 * (ver PruebasAutenticacion.java).
 *
 * Ruta base: /auth
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registra un nuevo usuario del personal de la clinica.
     * POST /auth/registro
     * Cuerpo esperado: {"nombreUsuario": "...", "clave": "..."}
     */
    @PostMapping("/registro")
    public ResponseEntity<String> registrar(@RequestBody Usuario datosRegistro) {
        Usuario usuarioExistente = usuarioRepository.findByNombreUsuario(datosRegistro.getNombreUsuario());
        String errorValidacion = AutenticacionLogica.validarRegistro(
                datosRegistro.getNombreUsuario(), datosRegistro.getClave(), usuarioExistente != null);

        if (errorValidacion != null) {
            return ResponseEntity.badRequest().body(errorValidacion);
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(datosRegistro.getNombreUsuario());
        // La clave nunca se guarda en texto plano: se almacena su hash SHA-256.
        nuevoUsuario.setClave(AutenticacionLogica.prepararClaveParaAlmacenar(datosRegistro.getClave()));
        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok("Usuario registrado correctamente");
    }

    /**
     * Inicia sesion con un usuario y una contrasena.
     * POST /auth/login
     * Cuerpo esperado: {"nombreUsuario": "...", "clave": "..."}
     *
     * @return "Autenticacion satisfactoria" (200 OK) si las
     *         credenciales son correctas, o "Error en la
     *         autenticacion" (401) en caso contrario.
     */
    @PostMapping("/login")
    public ResponseEntity<String> iniciarSesion(@RequestBody Usuario intentoLogin) {
        Usuario usuarioEncontrado = usuarioRepository.findByNombreUsuario(intentoLogin.getNombreUsuario());
        String claveHasheadaAlmacenada = (usuarioEncontrado != null) ? usuarioEncontrado.getClave() : null;

        String resultado = AutenticacionLogica.autenticar(intentoLogin.getClave(), claveHasheadaAlmacenada);

        if (AutenticacionLogica.MENSAJE_AUTENTICACION_EXITOSA.equals(resultado)) {
            return ResponseEntity.ok(resultado);
        }
        return ResponseEntity.status(401).body(resultado);
    }
}
