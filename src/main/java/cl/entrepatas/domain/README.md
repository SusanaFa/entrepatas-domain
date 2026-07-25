# Entre Patas Domain

Core de dominio en Java puro para gestionar solicitudes de adopción de mascotas.

Este proyecto corresponde al Hito 1 y demuestra el modelado de reglas de negocio sin dependencias de frameworks, bases de datos ni componentes web.

## Objetivo

El dominio permite:

- Crear solicitudes de adopción con estado inicial `PENDING`.
- Aprobar solicitudes pendientes.
- Rechazar solicitudes pendientes.
- Impedir transiciones desde estados finales.
- Evitar solicitudes duplicadas para una misma mascota y postulante.

## Arquitectura

El proyecto implementa un core de dominio puro siguiendo principios de Clean Architecture y Ports and Adapters.

```text
AdoptionApplicationService
            |
            v
AdoptionApplicationRepository
            |
            v
External implementation or test mock
```

## Estructura del Proyecto

```text
entrepatas-domain/
├── src/main/java/cl/entrepatas/domain/
│   ├── model/              # Entidades y Value Objects
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

El proyecto utiliza únicamente:

- **JUnit 5** (testing)
- **Mockito** (testing)
- **JaCoCo** (testing)

No hay dependencias de frameworks pesados, bases de datos ni frameworks web.

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

## Licencia

MIT

## Autor

Susana Farías Vera