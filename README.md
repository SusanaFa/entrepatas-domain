# Entre Patas Domain

Core de dominio en Java 17 para modelar reglas del proceso de adopción de mascotas.

Este repositorio corresponde a un ejercicio práctico de profundización en Java, diseño de dominio y testing automatizado. Implementa reglas de negocio sin depender de frameworks web, bases de datos o servicios externos.

Forma parte del proyecto [Entre Patas y Hogares](https://github.com/SusanaFa/api-entrepatas).

## Objetivo

El proyecto permite:

- Crear solicitudes de adopción con estado inicial `PENDING`.
- Aprobar solicitudes pendientes.
- Rechazar solicitudes pendientes.
- Impedir transiciones desde estados finales.
- Evitar solicitudes duplicadas para una misma mascota y postulante.

## Arquitectura

El proyecto mantiene el dominio independiente de frameworks y utiliza una separación de capas inspirada en Clean Architecture y Ports and Adapters (Hexagonal). Se divide en las siguientes capas:

- **`domain`**: Modelo y reglas centrales del negocio. Es completamente independiente de frameworks, infraestructura o de las capas externas.
- **`application`**: Coordinación de casos de uso (flujos). Depende de la capa `domain`.
- **`infrastructure`**: Implementaciones concretas de los contratos (interfaces) definidos por el dominio. Depende de la capa `domain`.

La regla de dependencias indica que **`application` e `infrastructure` dependen de `domain`**, pero **`domain` no depende de `application` ni de `infrastructure`**.

### Diagrama de Dependencias

```text
CreateAdoptionApplicationUseCase (Capa Application)
                |
                v
  AdoptionApplicationRepository  (Capa Domain / Contrato)
                ^
                |
InMemoryAdoptionApplicationRepository (Capa Infrastructure / Adaptador)
```

- `CreateAdoptionApplicationUseCase` utiliza `AdoptionApplicationRepository`.
- `AdoptionApplicationRepository` es una interfaz del dominio.
- `InMemoryAdoptionApplicationRepository` implementa ese contrato. Esta implementación temporal en memoria pierde sus datos al finalizar la ejecución, y es utilizada como un adaptador sencillo para este ejercicio.

## Estructura del Proyecto

```text
src/main/java/cl/entrepatas/
├── application/
│   └── usecase/
│       └── CreateAdoptionApplicationUseCase.java
├── domain/
│   ├── entity/
│   │   └── AdoptionApplication.java
│   ├── exception/
│   │   ├── DuplicateApplicationException.java
│   │   └── InvalidStatusTransitionException.java
│   ├── repository/
│   │   └── AdoptionApplicationRepository.java
│   └── valueobject/
│       ├── AdoptionApplicationId.java
│       ├── ApplicantEmail.java
│       ├── ApplicationStatus.java
│       └── PetId.java
└── infrastructure/
    └── repository/
        └── InMemoryAdoptionApplicationRepository.java
```

### Estructura de Pruebas

Los tests se reflejan de la misma forma, respetando las capas reales en `src/test/java`:

- `application/usecase`
- `domain/entity`
- `domain/valueobject`
- `infrastructure/repository`

## Reglas de Negocio Implementadas

### 1. Entidades y Value Objects
- **`AdoptionApplication`**: Se representa como una entidad con identidad propia.
- **`AdoptionApplicationId` y `PetId`**: Value Objects que funcionan como identificadores.
- **`ApplicantEmail`**: Value Object de correo.
- Los tres rechazan valores nulos o en blanco. Además, `ApplicantEmail` normaliza y valida el correo mediante las reglas simples implementadas.
- **`ApplicationStatus`**: Representación de todos los estados posibles durante el ciclo de vida de una solicitud.

### 2. Creación de Solicitudes y Casos de Uso
- El flujo de creación de solicitudes es coordinado por `CreateAdoptionApplicationUseCase`.
- El caso de uso consulta `AdoptionApplicationRepository`. Si ya existe la combinación de mascota y postulante, lanza `DuplicateApplicationException`.
- Al caso de uso (`CreateAdoptionApplicationUseCase`) se le inyecta la interfaz `AdoptionApplicationRepository` mediante su constructor, aplicando inyección de dependencias y respetando la inversión de dependencias.
- En toda la capa `domain` y `application` existe una total ausencia de dependencias de frameworks.

### 3. Transiciones de Estado
- Una solicitud se crea con estado inicial `PENDING`.
- Puede pasar de `PENDING` a `APPROVED`.
- Puede pasar de `PENDING` a `REJECTED`.
- No puede cambiar desde un estado final (`APPROVED` o `REJECTED`). Si esto ocurre, lanza una `InvalidStatusTransitionException`.
- Las solicitudes guardadas se mantienen temporalmente en memoria mediante `InMemoryAdoptionApplicationRepository` y se pierden al finalizar la ejecución.

## Cobertura de Código

El proyecto exige 100% de cobertura de líneas y branches:

```text
Line coverage: 100%
Branch coverage: 100%
```

## Dependencias

El dominio no tiene dependencias de ejecución externas. Para testing y control de calidad utiliza:

- **JUnit 5**: pruebas unitarias.
- **Mockito**: simulación del puerto de persistencia para aislar el caso de uso durante las pruebas.
- **JaCoCo**: medición y validación de cobertura.
- **Maven Surefire**: ejecución de pruebas.

El proyecto no utiliza Spring, JPA, bases de datos ni servicios externos.

## Requisitos

- Java 17 o superior.
- Maven 3.9 o superior.

## Ejecución de Tests

Para compilar, ejecutar los tests y verificar el código:

```bash
mvn clean compile
mvn test
mvn clean verify
```

El comando `mvn clean verify` ejecuta la verificación completa y además valida un **100 % de cobertura de líneas y ramas** mediante JaCoCo.
El reporte de cobertura se genera en:

```text
target/site/jacoco/index.html
```

## Cómo Extender el Dominio

Para agregar nuevas funcionalidades respetando la arquitectura:

1. Agregar o modificar reglas de negocio creando nuevas entidades, Value Objects o excepciones en `domain/entity`, `domain/valueobject` o `domain/exception`.
2. Agregar nuevos contratos en `domain/repository` si es necesario.
3. Extender la capa de aplicación implementando nuevos casos de uso en `application/usecase`, comunicándose mediante las interfaces previamente definidas, e inyectándolas por constructor.
4. Implementar los contratos como adaptadores correspondientes en la capa de `infrastructure/repository`.
5. Asegurar un 100% de cobertura incluyendo pruebas en las carpetas correspondientes en `src/test/java/cl/entrepatas/`.

## Autor

Susana Farías Vera
