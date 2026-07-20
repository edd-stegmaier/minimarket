# Minimarket

Backend REST para la gestión de un minimarket, desarrollado con Java y Spring Boot.

## Estado actual del proyecto

- API REST con recursos de productos, categorías, inventario, carrito, ventas, detalle de ventas, usuarios y autenticación.
- Arquitectura por capas con paquetes `controller`, `service`, `repository`, `dto`, `entity`, `exception`, `security` y `config`.
- Seguridad stateless con Spring Security + JWT, autorización por roles y control adicional con `@PreAuthorize`.
- Documentación OpenAPI/Swagger UI y respuestas hipermedia con HATEOAS.
- Persistencia con Spring Data JPA sobre H2 en memoria para desarrollo y pruebas.
- Cobertura automatizada de servicios y controladores con 88 pruebas pasando.

## Tecnologías principales

- Java 20
- Spring Boot 3.4.1
- Spring Web
- Spring Security
- Spring Data JPA
- Bean Validation
- H2 Database
- JWT (`jjwt`)
- Jsoup
- Springdoc OpenAPI
- Spring HATEOAS
- JUnit 5, Mockito y JaCoCo

## Seguridad

La aplicación implementa autenticación stateless mediante JWT usando el header `Authorization: Bearer <token>`.

Características actuales de seguridad:

- `SecurityFilterChain` con `SessionCreationPolicy.STATELESS`.
- `httpBasic`, `formLogin`, `logout` de sesión y CSRF deshabilitados para un modelo API stateless.
- `JwtAuthenticationFilter` para validar tokens en cada request protegida.
- `DaoAuthenticationProvider` + `CustomUserDetailsService` para autenticación contra usuarios persistidos.
- Contraseñas cifradas con `BCryptPasswordEncoder`.
- Sanitización de entradas de texto visibles con Jsoup.
- Headers de seguridad mínimos: CSP, `X-Content-Type-Options`, `X-Frame-Options` y HSTS.
- Respuestas unificadas de error con `ApiErrorResponse` para `400`, `401`, `403`, `404` y `500`.

Roles actuales:

- `CLIENTE`: lectura de productos y categorías, operaciones de carrito y consulta general de ventas.
- `EMPLEADO`: puede operar carrito y ventas; además puede crear ventas y trabajar sobre categorías, inventario y detalle de ventas.
- `ADMINISTRADOR`: acceso completo de administración, incluida la gestión de usuarios y la modificación de productos.

## Usuarios iniciales

Al iniciar la aplicación se cargan roles y usuarios base desde `SecurityDataInitializer`:

- `cliente` / `Cliente123!`
- `empleado` / `Empleado123!`
- `administrador` / `administrador123!`

Estos usuarios están pensados para entorno local con H2 en memoria y facilitan probar el flujo de login JWT y las restricciones por rol.

## Estructura del proyecto

- **Código fuente**: `src/main/java/com/minimarket/`
- **Recursos**: `src/main/resources/`
- **Pruebas**: `src/test/java/com/minimarket/`
- **Documentación académica**: `docs/`
- **Reportes generados**: `target/` y `site/jacoco/`

## Cómo ejecutar

Desde la raíz del proyecto puedes usar Maven Wrapper.

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux/macOS:

```bash
./mvnw spring-boot:run
```

También puedes compilar y ejecutar el JAR:

En Windows:

```powershell
.\mvnw.cmd package
java -jar target/minimarket-0.0.1-SNAPSHOT.jar
```

En Linux/macOS:

```bash
./mvnw package
java -jar target/minimarket-0.0.1-SNAPSHOT.jar
```

Configuración local relevante:

- Puerto: `8080`
- Base de datos: H2 en memoria (`jdbc:h2:mem:testdb`)
- Expiración JWT: `3600000` ms

## Autenticación

El login está disponible en `POST /auth/login`.

Ejemplo de request:

```json
{
	"username": "administrador",
	"password": "administrador123!"
}
```

Respuesta esperada:

```json
{
	"token": "<jwt>"
}
```

Luego debes enviar el token en los endpoints protegidos:

```http
Authorization: Bearer <jwt>
```

## Documentación de la API

Con la aplicación en ejecución, la documentación está disponible en:

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

La API documenta actualmente recursos de autenticación, productos, categorías, inventario, carrito, ventas, detalle de ventas, usuarios y un endpoint público de prueba.

## HATEOAS

Las respuestas principales de la API usan `EntityModel` y `CollectionModel`, incluyendo enlaces `_links` para navegación entre recursos relacionados.

Ejemplos de relaciones implementadas:

- Productos con enlaces a su categoría.
- Carritos con enlaces a usuario y producto asociado.
- Ventas con enlaces a usuario y detalles de venta.
- Recursos administrativos con enlaces de self, colección, actualización o eliminación según corresponda.

## Ejecutar pruebas

En Windows:

```powershell
.\mvnw.cmd test
```

En Linux/macOS:

```bash
./mvnw test
```

Para generar cobertura:

En Windows:

```powershell
.\mvnw.cmd clean verify
```

En Linux/macOS:

```bash
./mvnw clean verify
```

Resultado validado en el proyecto:

- 88 pruebas ejecutadas
- 0 failures
- 0 errors
- 0 skipped

