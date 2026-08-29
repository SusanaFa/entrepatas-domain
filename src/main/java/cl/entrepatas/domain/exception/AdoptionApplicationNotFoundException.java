package cl.entrepatas.domain.exception;

public class AdoptionApplicationNotFoundException
        extends RuntimeException {

    public AdoptionApplicationNotFoundException(String message) {
        super(message);
    }
}