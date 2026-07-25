package cl.entrepatas.domain.port;

import cl.entrepatas.domain.model.AdoptionApplication;

/**
 * Defines the operations required to store adoption applications.
 */
public interface AdoptionApplicationRepository {

    boolean existsByPetIdAndApplicantEmail(
            String petId,
            String applicantEmail);

    AdoptionApplication save(AdoptionApplication application);
}