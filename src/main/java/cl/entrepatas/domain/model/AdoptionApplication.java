package cl.entrepatas.domain.model;

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
        this.status = ApplicationStatus.APPROVED;
    }

    public void reject() {
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