package cl.entrepatas.application.usecase;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.exception.AdoptionApplicationNotFoundException;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;

public class GetAdoptionApplicationUseCase {

    private final AdoptionApplicationRepository repository;

    public GetAdoptionApplicationUseCase(
            AdoptionApplicationRepository repository) {

        this.repository = repository;
    }

    public AdoptionApplication getApplication(
            AdoptionApplicationId applicationId) {

        return repository.findById(applicationId)
                .orElseThrow(() -> new AdoptionApplicationNotFoundException(
                        "Adoption application not found: "
                                + applicationId.value()));
    }
}