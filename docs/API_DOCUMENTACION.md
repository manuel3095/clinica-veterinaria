# Documentación de la API - Clínica Veterinaria (GA7-220501096-AA5-EV03)

Aprendiz: Manuel Passo. URL base: `http://localhost:3000`.
Formato: JSON (`Content-Type: application/json`). Especificación formal: [`openapi.yaml`](openapi.yaml).

## Conexión a la base de datos
No se codifica una clase de conexión manual: Spring Boot crea el `DataSource` a partir de
`src/main/resources/application.properties` (`spring.datasource.url=jdbc:mysql://localhost:3306/clinicaveterinaria`).
Spring Data JPA (`MascotaRepository`, `UsuarioRepository`) ejecuta las consultas; no hay SQL escrito a mano.
El esquema se crea con `sql/clinicaveterinaria.sql`.

## Arquitectura de cada servicio
`Controller` (recibe HTTP) → `Service`/lógica de validación → `Repository` (JPA) → MySQL.

## Modelo Mascota
| Campo | Tipo | Regla |
|---|---|---|
| idMascota | número | Generado por la base de datos |
| nomMascota | texto | Obligatorio, solo letras |
| estado | texto | `Estable`, `Critico` u `Hospitalizado` |
| especie, raza | texto | Obligatorios |
| edad | texto | 1 o 2 dígitos |
| fechaIngreso | fecha `AAAA-MM-DD` | No puede ser futura |
| correoPropietario | texto | Formato `usuario@dominio.com` |

## Servicios de autenticación (`/auth`)
| # | Método y ruta | Entrada | Respuestas |
|---|---|---|---|
| 1 | `POST /auth/registro` | `{"nombreUsuario","clave"}` (usuario ≥ 3, clave ≥ 8, usuario no repetido) | 200 `Usuario registrado correctamente` · 400 mensaje de validación |
| 2 | `POST /auth/login` | `{"nombreUsuario","clave"}` | 200 `Autenticacion satisfactoria` · 401 `Error en la autenticacion` |
| 3 | `GET /auth/usuarios` | — | 200 `[{"idUsuario":1,"nombreUsuario":"recepcion1"}]` (nunca incluye la clave) |

## Servicios de mascotas (`/mascotas`)
| # | Método y ruta | Entrada | Respuestas |
|---|---|---|---|
| 4 | `GET /mascotas/mostrar` | — | 200 arreglo de mascotas |
| 5 | `GET /mascotas/{id}` | id en la ruta | 200 mascota · 404 no existe |
| 6 | `GET /mascotas/estado/{estado}` | estado en la ruta | 200 arreglo (puede ser vacío) |
| 7 | `GET /mascotas/propietario?correo=...` | parámetro `correo` | 200 arreglo (puede ser vacío) |
| 8 | `POST /mascotas/nuevo` | mascota sin `idMascota` | 200 mascota creada · 400 lista de errores |
| 9 | `POST /mascotas/modificar` | mascota con `idMascota` | 200 mascota actualizada · 400 lista de errores |
| 10 | `POST /mascotas/{id}` | id en la ruta | 200 `true` si existía y se eliminó, `false` si no existía |

## Ejemplos
```
curl -X POST http://localhost:3000/auth/login -H "Content-Type: application/json" \
     -d '{"nombreUsuario":"recepcion1","clave":"clave1234"}'
curl http://localhost:3000/mascotas/estado/Hospitalizado
curl -X POST http://localhost:3000/mascotas/nuevo -H "Content-Type: application/json" \
     -d '{"nomMascota":"Luna","estado":"Estable","especie":"Felis catus","edad":"4","raza":"Siames","fechaIngreso":"2024-03-12","correoPropietario":"luna.duena@email.com"}'
```

## Errores de validación (400) de mascotas
Lista de textos, p. ej. `"estado: valor no permitido. Debe ser uno de [Estable, Critico, Hospitalizado]."`.

## Versionamiento
Repositorio Git con commits por funcionalidad (`git log --oneline`). Ver `REPOSITORIO_*.txt`.
