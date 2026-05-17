# Producto 4 - Servicio API RESTful

Proyecto desarrollado para la asignatura **FP.065 - Aplicación back-end con Java en servidores de aplicaciones**.

La aplicación implementa una plataforma de gestión de alquiler de vehículos desarrollada con **Java**, **Spring Boot**, **Spring MVC**, **Spring Data JPA**, **Spring Security**, **JWT**, **Thymeleaf**, **MySQL**, **Maven**, **Docker** y **Swagger/OpenAPI**.

Este Producto 4 amplía el Producto 3 incorporando una **API RESTful** que permite consultar y gestionar datos de la aplicación mediante endpoints HTTP, separando correctamente el comportamiento de la aplicación web tradicional y el comportamiento de la API REST.

---

## 1. Objetivo del producto

El objetivo principal del Producto 4 es transformar la aplicación desarrollada en los productos anteriores en una aplicación capaz de ofrecer servicios mediante una **API RESTful**.

Las mejoras principales implementadas son:

- Creación de endpoints REST públicos para consultar clientes, vehículos y alquileres.
- Creación de endpoints REST protegidos para operaciones de administración.
- Incorporación de autenticación mediante JWT.
- Creación de endpoint de login API para obtener token.
- Separación entre seguridad web y seguridad API.
- Uso de códigos HTTP adecuados en la API REST.
- Respuesta `401 Unauthorized` cuando no se envía token.
- Respuesta `403 Forbidden` cuando el usuario autenticado no tiene permisos.
- Uso de DTOs para exponer datos de forma controlada.
- Documentación de endpoints mediante Swagger/OpenAPI.
- Pruebas de la API con Postman.
- Ejecución de la aplicación en local y con Docker.

---

## 2. Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring MVC
- Spring Web
- Spring Data JPA
- Spring Security
- JWT
- Thymeleaf
- Thymeleaf Extras Spring Security
- MySQL
- Maven
- Docker
- Docker Compose
- Swagger / OpenAPI
- Postman
- IntelliJ IDEA
- Git y GitHub

---

## 3. Relación con los productos anteriores

El Producto 4 parte del trabajo realizado en los productos anteriores.

### Producto 2

Se implementó la aplicación MVC con:

- Entidades principales: `Cliente`, `Vehiculo` y `Alquiler`.
- Controladores web.
- Servicios.
- Repositorios.
- Vistas Thymeleaf.
- Persistencia en MySQL.
- Ejecución con Docker.

### Producto 3

Se añadió seguridad web con:

- Usuarios.
- Roles.
- Login.
- Registro.
- Control de acceso por roles.
- Rutas `/admin/**` y `/user/**`.
- Auditoría de alquileres.
- Gestión básica de usuarios.

### Producto 4

Se añade una nueva capa API REST con:

- Controladores REST.
- DTOs.
- Autenticación mediante JWT.
- Endpoints públicos y protegidos.
- Respuestas JSON.
- Documentación Swagger.

---

## 4. Arquitectura general del proyecto

La aplicación mantiene una arquitectura por capas:

```text
Cliente externo / Postman / Swagger
        ↓
Controller REST
        ↓
DTO
        ↓
Service
        ↓
Repository
        ↓
Base de datos MySQL
```

Además, se conserva la arquitectura web del Producto 3:

```text
Vista Thymeleaf → Controller Web → Service → Repository → Base de datos
```

Por tanto, en el Producto 4 conviven dos formas de acceso:

```text
Aplicación web Thymeleaf
API REST JSON
```

---

## 5. Estructura general del proyecto

