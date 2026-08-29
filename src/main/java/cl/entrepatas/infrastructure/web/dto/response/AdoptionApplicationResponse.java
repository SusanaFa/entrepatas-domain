package cl.entrepatas.infrastructure.web.dto.response;

public record AdoptionApplicationResponse(
        String id,
        String petId,
        String applicantEmail,
        String status) {
}