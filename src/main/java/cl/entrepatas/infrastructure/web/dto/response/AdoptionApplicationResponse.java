package cl.entrepatas.infrastructure.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AdoptionApplicationResponse(
                @Schema(description = "Unique identifier of the adoption application", example = "550e8400-e29b-41d4-a716-446655440000") String id,
                @Schema(description = "Identifier of the pet", example = "pet-001") String petId,
                @Schema(description = "Email address of the applicant", example = "susana@example.com") String applicantEmail,
                @Schema(description = "Current status of the application", example = "PENDING", allowableValues = {
                                "PENDING", "APPROVED", "REJECTED" }) String status) {
}