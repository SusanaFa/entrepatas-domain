package cl.entrepatas.domain.valueobject;

/**
 * Represents the unique identifier of a pet.
 */
public record PetId(String value) {

    public PetId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Pet ID cannot be null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    "Pet ID cannot be blank");
        }
    }
}