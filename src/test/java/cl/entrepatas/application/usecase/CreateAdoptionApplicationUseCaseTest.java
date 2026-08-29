package cl.entrepatas.application.usecase;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.exception.DuplicateApplicationException;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAdoptionApplicationUseCaseTest {

        @Mock
        private AdoptionApplicationRepository repository;

        private CreateAdoptionApplicationUseCase useCase;

        @BeforeEach
        void setUp() {
                useCase = new CreateAdoptionApplicationUseCase(repository);
        }

        @Test
        void shouldCreateAndSaveApplicationWhenItDoesNotExist() {
                // Arrange
                PetId petId = new PetId("pet-001");

                ApplicantEmail applicantEmail = new ApplicantEmail("applicant@example.com");

                when(repository.existsByPetIdAndApplicantEmail(
                                petId,
                                applicantEmail)).thenReturn(false);

                when(repository.save(any(AdoptionApplication.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                AdoptionApplication result = useCase.createApplication(
                                petId,
                                applicantEmail);

                // Assert
                assertNotNull(result);
                assertNotNull(result.getId());
                assertEquals(petId, result.getPetId());
                assertEquals(applicantEmail, result.getApplicantEmail());
                assertEquals(ApplicationStatus.PENDING, result.getStatus());

                verify(repository).existsByPetIdAndApplicantEmail(
                                petId,
                                applicantEmail);

                verify(repository).save(any(AdoptionApplication.class));
        }

        @Test
        void shouldThrowExceptionWhenApplicationAlreadyExists() {
                // Arrange
                PetId petId = new PetId("pet-001");

                ApplicantEmail applicantEmail = new ApplicantEmail("applicant@example.com");

                when(repository.existsByPetIdAndApplicantEmail(
                                petId,
                                applicantEmail)).thenReturn(true);

                // Act
                Executable action = () -> useCase.createApplication(
                                petId,
                                applicantEmail);

                // Assert
                DuplicateApplicationException exception = assertThrows(
                                DuplicateApplicationException.class,
                                action);

                assertEquals(
                                "An application already exists for this pet and applicant",
                                exception.getMessage());

                verify(repository).existsByPetIdAndApplicantEmail(
                                petId,
                                applicantEmail);

                verify(repository, never())
                                .save(any(AdoptionApplication.class));
        }
}