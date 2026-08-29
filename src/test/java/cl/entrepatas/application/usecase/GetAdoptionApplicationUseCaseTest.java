package cl.entrepatas.application.usecase;

import java.util.Optional;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.exception.AdoptionApplicationNotFoundException;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.ApplicationStatus;
import cl.entrepatas.domain.valueobject.PetId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAdoptionApplicationUseCaseTest {

    @Mock
    private AdoptionApplicationRepository repository;

    private GetAdoptionApplicationUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAdoptionApplicationUseCase(repository);
    }

    @Test
    void shouldReturnApplicationWhenItExists() {
        // Arrange
        AdoptionApplicationId applicationId = new AdoptionApplicationId("application-001");

        AdoptionApplication expectedApplication = AdoptionApplication.restore(
                applicationId,
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"),
                ApplicationStatus.APPROVED);

        when(repository.findById(applicationId))
                .thenReturn(Optional.of(expectedApplication));

        // Act
        AdoptionApplication result = useCase.getApplication(applicationId);

        // Assert
        assertSame(expectedApplication, result);

        verify(repository).findById(applicationId);
    }

    @Test
    void shouldThrowExceptionWhenApplicationDoesNotExist() {
        // Arrange
        AdoptionApplicationId applicationId = new AdoptionApplicationId("application-001");

        when(repository.findById(applicationId))
                .thenReturn(Optional.empty());

        // Act
        Executable action = () -> useCase.getApplication(applicationId);

        // Assert
        AdoptionApplicationNotFoundException exception = assertThrows(
                AdoptionApplicationNotFoundException.class,
                action);

        assertEquals(
                "Adoption application not found: application-001",
                exception.getMessage());

        verify(repository).findById(applicationId);
    }
}