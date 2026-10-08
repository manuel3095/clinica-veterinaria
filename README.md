# Clínica Veterinaria - Sistema de gestión de mascotas

Proyecto formativo del **Tecnólogo en Análisis y Desarrollo de Software (SENA)**, ficha GA7-220501096.
Aprendiz: **Manuel Passo**.

Aplicación web para una clínica veterinaria: gestión de mascotas (CRUD con validaciones), autenticación
del personal (registro e inicio de sesión con contraseñas en hash SHA-256) y una API REST documentada y probada.

**Tecnologías:** Java 17, Spring Boot 3.2.5, Spring Data JPA, MySQL/MariaDB, HTML/CSS/JavaScript, Maven, Git, Postman.

## Estructura del repositorio

```
├── src/main/java/...      Código Java (model, repository, services, controller, auth, validacion, util)
├── src/main/resources/    application.properties (puerto 3000) y front-end estático (static/)
├── sql/                   Script de la base de datos y arneses de verificación
├── docs/                  Documentación de la API (API_DOCUMENTACION.md y openapi.yaml)
├── postman/               Colección y entorno de Postman
├── ENDPOINT.txt           Lista de endpoints
└── evidencias/            Documentos Word (APA 7) y pruebas de cada evidencia del proyecto formativo
```

## Evidencias del proyecto formativo

Cada evidencia tiene su carpeta en [`evidencias/`](evidencias/) y una etiqueta (*tag*) de Git que marca el
estado del código al terminar esa evidencia (`git checkout aa4-ev03`, por ejemplo).

| Evidencia | Tema | Etiqueta Git |
|---|---|---|
| GA7-220501096-AA2-EV02 | Módulos codificados y probados: versiones previas con Servlets/JSP (carpeta `evidencias/`, sin etiqueta) | — |
| GA7-220501096-AA3-EV01 | Codificación de módulos (CRUD de mascotas) | `aa3-ev01` |
| GA7-220501096-AA3-EV02 | Módulos codificados y probados (validaciones) | `aa3-ev02` |
| GA7-220501096-AA4-EV03 | Componente front-end | `aa4-ev03` |
| GA7-220501096-AA5-EV01 | Servicio web de registro e inicio de sesión | `aa5-ev01` |
| GA7-220501096-AA5-EV02 | Testing de la API con Postman | `aa5-ev02` |
| GA7-220501096-AA5-EV03 | Diseño y desarrollo de servicios web | `aa5-ev03` |
| GA7-220501096-AA5-EV04 | API del proyecto: testing con Postman | `aa5-ev04` |

## Inicio rápido

1. Cargar la base de datos: `mysql -u root -p < sql/clinicaveterinaria.sql`
2. Ajustar usuario y clave en `src/main/resources/application.properties`.
3. Ejecutar: `mvn spring-boot:run` → API en `http://localhost:3000` (front-end en `/index.html` y `/auth.html`).
4. Probar la API: importar `postman/Veterinaria_API.postman_collection.json` y `postman/Veterinaria_Local.postman_environment.json` en Postman.

Documentación completa de los servicios: [`docs/API_DOCUMENTACION.md`](docs/API_DOCUMENTACION.md).

> **Nota sobre las pruebas:** las pruebas de las evidencias AA5-EV02 a AA5-EV04 se ejecutaron contra un servidor
> equivalente hecho solo con el JDK (`sql/ServidorPruebasAPI.java`) porque en el entorno de preparación no se
> pudieron descargar las dependencias de Maven. La colección de Postman debe repetirse contra la aplicación
> Spring Boot real (`mvn spring-boot:run`).

---

## Guía detallada del módulo Mascotas (AA3-EV01)

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

La aplicación queda disponible en `http://localhost:3000`.

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
`http://localhost:3000/auth.html`.

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
