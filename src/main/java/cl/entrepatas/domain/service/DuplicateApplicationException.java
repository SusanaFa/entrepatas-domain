package cl.entrepatas.domain.service;

/**
 * Thrown when an adoption application already exists
 * for the same pet and applicant.
 */
public class DuplicateApplicationException extends RuntimeException {

    public DuplicateApplicationException(String message) {
        super(message);
    }
}