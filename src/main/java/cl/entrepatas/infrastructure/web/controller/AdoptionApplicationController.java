package cl.entrepatas.infrastructure.web.controller;

import cl.entrepatas.application.usecase.CreateAdoptionApplicationUseCase;
import cl.entrepatas.application.usecase.GetAdoptionApplicationUseCase;
import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;
import cl.entrepatas.infrastructure.web.dto.request.CreateAdoptionApplicationRequest;
import cl.entrepatas.infrastructure.web.dto.response.AdoptionApplicationResponse;
import cl.entrepatas.infrastructure.web.error.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Adoption Applications", description = "Create and retrieve pet adoption applications")
public class AdoptionApplicationController {

        private final CreateAdoptionApplicationUseCase createUseCase;
        private final GetAdoptionApplicationUseCase getUseCase;

        public AdoptionApplicationController(
                        CreateAdoptionApplicationUseCase createUseCase,
                        GetAdoptionApplicationUseCase getUseCase) {
                this.createUseCase = createUseCase;
                this.getUseCase = getUseCase;
        }

        @Operation(summary = "Create an adoption application", description = """
                        Creates a new adoption application with PENDING status.
                        A pet can only have one application per applicant email.
                        """)
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Adoption application created", content = @Content(schema = @Schema(implementation = AdoptionApplicationResponse.class))),
                        @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Validation error", value = """
                                        {
                                          "timestamp": "2026-08-29T02:30:34.5454618",
                                          "status": 400,
                                          "code": "VALIDATION_ERROR",
                                          "message": "Applicant email must have a valid format",
                                          "path": "/api/v1/adoption-applications"
                                        }
                                        """))),
                        @ApiResponse(responseCode = "422", description = """
                                        An application already exists for this pet and applicant
                                        """, content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Duplicate adoption application", value = """
                                        {
                                          "timestamp": "2026-08-29T02:30:34.5454618",
                                          "status": 422,
                                          "code": "DUPLICATE_ADOPTION_APPLICATION",
                                          "message": "An application already exists for this pet and applicant",
                                          "path": "/api/v1/adoption-applications"
                                        }
                                        """)))
        })
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

        @Operation(summary = "Get an adoption application by ID", description = """
                        Retrieves an adoption application using its unique identifier.
                        """)
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Adoption application found", content = @Content(schema = @Schema(implementation = AdoptionApplicationResponse.class))),
                        @ApiResponse(responseCode = "404", description = "Adoption application not found", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class), examples = @ExampleObject(name = "Adoption application not found", value = """
                                        {
                                          "timestamp": "2026-08-29T02:30:34.5454618",
                                          "status": 404,
                                          "code": "ADOPTION_APPLICATION_NOT_FOUND",
                                          "message": "Adoption application not found: application-001",
                                          "path": "/api/v1/adoption-applications/application-001"
                                        }
                                        """)))
        })
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