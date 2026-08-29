package cl.entrepatas.infrastructure.web.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateAdoptionApplicationRequest(

        @NotBlank(message = "Pet ID is required") String petId,

        @NotBlank(message = "Applicant email is required") @Email(message = "Applicant email must have a valid format") String applicantEmail) {
}