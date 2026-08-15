package cl.entrepatas.domain.valueobject;

/**
 * Represents the unique identifier of an adoption application.
 */
public record AdoptionApplicationId(String value) {

    public AdoptionApplicationId {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Adoption application ID cannot be null");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new IllegalArgumentException(
                    "Adoption application ID cannot be blank");
        }
    }
}