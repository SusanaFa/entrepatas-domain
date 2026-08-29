package cl.entrepatas.infrastructure.web.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAdoptionApplicationRequest(
                @Schema(description = "Identifier of the pet to adopt", example = "pet-001") @NotBlank(message = "Pet ID is required") String petId,

                @Schema(description = "Email address of the adoption applicant", example = "susana@example.com") @NotBlank(message = "Applicant email is required") @Email(message = "Applicant email must have a valid format") String applicantEmail) {
}