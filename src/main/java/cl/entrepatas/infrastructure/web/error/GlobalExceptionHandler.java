package cl.entrepatas.infrastructure.web.error;

import cl.entrepatas.domain.exception.AdoptionApplicationNotFoundException;
import cl.entrepatas.domain.exception.DuplicateApplicationException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationError(
            MethodArgumentNotValidException exception,
            HttpServletRequest request) {
        String message = exception.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        return buildError(
                HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR",
                message,
                request.getRequestURI());
    }

    @ExceptionHandler(AdoptionApplicationNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            AdoptionApplicationNotFoundException exception,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.NOT_FOUND,
                "ADOPTION_APPLICATION_NOT_FOUND",
                exception.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<ApiErrorResponse> handleDuplicateApplication(
            DuplicateApplicationException exception,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "DUPLICATE_ADOPTION_APPLICATION",
                exception.getMessage(),
                request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpectedError(
            Exception exception,
            HttpServletRequest request) {
        return buildError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "An unexpected error occurred",
                request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildError(
            HttpStatus status,
            String code,
            String message,
            String path) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                code,
                message,
                path);

        return ResponseEntity.status(status).body(errorResponse);
    }
}