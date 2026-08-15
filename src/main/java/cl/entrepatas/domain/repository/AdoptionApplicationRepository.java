package cl.entrepatas.domain.repository;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;

/**
 * Defines the operations required to store adoption applications.
 */
public interface AdoptionApplicationRepository {

    boolean existsByPetIdAndApplicantEmail(
            PetId petId,
            ApplicantEmail applicantEmail);

    AdoptionApplication save(AdoptionApplication application);
}