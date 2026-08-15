package cl.entrepatas.domain.service;

import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.exception.DuplicateApplicationException;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
import cl.entrepatas.domain.valueobject.ApplicationStatus;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdoptionApplicationServiceTest {

        @Mock
        private AdoptionApplicationRepository repository;

        private AdoptionApplicationService service;

        @BeforeEach
        void setUp() {
                service = new AdoptionApplicationService(repository);
        }

        @Test
        void shouldCreateAndSaveApplicationWhenItDoesNotExist() {
                // Arrange
                AdoptionApplicationId applicationId = new AdoptionApplicationId("application-001");

                PetId petId = new PetId("pet-001");

                ApplicantEmail applicantEmail = new ApplicantEmail("applicant@example.com");

                when(repository.existsByPetIdAndApplicantEmail(
                                petId,
                                applicantEmail)).thenReturn(false);

                when(repository.save(any(AdoptionApplication.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                // Act
                AdoptionApplication result = service.createApplication(
                                applicationId,
                                petId,
                                applicantEmail);

                // Assert
                assertNotNull(result);
                assertEquals(applicationId, result.getId());
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
                AdoptionApplicationId applicationId = new AdoptionApplicationId("application-001");

                PetId petId = new PetId("pet-001");

                ApplicantEmail applicantEmail = new ApplicantEmail("applicant@example.com");

                when(repository.existsByPetIdAndApplicantEmail(
                                petId,
                                applicantEmail)).thenReturn(true);

                // Act
                Executable action = () -> service.createApplication(
                                applicationId,
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