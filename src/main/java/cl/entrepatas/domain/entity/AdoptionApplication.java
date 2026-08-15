package cl.entrepatas.domain.entity;

import java.util.Objects;

import cl.entrepatas.domain.exception.InvalidStatusTransitionException;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.ApplicationStatus;
import cl.entrepatas.domain.valueobject.PetId;

/**
 * Represents an application submitted to adopt a pet.
 */
public class AdoptionApplication {

    private final AdoptionApplicationId id;
    private final PetId petId;
    private final ApplicantEmail applicantEmail;
    private ApplicationStatus status;

    public AdoptionApplication(
            AdoptionApplicationId id,
            PetId petId,
            ApplicantEmail applicantEmail) {

        this.id = Objects.requireNonNull(
                id,
                "Adoption application ID cannot be null");

        this.petId = Objects.requireNonNull(
                petId,
                "Pet ID cannot be null");

        this.applicantEmail = Objects.requireNonNull(
                applicantEmail,
                "Applicant email cannot be null");

        this.status = ApplicationStatus.PENDING;
    }

    public void approve() {
        if (status != ApplicationStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Only pending applications can be approved");
        }

        this.status = ApplicationStatus.APPROVED;
    }

    public void reject() {
        if (status != ApplicationStatus.PENDING) {
            throw new InvalidStatusTransitionException(
                    "Only pending applications can be rejected");
        }

        this.status = ApplicationStatus.REJECTED;
    }

    public AdoptionApplicationId getId() {
        return id;
    }

    public PetId getPetId() {
        return petId;
    }

    public ApplicantEmail getApplicantEmail() {
        return applicantEmail;
    }

    public ApplicationStatus getStatus() {
        return status;
    }
}