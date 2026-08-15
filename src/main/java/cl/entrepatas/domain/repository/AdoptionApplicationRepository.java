package cl.entrepatas.domain.repository;

import cl.entrepatas.domain.entity.AdoptionApplication;

/**
 * Defines the operations required to store adoption applications.
 */
public interface AdoptionApplicationRepository {

    boolean existsByPetIdAndApplicantEmail(
            String petId,
            String applicantEmail);

    AdoptionApplication save(AdoptionApplication application);
}