# Producto 4 - Servicio API RESTful

Este producto amplía el Producto 3 incorporando una API REST bajo la ruta `/api`, seguridad mediante tokens JWT y documentación Swagger/OpenAPI.

## Qué se ha añadido

### 1. Dependencias nuevas en `pom.xml`

- `spring-boot-starter-oauth2-resource-server`: permite validar tokens Bearer JWT en los endpoints protegidos.
- `springdoc-openapi-starter-webmvc-ui`: genera la documentación Swagger UI.

### 2. Endpoints públicos no securizados

Estos endpoints devuelven JSON y se pueden comprobar sin token:

| Método | Ruta | Descripción |
| --- | --- | --- |
| GET | `/api/vehiculos` | Lista todos los vehículos |
| GET | `/api/clientes` | Lista todos los clientes |

### 3. Endpoint para obtener token

| Método | Ruta | Descripción |
| --- | --- | --- |
| POST | `/api/auth/token` | Recibe usuario y contraseña y devuelve un JWT |

Body de ejemplo:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Respuesta esperada:

```json
{
  "tokenType": "Bearer",
  "accessToken": "eyJ...",
  "expiresInSeconds": 3600
}
```

### 4. Endpoints protegidos con token JWT

Estos endpoints requieren cabecera HTTP:

```text
Authorization: Bearer <accessToken>
```

| Método | Ruta | Descripción |
| --- | --- | --- |
| GET | `/api/vehiculos/{id}` | Obtiene un vehículo por id |
| GET | `/api/secure/clientes/{id}` | Obtiene un cliente por id |
| GET | `/api/secure/alquileres` | Lista los alquileres |
| POST | `/api/secure/vehiculos` | Crea un vehículo |
| POST | `/api/secure/clientes` | Crea un cliente |

Con esto se cumple el requisito de tener al menos tres puntos de acceso securizados.

### 5. Swagger UI

La documentación de la API está disponible en:

```text
http://localhost:8080/swagger-ui.html
```

Desde Swagger UI se pueden ver los endpoints y probarlos. Para probar endpoints protegidos:

1. Ejecutar `POST /api/auth/token` con `admin/admin123` o `xavi/user123`.
2. Copiar el campo `accessToken`.
3. Pulsar el botón `Authorize`.
4. Escribir: `Bearer <accessToken>`.
5. Ejecutar los endpoints protegidos.

## Pruebas con Postman

### Obtener token

```http
POST http://localhost:8080/api/auth/token
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### Endpoint público

```http
GET http://localhost:8080/api/vehiculos
```

### Endpoint protegido

```http
GET http://localhost:8080/api/secure/alquileres
Authorization: Bearer <accessToken>
```

### Crear vehículo protegido

```http
POST http://localhost:8080/api/secure/vehiculos
Content-Type: application/json
Authorization: Bearer <accessToken>

{
  "matricula": "9999ZZZ",
  "marca": "Seat",
  "modelo": "Ibiza",
  "tipo": "Turismo",
  "precioDia": 39.99,
  "estado": "DISPONIBLE"
}
```

### Crear cliente protegido

```http
POST http://localhost:8080/api/secure/clientes
Content-Type: application/json
Authorization: Bearer <accessToken>

{
  "nombre": "Laura",
  "apellidos": "Sánchez Ruiz",
  "dni": "11223344C",
  "telefono": "600555666"
}
```

## Archivos principales añadidos o modificados

```text
src/main/java/com/code065/alquilervehiculos/config/SecurityConfig.java
src/main/java/com/code065/alquilervehiculos/config/JwtConfig.java
src/main/java/com/code065/alquilervehiculos/config/OpenApiConfig.java
src/main/java/com/code065/alquilervehiculos/security/JwtTokenService.java
src/main/java/com/code065/alquilervehiculos/controller/api/AuthApiController.java
src/main/java/com/code065/alquilervehiculos/controller/api/VehiculoApiController.java
src/main/java/com/code065/alquilervehiculos/controller/api/ClienteApiController.java
src/main/java/com/code065/alquilervehiculos/controller/api/AlquilerApiController.java
src/main/java/com/code065/alquilervehiculos/dto/api/*.java
```

## Funcionamiento interno

La aplicación mantiene el login web del Producto 3 con sesiones y formularios. Para la API se ha añadido una segunda cadena de seguridad específica para `/api/**`, sin sesiones y con validación de Bearer JWT.

Cuando se hace `POST /api/auth/token`, Spring Security comprueba el usuario con el mismo `CustomUserDetailsService` y las mismas contraseñas BCrypt de la aplicación web. Si las credenciales son correctas, `JwtTokenService` genera un token firmado con la clave `app.jwt.secret` definida en `application.properties`.

Cuando se llama a un endpoint protegido, Spring Security valida la firma y la caducidad del token. Si el token es correcto, la petición entra al controlador REST y devuelve JSON. Si no se envía token o es incorrecto, la API responde con error 401.
