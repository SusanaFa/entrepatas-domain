package cl.entrepatas.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PetIdTest {

    @Test
    void shouldNormalizeValidPetId() {
        // Arrange
        String value = "  pet-001  ";

        // Act
        PetId petId = new PetId(value);

        // Assert
        assertEquals("pet-001", petId.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "   "
    })
    void shouldRejectInvalidPetId(String invalidPetId) {
        // Act
        Runnable action = () -> new PetId(invalidPetId);

        // Assert
        assertThrows(IllegalArgumentException.class, action::run);
    }
}