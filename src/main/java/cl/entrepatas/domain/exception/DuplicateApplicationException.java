package cl.entrepatas.domain.exception;

/**
 * Thrown when an adoption application already exists
 * for the same pet and applicant.
 */
public class DuplicateApplicationException extends RuntimeException {

    public DuplicateApplicationException(String message) {
        super(message);
    }
}