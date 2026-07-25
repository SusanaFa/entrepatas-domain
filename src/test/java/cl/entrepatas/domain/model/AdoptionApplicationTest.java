
package cl.entrepatas.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdoptionApplicationTest {

    @Test
    void shouldCreateApplicationWithPendingStatus() {
        // Arrange
        String petId = "pet-001";
        String applicantEmail = "applicant@example.com";

        // Act
        AdoptionApplication application = new AdoptionApplication(petId, applicantEmail);

        // Assert
        assertEquals(petId, application.getPetId());
        assertEquals(applicantEmail, application.getApplicantEmail());
        assertEquals(ApplicationStatus.PENDING, application.getStatus());
    }

    @Test
    void shouldApprovePendingApplication() {
        // Arrange
        AdoptionApplication application = new AdoptionApplication("pet-001", "applicant@example.com");

        // Act
        application.approve();

        // Assert
        assertEquals(ApplicationStatus.APPROVED, application.getStatus());
    }

    @Test
    void shouldRejectPendingApplication() {
        // Arrange
        AdoptionApplication application = new AdoptionApplication("pet-001", "applicant@example.com");

        // Act
        application.reject();

        // Assert
        assertEquals(ApplicationStatus.REJECTED, application.getStatus());
    }

    @Test
    void shouldThrowExceptionWhenApprovingRejectedApplication() {
        // Arrange
        AdoptionApplication application = new AdoptionApplication("pet-001", "applicant@example.com");

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
        AdoptionApplication application = new AdoptionApplication("pet-001", "applicant@example.com");

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

}