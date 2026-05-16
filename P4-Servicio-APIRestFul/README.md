# Producto 3 - Seguridad y usuarios

Proyecto desarrollado para la asignatura **FP.065 - Aplicación back-end con Java en servidores de aplicaciones**.

La aplicación implementa una plataforma de gestión de alquiler de vehículos desarrollada con **Java**, **Spring Boot**, **Spring MVC**, **Thymeleaf**, **Spring Data JPA**, **Spring Security**, **MySQL**, **Maven** y **Docker**.

Este Producto 3 amplía el Producto 2 incorporando autenticación, registro de usuarios, roles, rutas protegidas, menú dinámico por rol y auditoría de acciones sobre alquileres.

---

## 1. Objetivo del producto

El objetivo principal del Producto 3 es incorporar seguridad y gestión de usuarios a la aplicación MVC desarrollada en el Producto 2.

Las mejoras principales implementadas son:

- Incorporación de usuarios y roles en la base de datos.
- Creación de las entidades JPA `Usuario` y `Rol`.
- Configuración de Spring Security.
- Formulario de login personalizado.
- Formulario de registro de usuarios.
- Cifrado de contraseñas mediante BCrypt.
- Control de acceso por roles.
- Separación de rutas `/admin/**` y `/user/**`.
- Menú y vistas dinámicas según el rol del usuario.
- Auditoría de alquileres mediante usuario creador y usuario modificador.
- Filtrado de alquileres para que el usuario estándar solo vea sus propios alquileres.
- Gestión básica de usuarios desde el área de administración.
- Página personalizada de acceso denegado.
- Adaptación de Docker para crear la base de datos actualizada.

---

## 2. Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- Thymeleaf
- Thymeleaf Extras Spring Security
- MySQL
- Maven
- Docker
- Docker Compose
- IntelliJ IDEA
- Git y GitHub

---

## 3. Estructura general del proyecto

```text
src/main/java/com/code065/alquilervehiculos
├── config
│   └── SecurityConfig.java
├── controller
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
│   └── UsuarioAutenticadoService.java
└── service
    └── imp
```

La aplicación mantiene una arquitectura por capas:

```text
Vista Thymeleaf → Controller → Service → Repository → Base de datos
```

---

## 4. Modelo de seguridad

La aplicación utiliza dos roles principales:

| Rol | Descripción |
| --- | --- |
| ADMIN | Puede acceder a la administración completa de clientes, vehículos, alquileres y usuarios. |
| USER | Puede acceder al área de usuario y gestionar sus propios alquileres. |

Las rutas principales quedan organizadas así:

| Ruta | Acceso |
| --- | --- |
| `/` | Pública |
| `/login` | Pública |
| `/registro` | Pública |
| `/admin/**` | Solo usuarios con rol ADMIN |
| `/user/**` | Usuarios con rol USER o ADMIN |

---

## 5. Usuarios de prueba

La base de datos inicial incluye usuarios de prueba para comprobar la aplicación.

| Usuario | Contraseña | Rol |
| --- | --- | --- |
| `admin` | `admin123` | ADMIN |
| `xavi` | `user123` | USER |

Las contraseñas se almacenan cifradas mediante **BCrypt** en la columna `password_hash`.

---

## 6. Funcionalidades principales

### Usuario anónimo

Un usuario no autenticado puede:

- Acceder al dashboard público.
- Acceder al formulario de login.
- Registrarse como usuario estándar.
- Ver una versión limitada del menú.

No puede acceder a las zonas protegidas de administración ni de usuario.

### Usuario ADMIN

Un usuario con rol `ADMIN` puede:

- Acceder al área `/admin`.
- Gestionar clientes.
- Gestionar vehículos.
- Gestionar todos los alquileres.
- Consultar qué usuario creó y modificó cada alquiler.
- Gestionar usuarios activando o desactivando cuentas.
- Acceder al menú completo de administración.

### Usuario USER

Un usuario con rol `USER` puede:

- Acceder al área `/user`.
- Crear alquileres.
- Consultar únicamente los alquileres creados por su usuario.
- Ver un menú limitado a las funciones permitidas.
- No puede acceder a clientes, vehículos ni gestión de usuarios.

---

## 7. Base de datos

La base de datos se ha ampliado con las tablas:

```text
roles
usuarios
```

Además, la tabla `alquileres` incorpora los campos:

```text
creado_por
modificado_por
```

Estos campos permiten registrar la auditoría de alquileres:

- `creado_por`: usuario que creó el alquiler.
- `modificado_por`: último usuario que modificó el alquiler.

La relación principal de seguridad es:

```text
Rol 1 ---- N Usuario
Usuario 1 ---- N Alquiler como creado_por
Usuario 1 ---- N Alquiler como modificado_por
```

---

## 8. Scripts de base de datos

Los scripts de inicialización se encuentran en:

```text
docker/mysql/init
├── 01_schema.sql
└── 02_data.sql
```

### `01_schema.sql`

Crea la estructura completa de la base de datos:

