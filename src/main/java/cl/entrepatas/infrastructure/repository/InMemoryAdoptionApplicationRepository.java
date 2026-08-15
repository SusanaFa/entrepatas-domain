package cl.entrepatas.infrastructure.repository;

import java.util.HashMap;
import java.util.Map;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;

public class InMemoryAdoptionApplicationRepository
        implements AdoptionApplicationRepository {

    private final Map<AdoptionApplicationId, AdoptionApplication> applications = new HashMap<>();

    @Override
    public boolean existsByPetIdAndApplicantEmail(
            PetId petId,
            ApplicantEmail applicantEmail) {
        return applications.values().stream()
                .anyMatch(application -> application.getPetId().equals(petId)
                        && application.getApplicantEmail()
                                .equals(applicantEmail));
    }

    @Override
    public AdoptionApplication save(AdoptionApplication application) {
        applications.put(application.getId(), application);
        return application;
    }
}