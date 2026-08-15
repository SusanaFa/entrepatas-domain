package cl.entrepatas.infrastructure.repository;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class InMemoryAdoptionApplicationRepositoryTest {

    @Test
    void shouldSaveAndFindApplicationByPetIdAndApplicantEmail() {
        // Arrange
        InMemoryAdoptionApplicationRepository repository = new InMemoryAdoptionApplicationRepository();

        AdoptionApplication application = new AdoptionApplication(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));

        // Act
        AdoptionApplication savedApplication = repository.save(application);

        // Assert
        assertSame(application, savedApplication);
        assertTrue(repository.existsByPetIdAndApplicantEmail(
                application.getPetId(),
                application.getApplicantEmail()));
    }

    @Test
    void shouldReturnFalseWhenRepositoryIsEmpty() {
        // Arrange
        InMemoryAdoptionApplicationRepository repository = new InMemoryAdoptionApplicationRepository();

        // Act
        boolean applicationExists = repository.existsByPetIdAndApplicantEmail(
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));

        // Assert
        assertFalse(applicationExists);
    }

    @Test
    void shouldReturnFalseWhenPetIdDoesNotMatch() {
        // Arrange
        InMemoryAdoptionApplicationRepository repository = new InMemoryAdoptionApplicationRepository();

        AdoptionApplication application = new AdoptionApplication(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));

        repository.save(application);

        // Act
        boolean applicationExists = repository.existsByPetIdAndApplicantEmail(
                new PetId("pet-002"),
                application.getApplicantEmail());

        // Assert
        assertFalse(applicationExists);
    }

    @Test
    void shouldReturnFalseWhenApplicantEmailDoesNotMatch() {
        // Arrange
        InMemoryAdoptionApplicationRepository repository = new InMemoryAdoptionApplicationRepository();

        AdoptionApplication application = new AdoptionApplication(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));

        repository.save(application);

        // Act
        boolean applicationExists = repository.existsByPetIdAndApplicantEmail(
                application.getPetId(),
                new ApplicantEmail("another@example.com"));

        // Assert
        assertFalse(applicationExists);
    }
}