```text
src/main/java/com/code065/alquilervehiculos
├── config
│   └── SecurityConfig.java
├── controller
│   ├── api
│   │   ├── AlquilerAdminApiController.java
│   │   ├── AlquilerApiController.java
│   │   ├── AuthApiController.java
│   │   ├── ClienteAdminApiController.java
│   │   ├── ClienteApiController.java
│   │   ├── VehiculoAdminApiController.java
│   │   └── VehiculoApiController.java
│   └── web
│       ├── AdminAlquilerViewController.java
│       ├── AdminUsuarioViewController.java
│       ├── AuthViewController.java
│       ├── ClienteViewController.java
│       ├── ErrorViewController.java
│       ├── HomeViewController.java
│       ├── UserAlquilerViewController.java
│       └── VehiculoViewController.java
├── dto
│   ├── api
│   │   ├── AlquilerResponseDto.java
│   │   ├── AuthRequestDto.java
│   │   ├── AuthResponseDto.java
│   │   ├── ClienteRequestDto.java
│   │   ├── ClienteResponseDto.java
│   │   ├── VehiculoRequestDto.java
│   │   └── VehiculoResponseDto.java
│   └── RegistroUsuarioDto.java
├── model
│   ├── Alquiler.java
│   ├── Cliente.java
│   ├── Rol.java
│   ├── Usuario.java
│   └── Vehiculo.java
├── repository
├── security
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtService.java
│   └── UsuarioAutenticadoService.java
└── service
    └── imp
```

---

## 6. API REST implementada

La API REST se organiza principalmente en dos tipos de endpoints:

```text
/api/**
/api/admin/**
```

Los endpoints `/api/**` permiten consultas públicas.

Los endpoints `/api/admin/**` están protegidos y requieren autenticación mediante JWT con rol `ADMIN`.

---

## 7. Endpoints públicos

Los endpoints públicos permiten consultar información sin necesidad de autenticación.

### Clientes

```http
GET /api/clientes
```

Devuelve la lista de clientes en formato JSON.

### Vehículos

```http
GET /api/vehiculos
```

Devuelve la lista de vehículos en formato JSON.

### Alquileres

```http
GET /api/alquileres
```

Devuelve la lista de alquileres en formato JSON.

Estos endpoints están pensados para consultas generales y no requieren token.

---

## 8. Endpoint de autenticación API

Para acceder a los endpoints protegidos se debe obtener previamente un token JWT.

### Login API

```http
POST /api/auth/login
```

Ejemplo de cuerpo JSON:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Respuesta esperada:

```json
{
  "token": "TOKEN_JWT_GENERADO"
}
```

El token recibido debe enviarse después en las peticiones protegidas mediante la cabecera:

```http
Authorization: Bearer TOKEN_JWT_GENERADO
```

---

## 9. Endpoints protegidos de administración

Los endpoints de administración requieren:

- Token JWT válido.
- Usuario autenticado.
- Rol `ADMIN`.

### Clientes

```http
GET /api/admin/clientes
POST /api/admin/clientes
DELETE /api/admin/clientes/{id}
```

### Vehículos

```http
GET /api/admin/vehiculos
POST /api/admin/vehiculos
DELETE /api/admin/vehiculos/{id}
```

### Alquileres

```http
GET /api/admin/alquileres
POST /api/admin/alquileres
DELETE /api/admin/alquileres/{id}
```

Estos endpoints permiten realizar operaciones administrativas sobre los datos principales de la aplicación.

---

## 10. Seguridad API con JWT

La seguridad del Producto 4 se basa en JWT para la API REST.

El flujo general es:

```text
1. El usuario envía username y password a /api/auth/login.
2. Spring Security valida las credenciales.
3. Si son correctas, se genera un token JWT.
4. El cliente externo guarda temporalmente el token.
5. En cada petición protegida se envía:
   Authorization: Bearer TOKEN
6. El filtro JWT valida el token.
7. Si el token es válido, Spring Security autoriza la petición según el rol.
```

---

## 11. Separación entre seguridad web y seguridad API

Una de las partes importantes del Producto 4 es la separación entre:

```text
Seguridad web
Seguridad API REST
```

La parte web mantiene el comportamiento del Producto 3:

```text
/login
/registro
/admin/**
/user/**
```

La parte API utiliza:

```text
/api/**
```

La configuración se realiza en:

```text
SecurityConfig.java
```

Se han definido dos cadenas de seguridad:

```text
apiSecurityFilterChain
webSecurityFilterChain
```

### Cadena API

La cadena API se aplica a:

```text
/api/**
```

Características:

- No usa sesión HTTP.
- No usa login por formulario.
- No redirige a `/login`.
- Usa JWT.
- Devuelve respuestas JSON.
- Devuelve `401` si no hay autenticación.
- Devuelve `403` si no hay permisos suficientes.

### Cadena web

