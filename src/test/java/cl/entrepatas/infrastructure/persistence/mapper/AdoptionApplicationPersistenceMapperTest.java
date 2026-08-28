package cl.entrepatas.infrastructure.persistence.mapper;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.ApplicationStatus;
import cl.entrepatas.domain.valueobject.PetId;
import cl.entrepatas.infrastructure.persistence.entity.AdoptionApplicationJpaEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AdoptionApplicationPersistenceMapperTest {

    private final AdoptionApplicationPersistenceMapper mapper = new AdoptionApplicationPersistenceMapper();

    @Test
    void shouldMapDomainApplicationToJpaEntity() {
        // Arrange
        AdoptionApplication application = new AdoptionApplication(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));

        application.approve();

        // Act
        AdoptionApplicationJpaEntity entity = mapper.toEntity(application);

        // Assert
        assertEquals("application-001", entity.getId());
        assertEquals("pet-001", entity.getPetId());
        assertEquals("applicant@example.com", entity.getApplicantEmail());
        assertEquals(ApplicationStatus.APPROVED, entity.getStatus());
    }

    @Test
    void shouldMapJpaEntityToDomainApplication() {
        // Arrange
        AdoptionApplicationJpaEntity entity = new AdoptionApplicationJpaEntity(
                "application-002",
                "pet-002",
                "applicant@example.com",
                ApplicationStatus.REJECTED);

        // Act
        AdoptionApplication application = mapper.toDomain(entity);

        // Assert
        assertEquals(
                new AdoptionApplicationId("application-002"),
                application.getId());
        assertEquals(new PetId("pet-002"), application.getPetId());
        assertEquals(
                new ApplicantEmail("applicant@example.com"),
                application.getApplicantEmail());
        assertEquals(ApplicationStatus.REJECTED, application.getStatus());
    }
}