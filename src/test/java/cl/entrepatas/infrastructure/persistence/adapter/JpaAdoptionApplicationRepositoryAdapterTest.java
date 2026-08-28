package cl.entrepatas.infrastructure.persistence.adapter;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.ApplicationStatus;
import cl.entrepatas.domain.valueobject.PetId;
import cl.entrepatas.infrastructure.persistence.entity.AdoptionApplicationJpaEntity;
import cl.entrepatas.infrastructure.persistence.mapper.AdoptionApplicationPersistenceMapper;
import cl.entrepatas.infrastructure.persistence.repository.SpringDataAdoptionApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaAdoptionApplicationRepositoryAdapterTest {

    @Mock
    private SpringDataAdoptionApplicationRepository repository;

    @Mock
    private AdoptionApplicationPersistenceMapper mapper;

    @InjectMocks
    private JpaAdoptionApplicationRepositoryAdapter adapter;

    @Test
    void shouldCheckDuplicateApplicationUsingStringValues() {
        // Arrange
        PetId petId = new PetId("pet-001");

        ApplicantEmail applicantEmail = new ApplicantEmail("applicant@example.com");

        when(repository.existsByPetIdAndApplicantEmail(
                "pet-001",
                "applicant@example.com"))
                .thenReturn(true);

        // Act
        boolean exists = adapter.existsByPetIdAndApplicantEmail(
                petId,
                applicantEmail);

        // Assert
        assertTrue(exists);

        verify(repository).existsByPetIdAndApplicantEmail(
                "pet-001",
                "applicant@example.com");
    }

    @Test
    void shouldSaveApplicationUsingMapperAndSpringDataRepository() {
        // Arrange
        AdoptionApplication application = new AdoptionApplication(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));

        application.approve();

        AdoptionApplicationJpaEntity entity = new AdoptionApplicationJpaEntity(
                "application-001",
                "pet-001",
                "applicant@example.com",
                ApplicationStatus.APPROVED);

        AdoptionApplication savedApplication = AdoptionApplication.restore(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"),
                ApplicationStatus.APPROVED);

        when(mapper.toEntity(application)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toDomain(entity)).thenReturn(savedApplication);

        // Act
        AdoptionApplication result = adapter.save(application);

        // Assert
        assertSame(savedApplication, result);

        verify(mapper).toEntity(application);
        verify(repository).save(entity);
        verify(mapper).toDomain(entity);
    }
}