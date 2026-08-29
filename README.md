# Entre Patas Domain

API REST para gestionar solicitudes de adopción de mascotas, construida con Java 17 y Spring Boot.

El proyecto mantiene las reglas de negocio independientes de los frameworks y agrega adaptadores para persistencia PostgreSQL, exposición HTTP y documentación OpenAPI.

Forma parte del proyecto [Entre Patas y Hogares](https://github.com/SusanaFa/api-entrepatas).

## Funcionalidades

- Crear solicitudes de adopción con estado inicial `PENDING`.
- Consultar una solicitud de adopción por su identificador.
- Aprobar o rechazar solicitudes pendientes desde el dominio.
- Impedir transiciones desde estados finales.
- Evitar solicitudes duplicadas para la misma mascota y correo de postulante.
- Persistir solicitudes en PostgreSQL.
- Exponer una API REST documentada con OpenAPI y Swagger UI.
- Entregar errores HTTP con un formato estandarizado.

## Arquitectura

El proyecto utiliza una separación de capas inspirada en Clean Architecture y Ports and Adapters.

- **`domain`**: entidades, Value Objects, reglas de negocio, excepciones y puertos. No depende de Spring, JPA ni PostgreSQL.
- **`application`**: casos de uso que coordinan los flujos de negocio.
- **`infrastructure`**: adaptadores concretos para JPA, PostgreSQL, configuración Spring y API REST.

```text
HTTP request
    ↓
REST controller + DTO
    ↓
Application use case
    ↓
Domain entity and repository port
    ↓
JPA repository adapter + mapper
    ↓
PostgreSQL
```

La capa `domain` define el contrato `AdoptionApplicationRepository`. La infraestructura lo implementa mediante `JpaAdoptionApplicationRepositoryAdapter`, sin acoplar el dominio a JPA.

## Estructura principal

```text
src/main/java/cl/entrepatas/
├── application/
│   └── usecase/
├── domain/
│   ├── entity/
│   ├── exception/
│   ├── repository/
│   └── valueobject/
└── infrastructure/
    ├── config/
    ├── persistence/
    │   ├── adapter/
    │   ├── entity/
    │   ├── mapper/
    │   └── repository/
    └── web/
        ├── controller/
        ├── dto/
        └── error/
```

## Reglas de negocio

- Una solicitud nueva comienza en estado `PENDING`.
- Una solicitud `PENDING` puede aprobarse o rechazarse.
- Una solicitud `APPROVED` o `REJECTED` no puede cambiar de estado.
- No puede existir más de una solicitud para la misma combinación de mascota y correo de postulante.
- Si una solicitud no existe, la aplicación responde con un error `404`.

## Requisitos

- Java 17 o superior.
- Maven 3.9 o superior.
- Docker Desktop.

## Base de datos local

PostgreSQL se ejecuta mediante Docker Compose.

```bash
docker compose up -d
docker compose ps
```

El contenedor queda disponible en:

| Propiedad | Valor |
|---|---|
| Motor | PostgreSQL 16 |
| Host | `localhost` |
| Puerto del host | `15432` |
| Base de datos | `entrepatas` |
| Usuario | `entrepatas` |
| Contraseña de desarrollo | `entrepatas_dev` |

Los datos se conservan en el volumen Docker `entrepatas_postgres_data`.

Para detener el contenedor:

```bash
docker compose down
```

> Las credenciales anteriores son exclusivas para desarrollo local. En producción se utilizan variables de entorno.

## Ejecución

### Perfil de desarrollo

El perfil `dev` conecta con PostgreSQL local, permite que Hibernate actualice el esquema y habilita Swagger.

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=dev"
```

La API queda disponible en:

```text
http://localhost:8080
```

### Perfil de producción

El perfil `prod` requiere variables de entorno para la conexión y deshabilita Swagger.

```bash
export DB_URL="jdbc:postgresql://localhost:15432/entrepatas"
export DB_USERNAME="entrepatas"
export DB_PASSWORD="entrepatas_dev"

mvn spring-boot:run "-Dspring-boot.run.profiles=prod"
```

En producción, Hibernate valida el esquema existente y no lo modifica automáticamente.

## API REST

### Crear una solicitud de adopción

```http
POST /api/v1/adoption-applications
Content-Type: application/json
```

```json
{
  "petId": "pet-001",
  "applicantEmail": "susana@example.com"
}
```

Respuesta exitosa:

```http
201 Created
```

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "petId": "pet-001",
  "applicantEmail": "susana@example.com",
  "status": "PENDING"
}
```

Posibles respuestas:

| Código | Situación |
|---|---|
| `201` | Solicitud creada |
| `400` | Datos de entrada inválidos |
| `422` | Ya existe una solicitud para esa mascota y correo |

### Consultar una solicitud

```http
GET /api/v1/adoption-applications/{id}
```

Posibles respuestas:

| Código | Situación |
|---|---|
| `200` | Solicitud encontrada |
| `404` | No existe una solicitud con ese identificador |

## Formato de errores

La API utiliza una respuesta uniforme para los errores:

```json
{
  "timestamp": "2026-08-29T02:30:34.5454618",
  "status": 422,
  "code": "DUPLICATE_ADOPTION_APPLICATION",
  "message": "An application already exists for this pet and applicant",
  "path": "/api/v1/adoption-applications"
}
```

Códigos de error funcionales:

| Código | Estado HTTP |
|---|---:|
| `VALIDATION_ERROR` | `400` |
| `ADOPTION_APPLICATION_NOT_FOUND` | `404` |
| `RESOURCE_NOT_FOUND` | `404` |
| `DUPLICATE_ADOPTION_APPLICATION` | `422` |

## OpenAPI y Swagger

Con el perfil `dev` activo:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Documento OpenAPI: `http://localhost:8080/v3/api-docs`

La documentación incluye los endpoints, DTOs, ejemplos de solicitudes y respuestas, y los códigos HTTP documentados.

Con el perfil `prod`, Swagger UI y `/v3/api-docs` están deshabilitados y responden `404`.

## Tests y cobertura

Para ejecutar la verificación completa:

```bash
mvn verify
```

El proyecto exige y valida mediante JaCoCo:

```text
Line coverage: 100%
Branch coverage: 100%
```

El reporte HTML se genera en:

```text
target/site/jacoco/index.html
```

## Tecnologías

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- PostgreSQL
- Docker Compose
- OpenAPI / Springdoc / Swagger UI
- JUnit 5
- Mockito
- JaCoCo
- Maven

## Autor

Susana Farías Vera
