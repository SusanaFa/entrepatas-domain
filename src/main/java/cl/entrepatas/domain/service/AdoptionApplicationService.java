package cl.entrepatas.domain.service;

import cl.entrepatas.domain.model.AdoptionApplication;
import cl.entrepatas.domain.port.AdoptionApplicationRepository;

public class AdoptionApplicationService {

    private final AdoptionApplicationRepository repository;

    public AdoptionApplicationService(
            AdoptionApplicationRepository repository) {
        this.repository = repository;
    }

    public AdoptionApplication createApplication(
            String petId,
            String applicantEmail) {
        boolean applicationExists = repository.existsByPetIdAndApplicantEmail(
                petId,
                applicantEmail);

        if (applicationExists) {
            throw new DuplicateApplicationException(
                    "An application already exists for this pet and applicant");
        }

        AdoptionApplication application = new AdoptionApplication(petId, applicantEmail);

        return repository.save(application);
    }
}