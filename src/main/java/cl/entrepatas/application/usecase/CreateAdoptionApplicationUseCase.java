package cl.entrepatas.application.usecase;

import java.util.UUID;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.exception.DuplicateApplicationException;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;

public class CreateAdoptionApplicationUseCase {

    private final AdoptionApplicationRepository repository;

    public CreateAdoptionApplicationUseCase(
            AdoptionApplicationRepository repository) {

        this.repository = repository;
    }

    public AdoptionApplication createApplication(
            PetId petId,
            ApplicantEmail applicantEmail) {

        boolean applicationExists = repository.existsByPetIdAndApplicantEmail(
                petId,
                applicantEmail);

        if (applicationExists) {
            throw new DuplicateApplicationException(
                    "An application already exists for this pet and applicant");
        }

        AdoptionApplication application = new AdoptionApplication(
                new AdoptionApplicationId(UUID.randomUUID().toString()),
                petId,
                applicantEmail);

        return repository.save(application);
    }
}