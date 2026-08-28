package cl.entrepatas.infrastructure.persistence.adapter;

import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.PetId;
import cl.entrepatas.infrastructure.persistence.entity.AdoptionApplicationJpaEntity;
import cl.entrepatas.infrastructure.persistence.mapper.AdoptionApplicationPersistenceMapper;
import cl.entrepatas.infrastructure.persistence.repository.SpringDataAdoptionApplicationRepository;

public class JpaAdoptionApplicationRepositoryAdapter
        implements AdoptionApplicationRepository {

    private final SpringDataAdoptionApplicationRepository repository;
    private final AdoptionApplicationPersistenceMapper mapper;

    public JpaAdoptionApplicationRepositoryAdapter(
            SpringDataAdoptionApplicationRepository repository,
            AdoptionApplicationPersistenceMapper mapper) {

        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public boolean existsByPetIdAndApplicantEmail(
            PetId petId,
            ApplicantEmail applicantEmail) {

        return repository.existsByPetIdAndApplicantEmail(
                petId.value(),
                applicantEmail.value());
    }

    @Override
    public AdoptionApplication save(AdoptionApplication application) {
        AdoptionApplicationJpaEntity entity = mapper.toEntity(application);

        AdoptionApplicationJpaEntity savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }
}