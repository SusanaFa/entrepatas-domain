package cl.entrepatas.domain.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ApplicantEmailTest {

    @Test
    void shouldNormalizeValidEmail() {
        // Arrange
        String value = "  Susana@Example.COM  ";

        // Act
        ApplicantEmail email = new ApplicantEmail(value);

        // Assert
        assertEquals("susana@example.com", email.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            " ",
            "invalid-email",
            "@example.com",
            "susana@"
    })
    void shouldRejectInvalidEmail(String invalidEmail) {
        // Act
        Runnable action = () -> new ApplicantEmail(invalidEmail);

        // Assert
        assertThrows(IllegalArgumentException.class, action::run);
    }
}