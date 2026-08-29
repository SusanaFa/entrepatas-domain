package cl.entrepatas.infrastructure.web.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ApiErrorResponse(
                @Schema(description = "Timestamp of the error", example = "2023-10-27T10:00:00") LocalDateTime timestamp,
                @Schema(description = "HTTP status code", example = "400") int status,
                @Schema(description = "Error code", example = "INVALID_REQUEST") String code,
                @Schema(description = "Error message", example = "Invalid request data") String message,
                @Schema(description = "Request path", example = "/api/v1/adoption-applications") String path) {
}