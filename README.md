# Gestión Pago Proveedores

Proyecto Spring Boot para gestionar proveedores y órdenes de pago con reportes de montos pagados y órdenes próximas a vencer.

## Arquitectura

- Spring Boot 3.5.x con Java 17.
- Arquitectura por capas:
  - `controller`: expone endpoints REST.
  - `service`: lógica de negocio y validaciones.
  - `repo`: acceso a datos con Spring Data JPA.
  - `model`: entidades JPA.
  - `dto`: objetos de transferencia para solicitudes y respuestas.
  - `config`: configuración de beans, OpenAPI y ModelMapper.
  - `exception`: manejo centralizado de errores.
  - `util`: utilidades comunes para fechas y validación.

## Requisitos previos

- Java 17
- Maven 3.x o uso de `mvnw`
- Base de datos PostgreSQL para ejecución productiva
- H2 se incluye para pruebas y desarrollo local en memoria

## Ejecutar localmente

1. En la raíz del proyecto:
   ```bash
   ./mvnw clean package
   ```
2. Ejecutar la aplicación:
   ```bash
   ./mvnw spring-boot:run
   ```
   o
   ```bash
   java -jar target/gestionPagoProveedores-0.0.1-SNAPSHOT.war
   ```
3. Acceder a la API en:
   - `http://localhost:8093`

## Ejecutar pruebas

```bash
./mvnw test
```

## Swagger / OpenAPI

La documentación interactiva está disponible en:

- `http://localhost:8080/swagger-ui/index.html`

## Endpoints relevantes

- `GET /reportesOrdenP/ordenesProximasVencer`
- `GET /reportesOrdenP/totalPagadoProveedor?idProveedor={id}&fechaInicio={yyyy-MM-dd}&fechaFin={yyyy-MM-dd}`

## Decisiones de diseño

- `Spring Boot` con capas separadas: facilita mantenimiento, pruebas y escalabilidad.
- `ModelMapper`: reduce el código de transformación entre entidades y DTOs.
- `DTOs` para desacoplar el API público de las entidades JPA y controlar los datos expuestos.
- `Spring Data JPA`: abstrae consultas SQL y permite repositorios sencillos.
- `Bean Validation` (`spring-boot-starter-validation`): valida datos de entrada en los endpoints REST.
- `springdoc-openapi` para documentación Swagger automática: mejora la usabilidad del API.
- `@RestControllerAdvice`: centraliza el manejo de excepciones y mejora la consistencia de errores.
- `H2` como dependencia de `runtime` para pruebas y arranque local rápido, junto con `postgresql` para despliegue real.
- `war` packaging y `spring-boot-starter-tomcat` `provided`: permite desplegar en contenedores externos si es necesario.

## Pendientes

- Idempotencia en la creación de órdenes mediante un header `Idempotency-Key`: este mecanismo evita la creación de órdenes duplicadas cuando el cliente reintenta una petición y el servidor ya procesó la misma petición anteriormente.
- Manejo de concurrencia en la transición de estado de una orden: se refiere a proteger los cambios de estado frente a actualizaciones simultáneas, para que dos procesos no modifiquen la misma orden al mismo tiempo y generen resultados inconsistentes.

No se pudo implementar porque se revisó al final y no hubo tiempo suficiente; no quise tomar riesgos de que al final el proceso de dañara. Sin embargo, son mejoras importantes para garantizar la seguridad y consistencia de las operaciones en un sistema de pagos.

## Notas especiales

- El reporte de órdenes próximas a vencer se calcula sobre `fechaCreacion` y suma días hábiles para determinar la fecha de vencimiento.
- La validación de parámetros de fecha utiliza `Utilidades.parseFecha` con formatos `yyyy-MM-dd` y `yyyy-MM-dd'T'HH:mm:ss`.
