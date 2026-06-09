# Minimarket

Proyecto backend para una tienda Minimarket desarrollado con Java y Spring Boot.

Características principales:
- API REST con controladores para Productos, Categorías, Inventario, Carrito, Usuarios y Ventas.
- Capas bien separadas: `controller`, `service`, `repository`, `dto`, `entity`.
- Manejo de excepciones y respuestas mediante DTOs (`Request/Response`).
- Seguridad básica con filtros/utilidades (implementación JWT disponible en `security`).
- Persistencia con JPA (repositorios) y configuración en `src/main/resources/application.properties`.

Cómo ejecutar (desde la raíz del proyecto):

```bash
mvn spring-boot:run
# o
mvn package
java -jar target/minimarket-0.0.1-SNAPSHOT.jar
```

Ejecutar pruebas:

```bash
mvn test
```
