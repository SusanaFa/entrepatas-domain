package cl.entrepatas.infrastructure.web.controller;

import cl.entrepatas.application.usecase.CreateAdoptionApplicationUseCase;
import cl.entrepatas.application.usecase.GetAdoptionApplicationUseCase;
import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;
import cl.entrepatas.infrastructure.web.dto.request.CreateAdoptionApplicationRequest;
import cl.entrepatas.infrastructure.web.dto.response.AdoptionApplicationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/adoption-applications")
public class AdoptionApplicationController {

    private final CreateAdoptionApplicationUseCase createUseCase;
    private final GetAdoptionApplicationUseCase getUseCase;

    public AdoptionApplicationController(
            CreateAdoptionApplicationUseCase createUseCase,
            GetAdoptionApplicationUseCase getUseCase) {

        this.createUseCase = createUseCase;
        this.getUseCase = getUseCase;
    }

    @PostMapping
    public ResponseEntity<AdoptionApplicationResponse> create(
            @Valid @RequestBody CreateAdoptionApplicationRequest request) {

        AdoptionApplication application = createUseCase.createApplication(
                new PetId(request.petId()),
                new ApplicantEmail(request.applicantEmail()));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(application));
    }

    @GetMapping("/{id}")
    public AdoptionApplicationResponse getById(
            @PathVariable("id") String id) {

        AdoptionApplication application = getUseCase.getApplication(
                new AdoptionApplicationId(id));

        return toResponse(application);
    }

    private AdoptionApplicationResponse toResponse(
            AdoptionApplication application) {

        return new AdoptionApplicationResponse(
                application.getId().value(),
                application.getPetId().value(),
                application.getApplicantEmail().value(),
                application.getStatus().name());
    }
}