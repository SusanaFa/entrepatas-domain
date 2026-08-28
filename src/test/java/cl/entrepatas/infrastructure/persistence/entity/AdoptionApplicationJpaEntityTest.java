package cl.entrepatas.infrastructure.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

class AdoptionApplicationJpaEntityTest {

    @Test
    void shouldCreateEmptyEntityForJpa() {
        // Act
        AdoptionApplicationJpaEntity entity = new AdoptionApplicationJpaEntity();

        // Assert
        assertNull(entity.getId());
        assertNull(entity.getPetId());
        assertNull(entity.getApplicantEmail());
        assertNull(entity.getStatus());
    }
}