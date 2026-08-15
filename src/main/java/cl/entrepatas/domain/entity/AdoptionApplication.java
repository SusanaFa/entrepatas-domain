package cl.entrepatas.domain.entity;

import cl.entrepatas.domain.exception.InvalidStatusTransitionException;
import cl.entrepatas.domain.valueobject.ApplicationStatus;

/**
 * Represents an application submitted to adopt a pet.
 */
public class AdoptionApplication {

    private final String petId;
    private final String applicantEmail;
    private ApplicationStatus status;

    public AdoptionApplication(String petId, String applicantEmail) {
        this.petId = petId;
        this.applicantEmail = applicantEmail;
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

    public String getPetId() {
        return petId;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public ApplicationStatus getStatus() {
        return status;
    }
}