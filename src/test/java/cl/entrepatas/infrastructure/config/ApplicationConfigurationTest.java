package cl.entrepatas.infrastructure.config;

import cl.entrepatas.application.usecase.CreateAdoptionApplicationUseCase;
import cl.entrepatas.application.usecase.GetAdoptionApplicationUseCase;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
import cl.entrepatas.infrastructure.persistence.adapter.JpaAdoptionApplicationRepositoryAdapter;
import cl.entrepatas.infrastructure.persistence.mapper.AdoptionApplicationPersistenceMapper;
import cl.entrepatas.infrastructure.persistence.repository.SpringDataAdoptionApplicationRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ApplicationConfigurationTest {

    private final ApplicationConfiguration configuration = new ApplicationConfiguration();

    @Test
    void shouldCreatePersistenceMapper() {
        // Act
        AdoptionApplicationPersistenceMapper mapper = configuration.adoptionApplicationPersistenceMapper();

        // Assert
        assertNotNull(mapper);
    }

    @Test
    void shouldCreateJpaRepositoryAdapter() {
        // Arrange
        SpringDataAdoptionApplicationRepository repository = mock(SpringDataAdoptionApplicationRepository.class);

        AdoptionApplicationPersistenceMapper mapper = configuration.adoptionApplicationPersistenceMapper();

        // Act
        AdoptionApplicationRepository adapter = configuration.adoptionApplicationRepository(
                repository,
                mapper);

        // Assert
        assertInstanceOf(
                JpaAdoptionApplicationRepositoryAdapter.class,
                adapter);
    }

    @Test
    void shouldCreateAdoptionApplicationUseCase() {
        // Arrange
        AdoptionApplicationRepository repository = mock(AdoptionApplicationRepository.class);

        // Act
        CreateAdoptionApplicationUseCase useCase = configuration.createAdoptionApplicationUseCase(repository);

        // Assert
        assertNotNull(useCase);
    }

    @Test
    void shouldCreateGetAdoptionApplicationUseCase() {
        // Arrange
        AdoptionApplicationRepository repository = mock(AdoptionApplicationRepository.class);

        // Act
        GetAdoptionApplicationUseCase useCase = configuration.getAdoptionApplicationUseCase(repository);

        // Assert
        assertNotNull(useCase);
    }
}