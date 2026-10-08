# Clinica Veterinaria - Modulo Mascotas (GA7-220501096-AA2-EV02)

Aplicacion web JavaEE (Servlets + JSP + JDBC) que implementa el CRUD
(Create, Read, Update, Delete) del modulo **Mascotas** de la Clinica
Veterinaria, cumpliendo los requisitos de la evidencia:

- Codigo organizado en paquetes (modelo, dao, controlador, util) siguiendo
  estandares de nombramiento (clases en PascalCase, metodos y variables en
  camelCase, paquetes en minusculas).
- Conexion a la base de datos MySQL/MariaDB mediante JDBC (PreparedStatement).
- CRUD completo: insercion, consulta, actualizacion y eliminacion de mascotas.
- Formularios HTML atendidos por un Servlet (`MascotaServlet`).
- Uso de los metodos HTTP **GET** (listar, mostrar formularios) y **POST**
  (guardar, actualizar, eliminar).
- Vistas con elementos de **JSP** (JSTL `<c:forEach>`, `<c:choose>`, scriptlets).
- Proyecto gestionado con Maven y control de versiones Git.

## Estructura del proyecto

```
veterinaria-web/
├── pom.xml
├── sql/
│   └── clinicaveterinaria.sql        (script de creacion de la BD)
└── src/main/
    ├── java/com/clinicaveterinaria/veterinaria/
    │   ├── modelo/Mascota.java
    │   ├── dao/MascotaDAO.java
    │   ├── dao/MascotaDAOImpl.java
    │   ├── util/ConexionBD.java
    │   └── controlador/MascotaServlet.java
    └── webapp/
        ├── index.jsp
        ├── error.jsp
        ├── mascotas/listar.jsp
        ├── mascotas/formulario.jsp
        ├── recursos/css/estilos.css
        └── WEB-INF/web.xml
```

## Requisitos previos

- JDK 11 o superior.
- Apache Maven 3.8+.
- Apache Tomcat 9 (o NetBeans con el servidor Tomcat/GlassFish configurado).
- MySQL 8 o MariaDB 10.

## 1. Crear la base de datos

Ejecutar el script `sql/clinicaveterinaria.sql` en MySQL Workbench o por
consola:

```
mysql -u root -p < sql/clinicaveterinaria.sql
```

Esto crea la base de datos `clinicaveterinaria`, la tabla `mascotas`, el
usuario `veterinaria_user` (clave `Veterinaria2024*`) y dos registros de
ejemplo.

> Si se desea usar otro usuario/clave, actualizar las constantes en
> `ConexionBD.java`.

## 2. Compilar el proyecto

Con Maven instalado y conexion a internet (para descargar las
dependencias desde Maven Central):

```
cd veterinaria-web
mvn clean package
```

Esto genera `target/veterinaria-web.war`.

### Alternativa en NetBeans

1. Abrir NetBeans → *File → Open Project* → seleccionar la carpeta
   `veterinaria-web` (NetBeans reconoce el `pom.xml` automaticamente).
2. Agregar el servidor Apache Tomcat 9 en *Tools → Servers* si no esta
   configurado.
3. Clic derecho sobre el proyecto → *Run*.

## 3. Desplegar en Tomcat

Copiar `target/veterinaria-web.war` a la carpeta `webapps` de Tomcat, o
usar el boton *Deploy* de NetBeans. Tomcat expandira el WAR
automaticamente.

## 4. Probar la aplicacion

Abrir en el navegador:

```
http://localhost:8080/veterinaria-web/mascotas
```

- **Listar**: pagina principal, `GET /mascotas?accion=listar`.
- **Crear**: boton "Nueva mascota" → formulario HTML → `POST /mascotas?accion=guardar`.
- **Editar**: enlace "Editar" en la tabla → formulario precargado → `POST /mascotas?accion=actualizar`.
- **Eliminar**: boton "Eliminar" en la tabla → `POST /mascotas?accion=eliminar`.

## Control de versiones

El proyecto incluye un repositorio Git local (`git init`) con commits
incrementales por cada capa desarrollada (modelo, DAO, controlador,
vistas), tal como lo exige la evidencia. Para publicarlo en un
repositorio remoto (GitHub/GitLab):

```
git remote add origin <URL-del-repositorio>
git push -u origin main
```