- Base de datos `alquiler_vehiculos`.
- Tabla `roles`.
- Tabla `usuarios`.
- Tabla `clientes`.
- Tabla `vehiculos`.
- Tabla `alquileres`.
- Relaciones y claves foráneas.

### `02_data.sql`

Inserta datos iniciales:

- Roles `ADMIN` y `USER`.
- Usuarios iniciales.
- Clientes de prueba.
- Vehículos de prueba.
- Alquileres de prueba.

---

## 9. Seguridad implementada

La seguridad se configura en:

```text
SecurityConfig.java
```

Incluye:

- Rutas públicas.
- Rutas protegidas por rol.
- Login personalizado.
- Logout.
- Página personalizada de acceso denegado.
- Cifrado de contraseñas mediante BCrypt.

La autenticación contra base de datos se realiza mediante:

```text
CustomUserDetailsService.java
```

Este servicio carga el usuario desde MySQL y transforma sus datos al formato utilizado por Spring Security.

Para obtener el usuario autenticado en operaciones internas, como la auditoría de alquileres, se utiliza:

```text
UsuarioAutenticadoService.java
```

---

## 10. Login y registro

La aplicación incluye:

```text
GET  /login
GET  /registro
POST /registro
```

El login es procesado automáticamente por Spring Security.

El registro utiliza el DTO:

```text
RegistroUsuarioDto.java
```

Este DTO recoge los datos del formulario sin exponer directamente la entidad `Usuario`.

Durante el registro se realizan estas operaciones:

- Validar username, email y contraseña.
- Confirmar que las dos contraseñas coinciden.
- Comprobar que no exista otro usuario con el mismo username.
- Comprobar que no exista otro usuario con el mismo email.
- Cifrar la contraseña con BCrypt.
- Asignar el rol `USER` por defecto.
- Guardar el usuario en base de datos.

---

## 11. Rutas principales

### Rutas públicas

```text
/
/login
/registro
/css/**
/js/**
/images/**
/webjars/**
```

### Rutas de administración

```text
/admin/clientes
/admin/vehiculos
/admin/alquileres
/admin/usuarios
```

### Rutas de usuario

```text
/user/alquileres
/user/alquileres/nuevo
```

---

## 12. Gestión básica de usuarios

Se ha añadido una pantalla de administración de usuarios en:

```text
/admin/usuarios
```

Desde esta pantalla el administrador puede:

- Consultar usuarios registrados.
- Ver el rol asignado.
- Ver si el usuario está activo o desactivado.
- Activar cuentas.
- Desactivar cuentas.

No se ha implementado eliminación física de usuarios para conservar la integridad referencial, ya que los usuarios pueden estar relacionados con alquileres mediante `creadoPor` y `modificadoPor`.

---

## 13. Ejecución en local

Para ejecutar el proyecto en local es necesario tener instalado:

- JDK 21
- Maven
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

Desde terminal también se puede ejecutar con:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

La aplicación estará disponible en:

```text
http://localhost:8080
```

---

## 14. Ejecución con Docker

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

## 15. Pruebas realizadas

Se han realizado pruebas con tres tipos de acceso.

### Usuario anónimo

- Acceso al dashboard público.
- Acceso al login.
- Acceso al registro.
- Redirección al login al intentar acceder a rutas protegidas.

### Usuario ADMIN

- Login correcto.
- Acceso a `/admin/clientes`.
- Acceso a `/admin/vehiculos`.
- Acceso a `/admin/alquileres`.
- Acceso a `/admin/usuarios`.
- Creación, edición y eliminación de registros según la funcionalidad disponible.
- Visualización del usuario creador y modificador de alquileres.
- Activación y desactivación de usuarios.

### Usuario USER

- Login correcto.
- Acceso a `/user/alquileres`.
- Creación de alquileres.
- Visualización únicamente de los alquileres creados por el usuario autenticado.
- Bloqueo de acceso a rutas `/admin/**`.
- Visualización de página personalizada de acceso denegado.

También se ha probado la ejecución completa con Docker.

---

## 16. Página de acceso denegado

La aplicación incluye una página personalizada para errores 403.

Cuando un usuario autenticado intenta acceder a una sección para la que no tiene permisos, se muestra una vista de acceso denegado en lugar del error genérico del navegador.

---

## 17. Estado del producto

El Producto 3 se considera finalizado porque incluye:

- Diagrama E/R actualizado con usuarios y roles.
- Modelos JPA actualizados.
- Repositorios y servicios para usuarios y roles.
- Spring Security configurado.
- Login y registro.
- Contraseñas cifradas con BCrypt.
- Roles `ADMIN` y `USER`.
- Rutas `/admin/**` y `/user/**`.
- Menú dinámico según rol.
- Gestión básica de usuarios.
- Auditoría de alquileres.
- Filtrado de alquileres por usuario.
- Página 403 personalizada.
- Docker actualizado y probado.

---

## 18. Comandos Git habituales

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

---

## 19. Autores

Proyecto desarrollado por el equipo de trabajo de la asignatura **FP.065 - Aplicación back-end con Java en servidores de aplicaciones**.

