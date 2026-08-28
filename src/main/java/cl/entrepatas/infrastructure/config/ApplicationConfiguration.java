package cl.entrepatas.infrastructure.config;

import cl.entrepatas.application.usecase.CreateAdoptionApplicationUseCase;
import cl.entrepatas.application.usecase.GetAdoptionApplicationUseCase;
import cl.entrepatas.domain.repository.AdoptionApplicationRepository;
import cl.entrepatas.infrastructure.persistence.adapter.JpaAdoptionApplicationRepositoryAdapter;
import cl.entrepatas.infrastructure.persistence.mapper.AdoptionApplicationPersistenceMapper;
import cl.entrepatas.infrastructure.persistence.repository.SpringDataAdoptionApplicationRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public AdoptionApplicationPersistenceMapper adoptionApplicationPersistenceMapper() {

        return new AdoptionApplicationPersistenceMapper();
    }

    @Bean
    public AdoptionApplicationRepository adoptionApplicationRepository(
            SpringDataAdoptionApplicationRepository repository,
            AdoptionApplicationPersistenceMapper mapper) {

        return new JpaAdoptionApplicationRepositoryAdapter(
                repository,
                mapper);
    }

    @Bean
    public CreateAdoptionApplicationUseCase createAdoptionApplicationUseCase(
            AdoptionApplicationRepository repository) {

        return new CreateAdoptionApplicationUseCase(repository);
    }

    @Bean
    public GetAdoptionApplicationUseCase getAdoptionApplicationUseCase(
            AdoptionApplicationRepository repository) {

        return new GetAdoptionApplicationUseCase(repository);
    }

}