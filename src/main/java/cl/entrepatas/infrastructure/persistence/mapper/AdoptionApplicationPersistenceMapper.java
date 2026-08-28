package cl.entrepatas.infrastructure.persistence.mapper;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;
import cl.entrepatas.infrastructure.persistence.entity.AdoptionApplicationJpaEntity;

public class AdoptionApplicationPersistenceMapper {

    public AdoptionApplicationJpaEntity toEntity(
            AdoptionApplication application) {

        return new AdoptionApplicationJpaEntity(
                application.getId().value(),
                application.getPetId().value(),
                application.getApplicantEmail().value(),
                application.getStatus());
    }

    public AdoptionApplication toDomain(
            AdoptionApplicationJpaEntity entity) {

        return AdoptionApplication.restore(
                new AdoptionApplicationId(entity.getId()),
                new PetId(entity.getPetId()),
                new ApplicantEmail(entity.getApplicantEmail()),
                entity.getStatus());
    }
}