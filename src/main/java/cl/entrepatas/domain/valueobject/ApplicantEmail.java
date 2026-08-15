package cl.entrepatas.domain.valueobject;

import java.util.Locale;

/**
 * Represents the normalized email of an adoption applicant.
 */
public record ApplicantEmail(String value) {

    public ApplicantEmail {
        if (value == null) {
            throw new IllegalArgumentException(
                    "Applicant email cannot be null");
        }

        value = value.trim().toLowerCase(Locale.ROOT);

        if (value.isBlank()
                || !value.contains("@")
                || value.startsWith("@")
                || value.endsWith("@")) {

            throw new IllegalArgumentException(
                    "Applicant email has an invalid format");
        }
    }
}