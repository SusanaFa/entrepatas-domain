package cl.entrepatas.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenApiConfigurationTest {

    private final OpenApiConfiguration openApiConfiguration = new OpenApiConfiguration();

    @Test
    void shouldCreateOpenApiDefinition() {
        // Arrange
        OpenAPI openApi = openApiConfiguration.entrepatasOpenApi();

        // Assert
        assertAll(
                () -> assertEquals("Entre Patas API", openApi.getInfo().getTitle()),
                () -> assertEquals("v1", openApi.getInfo().getVersion()),
                () -> assertEquals(
                        "API for managing adoption applications",
                        openApi.getInfo().getDescription()));
    }
}
