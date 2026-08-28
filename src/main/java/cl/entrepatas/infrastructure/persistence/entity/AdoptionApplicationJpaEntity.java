package cl.entrepatas.infrastructure.persistence.entity;

import jakarta.persistence.*;
import cl.entrepatas.domain.valueobject.ApplicationStatus;

@Entity
@Table(name = "adoption_applications", uniqueConstraints = @UniqueConstraint(name = "uk_adoption_application_pet_email", columnNames = {
        "pet_id", "applicant_email" }))
public class AdoptionApplicationJpaEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false, length = 36)
    private String id;

    @Column(name = "pet_id", nullable = false, length = 100)
    private String petId;

    @Column(name = "applicant_email", nullable = false, length = 320)
    private String applicantEmail;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ApplicationStatus status;

    protected AdoptionApplicationJpaEntity() {
    }

    public AdoptionApplicationJpaEntity(
            String id,
            String petId,
            String applicantEmail,
            ApplicationStatus status) {

        this.id = id;
        this.petId = petId;
        this.applicantEmail = applicantEmail;
        this.status = status;
    }

    public String getId() {
        return id;
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