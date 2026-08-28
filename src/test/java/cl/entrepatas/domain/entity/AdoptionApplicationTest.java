
package cl.entrepatas.domain.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import cl.entrepatas.domain.exception.InvalidStatusTransitionException;
import cl.entrepatas.domain.valueobject.ApplicationStatus;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdoptionApplicationTest {

        @Test
        void shouldCreateApplicationWithIdentityAndPendingStatus() {
                // Arrange
                AdoptionApplicationId applicationId = new AdoptionApplicationId("application-001");

                PetId petId = new PetId("pet-001");

                ApplicantEmail applicantEmail = new ApplicantEmail("applicant@example.com");

                // Act
                AdoptionApplication application = new AdoptionApplication(
                                applicationId,
                                petId,
                                applicantEmail);

                // Assert
                assertEquals(applicationId, application.getId());
                assertEquals(petId, application.getPetId());
                assertEquals(applicantEmail, application.getApplicantEmail());
                assertEquals(ApplicationStatus.PENDING, application.getStatus());
        }

        @Test
        void shouldApprovePendingApplication() {
                // Arrange
                AdoptionApplication application = createPendingApplication();

                // Act
                application.approve();

                // Assert
                assertEquals(ApplicationStatus.APPROVED, application.getStatus());
        }

        @Test
        void shouldRejectPendingApplication() {
                // Arrange
                AdoptionApplication application = createPendingApplication();

                // Act
                application.reject();

                // Assert
                assertEquals(ApplicationStatus.REJECTED, application.getStatus());
        }

        @Test
        void shouldThrowExceptionWhenApprovingRejectedApplication() {
                // Arrange
                AdoptionApplication application = createPendingApplication();

                application.reject();

                // Act
                Executable action = application::approve;

                // Assert
                InvalidStatusTransitionException exception = assertThrows(
                                InvalidStatusTransitionException.class,
                                action);

                assertEquals(
                                "Only pending applications can be approved",
                                exception.getMessage());

                assertEquals(
                                ApplicationStatus.REJECTED,
                                application.getStatus());
        }

        @Test
        void shouldThrowExceptionWhenRejectingApprovedApplication() {
                // Arrange
                AdoptionApplication application = createPendingApplication();

                application.approve();

                // Act
                Executable action = application::reject;

                // Assert
                InvalidStatusTransitionException exception = assertThrows(
                                InvalidStatusTransitionException.class,
                                action);

                assertEquals(
                                "Only pending applications can be rejected",
                                exception.getMessage());

                assertEquals(
                                ApplicationStatus.APPROVED,
                                application.getStatus());
        }

        @Test
        void shouldRestoreApprovedApplication() {
                // Act
                AdoptionApplication application = AdoptionApplication.restore(
                                new AdoptionApplicationId("application-001"),
                                new PetId("pet-001"),
                                new ApplicantEmail("applicant@example.com"),
                                ApplicationStatus.APPROVED);

                // Assert
                assertEquals(ApplicationStatus.APPROVED, application.getStatus());
        }

        @Test
        void shouldRestoreRejectedApplication() {
                // Act
                AdoptionApplication application = AdoptionApplication.restore(
                                new AdoptionApplicationId("application-001"),
                                new PetId("pet-001"),
                                new ApplicantEmail("applicant@example.com"),
                                ApplicationStatus.REJECTED);

                // Assert
                assertEquals(ApplicationStatus.REJECTED, application.getStatus());
        }

        private AdoptionApplication createPendingApplication() {
                return new AdoptionApplication(
                                new AdoptionApplicationId("application-001"),
                                new PetId("pet-001"),
                                new ApplicantEmail("applicant@example.com"));
        }

}