La cadena web se aplica al resto de rutas.

Características:

- Usa login por formulario.
- Usa sesión HTTP.
- Redirige al login si el usuario no está autenticado.
- Mantiene las rutas protegidas `/admin/**` y `/user/**`.

---

## 12. Códigos HTTP utilizados

La API REST utiliza códigos HTTP adecuados para indicar el resultado de cada petición.

| Código | Significado | Uso en el proyecto |
| --- | --- | --- |
| `200 OK` | Petición correcta | Consulta de datos |
| `201 Created` | Recurso creado | Alta mediante POST, si el controlador lo devuelve |
| `204 No Content` | Operación correcta sin contenido | Eliminación, si el controlador lo devuelve |
| `401 Unauthorized` | No autenticado | No se envía token o el token no es válido |
| `403 Forbidden` | Sin permisos | Usuario autenticado sin rol suficiente |
| `404 Not Found` | Recurso no encontrado | Recurso inexistente |

---

## 13. Diferencia entre 401 y 403

En la API se diferencia entre autenticación y autorización.

### 401 Unauthorized

Se devuelve cuando el usuario no está autenticado.

Ejemplo:

```text
GET /api/admin/clientes
Sin token
```

Respuesta:

```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "No autorizado. Debes enviar un token JWT válido."
}
```

### 403 Forbidden

Se devuelve cuando el usuario sí está autenticado, pero no tiene permisos suficientes.

Ejemplo:

```text
GET /api/admin/clientes
Token válido de usuario con rol USER
```

Respuesta:

```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "Acceso denegado. No tienes permisos suficientes."
}
```

Esta diferencia es importante porque permite distinguir entre:

```text
No sé quién eres → 401
Sé quién eres, pero no tienes permiso → 403
```

---

## 14. DTOs utilizados

En el Producto 4 se utilizan DTOs para separar la estructura interna de las entidades JPA de los datos expuestos por la API.

Los DTOs permiten:

- Evitar exponer directamente las entidades.
- Controlar qué campos se devuelven en el JSON.
- Evitar problemas de serialización con relaciones JPA.
- Separar datos de entrada y datos de salida.
- Facilitar la evolución de la API.

Ejemplos de DTOs:

```text
ClienteRequestDto
ClienteResponseDto
VehiculoRequestDto
VehiculoResponseDto
AlquilerResponseDto
AuthRequestDto
AuthResponseDto
```

---

## 15. Swagger / OpenAPI

El Producto 4 incorpora Swagger UI para documentar y visualizar los endpoints REST.

La documentación está disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger permite:

- Ver todos los endpoints disponibles.
- Consultar métodos HTTP.
- Revisar parámetros.
- Consultar tipos de respuesta.
- Probar peticiones desde el navegador.
- Facilitar el uso de la API por parte de otros desarrolladores.

---

## 16. Usuarios de prueba

La base de datos inicial incluye usuarios de prueba.

| Usuario | Contraseña | Rol |
| --- | --- | --- |
| `admin` | `admin123` | ADMIN |
| `xavi` | `user123` | USER |

Las contraseñas se almacenan cifradas mediante **BCrypt** en la columna:

```text
password_hash
```

---

## 17. Pruebas con Postman

Se han realizado pruebas de la API con Postman.

### Obtener token

```http
POST /api/auth/login
```

Body:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Resultado esperado:

```text
200 OK
```

con token JWT.

---

### Acceder a endpoint protegido con token ADMIN

```http
GET /api/admin/clientes
```

Authorization:

```text
Bearer Token
```

Resultado esperado:

```text
200 OK
```

---

### Acceder a endpoint protegido sin token

```http
GET /api/admin/clientes
```

Authorization:

```text
No Auth
```

Resultado esperado:

```text
401 Unauthorized
```

---

### Acceder a endpoint protegido con token USER

```http
GET /api/admin/clientes
```

Authorization:

```text
Bearer Token de usuario USER
```

Resultado esperado:

```text
403 Forbidden
```

---

### Acceder a endpoint público sin token

```http
GET /api/clientes
```

Authorization:

```text
No Auth
```

Resultado esperado:

```text
200 OK
```

---

### Crear cliente con token ADMIN

```http
POST /api/admin/clientes
```

