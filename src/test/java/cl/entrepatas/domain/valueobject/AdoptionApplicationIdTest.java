package cl.entrepatas.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdoptionApplicationIdTest {

    @Test
    void shouldNormalizeValidApplicationId() {
        // Arrange
        String value = "  application-001  ";

        // Act
        AdoptionApplicationId applicationId = new AdoptionApplicationId(value);

        // Assert
        assertEquals(
                "application-001",
                applicationId.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "   "
    })
    void shouldRejectInvalidApplicationId(String invalidApplicationId) {
        // Act
        Runnable action = () -> new AdoptionApplicationId(invalidApplicationId);

        // Assert
        assertThrows(
                IllegalArgumentException.class,
                action::run);
    }
}