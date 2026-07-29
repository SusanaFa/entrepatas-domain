# Entre Patas Domain

Core de dominio en Java 17 para modelar reglas del proceso de adopción de mascotas.

Este repositorio corresponde a un ejercicio práctico de profundización en Java, diseño de dominio y testing automatizado. Implementa reglas de negocio sin depender de frameworks web, bases de datos o servicios externos.

Forma parte del proyecto [Entre Patas y Hogares](https://github.com/SusanaFa/api-entrepatas).

## Objetivo

El dominio permite:

- Crear solicitudes de adopción con estado inicial `PENDING`.
- Aprobar solicitudes pendientes.
- Rechazar solicitudes pendientes.
- Impedir transiciones desde estados finales.
- Evitar solicitudes duplicadas para una misma mascota y postulante.

## Arquitectura

El proyecto mantiene el dominio independiente de frameworks y aplica una separación inspirada en Clean Architecture y Ports and Adapters. El repositorio define un puerto de salida que puede ser implementado posteriormente por una base de datos, una API externa o un adaptador en memoria.

```text
AdoptionApplicationService
            |
            v
AdoptionApplicationRepository
            |
            v
Implementación externa o mock de prueba
```

## Estructura del Proyecto

```text
entrepatas-domain/
├── src/main/java/cl/entrepatas/domain/
│   ├── model/              # Entidades, estados y excepciones del dominio
│   │   ├── AdoptionApplication.java
│   │   ├── ApplicationStatus.java
│   │   └── InvalidStatusTransitionException.java
│   ├── port/               # Puertos (interfaces que definen interacciones externas)
│   │   └── AdoptionApplicationRepository.java
│   └── service/            # Lógica de negocio (servicios)
│       ├── AdoptionApplicationService.java
│       └── DuplicateApplicationException.java
└── src/test/java/cl/entrepatas/domain/
    ├── model/
    │   └── AdoptionApplicationTest.java
    └── service/
        └── AdoptionApplicationServiceTest.java
```

## Dependencias

El dominio no tiene dependencias de ejecución externas. Para testing y control de calidad utiliza:

- **JUnit 5**: pruebas unitarias.
- **Mockito**: simulación del puerto de persistencia.
- **JaCoCo**: medición y validación de cobertura.
- **Maven Surefire**: ejecución de pruebas.

No hay dependencias de frameworks pesados, bases de datos ni frameworks web.

## Requisitos

- Java 17 o superior.
- Maven 3.9 o superior.

## Ejecución de Tests

Para ejecutar los tests y generar el reporte de cobertura:

```bash
mvn clean verify
```

El reporte de cobertura se genera en:

```
target/site/jacoco/index.html
```

## Reglas de Negocio Implementadas

### 1. Creación de Solicitudes

- Solo se pueden crear solicitudes para mascotas y postulantes únicos.
- Si existe una solicitud previa, se lanza `DuplicateApplicationException`.
- El estado inicial es siempre `PENDING`.

### 2. Transiciones de Estado

- Se permiten transiciones:
  - `PENDING` → `APPROVED`
  - `PENDING` → `REJECTED`
- No se permiten transiciones desde estados finales (`APPROVED`, `REJECTED`).
- Las transiciones inválidas lanzan `InvalidStatusTransitionException`.

## Cobertura de Código

El proyecto exige 100% de cobertura de líneas y branches:

```text
Line coverage: 100%
Branch coverage: 100%
```

## Cómo Extender el Dominio

Para agregar nuevas funcionalidades:

1. Agregar nuevos métodos en `AdoptionApplicationService`.
2. Agregar nuevos métodos en `AdoptionApplicationRepository` si es necesario.
3. Implementar los adaptadores correspondientes en capas externas.
4. Agregar tests en `src/test/java/cl/entrepatas/domain/model/` o `src/test/java/cl/entrepatas/domain/service/` según corresponda.


## Autor

Susana Farías Vera
