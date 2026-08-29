package cl.entrepatas.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class OpenApiConfiguration {
    @Bean
    public OpenAPI entrepatasOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Entre Patas API")
                        .version("v1")
                        .description(
                                "API for managing adoption applications"));
    }
}