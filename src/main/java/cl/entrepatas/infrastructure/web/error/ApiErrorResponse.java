package cl.entrepatas.infrastructure.web.error;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        LocalDateTime timestamp,
        int status,
        String code,
        String message,
        String path) {
}