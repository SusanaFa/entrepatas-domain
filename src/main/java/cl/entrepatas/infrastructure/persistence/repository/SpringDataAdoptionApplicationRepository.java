package cl.entrepatas.infrastructure.persistence.repository;

import cl.entrepatas.infrastructure.persistence.entity.AdoptionApplicationJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataAdoptionApplicationRepository
        extends JpaRepository<AdoptionApplicationJpaEntity, String> {

    boolean existsByPetIdAndApplicantEmail(
            String petId,
            String applicantEmail);
}