Body:

```json
{
  "nombre": "Laura",
  "apellidos": "Martínez API",
  "dni": "12345678Z",
  "telefono": "600123456"
}
```

Resultado esperado:

```text
200 OK
```

o:

```text
201 Created
```

según la implementación del controlador.

---

### Eliminar cliente con token ADMIN

```http
DELETE /api/admin/clientes/{id}
```

Resultado esperado:

```text
200 OK
```

o:

```text
204 No Content
```

según la implementación del controlador.

---

## 18. Base de datos

La aplicación utiliza MySQL como sistema gestor de base de datos.

Tablas principales:

```text
clientes
vehiculos
alquileres
roles
usuarios
```

La relación de seguridad principal es:

```text
Rol 1 ---- N Usuario
```

La relación de auditoría de alquileres es:

```text
Usuario 1 ---- N Alquiler como creado_por
Usuario 1 ---- N Alquiler como modificado_por
```

---

## 19. Scripts de base de datos

Los scripts de inicialización se encuentran en:

```text
docker/mysql/init
├── 01_schema.sql
└── 02_data.sql
```

### `01_schema.sql`

Crea la estructura de la base de datos:

- Base de datos `alquiler_vehiculos`.
- Tabla `roles`.
- Tabla `usuarios`.
- Tabla `clientes`.
- Tabla `vehiculos`.
- Tabla `alquileres`.
- Claves primarias.
- Claves foráneas.
- Relaciones entre tablas.

### `02_data.sql`

Inserta datos iniciales:

- Roles `ADMIN` y `USER`.
- Usuarios iniciales.
- Clientes de prueba.
- Vehículos de prueba.
- Alquileres de prueba.

---

## 20. Ejecución en local

Para ejecutar el proyecto en local es necesario tener instalado:

- JDK 21
- MySQL
- IntelliJ IDEA o IDE equivalente

El proyecto utiliza perfiles de Spring. Para ejecutar en local se debe activar el perfil:

```text
local
```

En IntelliJ IDEA se puede configurar como argumento de ejecución:

```bash
--spring.profiles.active=local
```

Desde terminal, si se dispone de Maven instalado globalmente:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Si no se dispone de Maven global, se puede usar el wrapper del proyecto:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

Swagger estará disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

---

## 21. Ejecución con Docker

Para ejecutar la aplicación con Docker:

```bash
docker compose up --build
```

Si se desea recrear la base de datos desde cero:

```bash
docker compose down -v
docker compose up --build
```

El comando:

```bash
docker compose down -v
```

elimina los volúmenes de Docker. Esto permite que MySQL vuelva a ejecutar los scripts de inicialización `01_schema.sql` y `02_data.sql`.

---

## 22. Comandos útiles de Docker

Ver contenedores activos:

```bash
docker ps
```

Parar contenedores:

```bash
docker compose down
```

Parar contenedores y eliminar volúmenes:

```bash
docker compose down -v
```

Reconstruir la aplicación:

```bash
docker compose up --build
```

---

## 23. Comandos Git habituales

Consultar estado:

```bash
git status
```

Añadir cambios:

```bash
git add .
```

Crear commit:

```bash
git commit -m "mensaje del commit"
```

Subir cambios:

```bash
git push
```

Si es la primera vez que se sube una rama:

```bash
git push -u origin nombre-de-la-rama
```

---

## 24. Estado del producto

El Producto 4 se considera finalizado porque incluye:

- API REST pública para clientes, vehículos y alquileres.
- API REST protegida para operaciones de administración.
- Endpoint de login API.
- Generación de token JWT.
- Validación de token JWT.
- Separación entre seguridad web y seguridad API.
- Respuestas `401 Unauthorized` para peticiones sin token.
- Respuestas `403 Forbidden` para usuarios sin permisos.
- DTOs para entrada y salida de datos.
- Documentación Swagger/OpenAPI.
- Pruebas realizadas con Postman.
- Ejecución local comprobada.
- Ejecución con Docker comprobada.
- Código versionado con Git y GitHub.

---

## 25. Autores

Proyecto desarrollado por el equipo de trabajo de la asignatura **FP.065 - Aplicación back-end con Java en servidores de aplicaciones**.
