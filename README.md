# Clínica Veterinaria - Módulo Mascotas (GA7-220501096-AA3-EV01)

Módulo de software desarrollado con **Spring Boot**, **Spring Data JPA**
y **MySQL**, que implementa el CRUD (Create, Read, Update, Delete) de
la entidad **Mascota** para el sistema de gestión de una clínica
veterinaria, dando cumplimiento a los requisitos de la evidencia:

- Código organizado en capas (`model`, `repository`, `services`,
  `controller`), con **comentarios** explicando el propósito de cada
  clase y método.
- **Estándares de codificación**: clases en PascalCase
  (`Mascota`, `MascotaController`), atributos y métodos en camelCase
  (`idMascota`, `nomMascota`, `obtenerTodas()`), paquetes en minúsculas.
- Conexión a base de datos MySQL mediante **Spring Data JPA** /
  **JDBC** (`application.properties`).
- CRUD completo expuesto como servicios REST (`/mascotas/nuevo`,
  `/mascotas/mostrar`, `/mascotas/modificar`, `/mascotas/{id}`).
- Proyecto gestionado con **Maven** y control de versiones **Git**.

## Estructura del proyecto

```
veterinaria/
├── pom.xml
├── sql/clinicaveterinaria.sql
└── src/main/
    ├── java/com/clinicaveterinaria/veterinaria/
    │   ├── VeterinariaApplication.java   (clase principal @SpringBootApplication)
    │   ├── model/Mascota.java            (entidad JPA)
    │   ├── repository/MascotaRepository.java
    │   ├── services/MascotaService.java          (interfaz)
    │   ├── services/MascotaServiceImpl.java       (implementación)
    │   └── controller/MascotaController.java     (endpoints REST)
    └── resources/application.properties
```

## Requisitos previos

- JDK 17+, Apache Maven 3.8+, MySQL 8 / MariaDB 10.

## 1. Crear la base de datos

```
mysql -u root -p < sql/clinicaveterinaria.sql
```

Si se prefiere, Spring Data JPA puede crear la tabla automáticamente
gracias a `spring.jpa.hibernate.ddl-auto=update`; el script solo deja
además dos registros de ejemplo.

> Ajustar usuario/clave de `application.properties` según la
> configuración local de MySQL.

## 2. Ejecutar la aplicación

```
cd veterinaria
mvn spring-boot:run
```

La aplicación queda disponible en `http://localhost:8080`.

### Alternativa en NetBeans

Abrir NetBeans → *File → Open Project* → seleccionar la carpeta
`veterinaria` (NetBeans reconoce el `pom.xml` automáticamente) → clic
derecho → *Run*.

## 3. Probar los endpoints (Postman o curl)

| Acción              | Método | Endpoint              |
|---------------------|--------|------------------------|
| Crear mascota       | POST   | `/mascotas/nuevo`      |
| Listar mascotas     | GET    | `/mascotas/mostrar`    |
| Modificar mascota   | POST   | `/mascotas/modificar`  |
| Eliminar mascota    | POST   | `/mascotas/{id}`       |

Ejemplo de cuerpo JSON para crear/modificar:

```json
{
  "idMascota": 1,
  "nomMascota": "Max",
  "estado": "Estable",
  "especie": "Canis lupus familiaris",
  "edad": "3",
  "raza": "Husky"
}
```

## 4. Servicio web de registro e inicio de sesión (AA5-EV01)

Servicio REST que recibe un usuario y una contraseña. Si la
autenticación es correcta, responde **"Autenticacion satisfactoria"**
(HTTP 200); en caso contrario, responde **"Error en la autenticacion"**
(HTTP 401). La contraseña nunca se almacena en texto plano: se guarda
su hash SHA-256.

| Acción            | Método | Endpoint          |
|--------------------|--------|--------------------|
| Registrar usuario  | POST   | `/auth/registro`   |
| Iniciar sesión     | POST   | `/auth/login`      |

Cuerpo JSON de ambos endpoints:

```json
{ "nombreUsuario": "recepcion1", "clave": "clave1234" }
```

Usuario de ejemplo creado por el script SQL: `recepcion1` /
`clave1234`.

Además de los endpoints, se incluye `auth.html` (formularios de
registro e inicio de sesión) accesible en
`http://localhost:8080/auth.html`.

### Pruebas de la lógica de autenticación (sin Maven ni base de datos)

```
cd src/main/java
javac com/clinicaveterinaria/veterinaria/util/UtilidadClave.java \
      com/clinicaveterinaria/veterinaria/auth/AutenticacionLogica.java \
      com/clinicaveterinaria/veterinaria/auth/PruebasAutenticacion.java
java com.clinicaveterinaria.veterinaria.auth.PruebasAutenticacion
```

Debe mostrar `11 / 11 casos aprobados`.

## Control de versiones

El proyecto incluye un repositorio Git local (`git init`) con commits
incrementales por cada capa desarrollada. Para publicarlo en un
repositorio remoto (GitHub/GitLab):

```
git remote add origin <URL-del-repositorio>
git push -u origin main
```

## AA5-EV02 - Pruebas de la API (Postman)
- `postman/Veterinaria_API.postman_collection.json`: coleccion importable en Postman (14 peticiones con pruebas automaticas).
- `ENDPOINT.txt`: rutas de la API.
- `sql/ServidorPruebasAPI.java`: servidor HTTP de verificacion (solo JDK) usado para ejecutar las pruebas cuando Spring Boot no esta disponible.

## AA5-EV03 - Diseño y desarrollo de servicios web
Se amplió la API con servicios de consulta: `GET /mascotas/{id}`, `GET /mascotas/estado/{estado}`,
`GET /mascotas/propietario?correo=` y `GET /auth/usuarios`; `modificar` ahora actualiza también
`fechaIngreso` y `correoPropietario`, y `POST /mascotas/{id}` retorna `false` si el id no existe.
Documentación: `docs/API_DOCUMENTACION.md` y `docs/openapi.yaml`. Endpoints: `ENDPOINT.txt`.
Colección Postman con 20 peticiones: `postman/Veterinaria_API.postman_collection.json`.

## AA5-EV04 - Testing de la API con Postman
- Colección: `postman/Veterinaria_API.postman_collection.json` (20 peticiones, 61 aserciones) y entorno `postman/Veterinaria_Local.postman_environment.json`.
- La colección es repetible: el usuario de prueba se genera con sufijo aleatorio (`{{usuarioNuevo}}`).
- Ejecución por línea de comandos con Newman (motor de Postman):
  `newman run postman/Veterinaria_API.postman_collection.json -e postman/Veterinaria_Local.postman_environment.json`
- Arrancar la API: `mvn spring-boot:run` (puerto 3000, definido en `server.port`).
