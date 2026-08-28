package cl.entrepatas.domain.repository;

import java.util.Optional;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;

/**
 * Defines the operations required to store adoption applications.
 */
public interface AdoptionApplicationRepository {

    boolean existsByPetIdAndApplicantEmail(
            PetId petId,
            ApplicantEmail applicantEmail);

    Optional<AdoptionApplication> findById(
            AdoptionApplicationId applicationId);

    AdoptionApplication save(AdoptionApplication application);
}