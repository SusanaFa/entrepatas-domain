package cl.entrepatas;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

class EntrePatasApplicationTest {

    @Test
    void shouldStartApplication() {
        String[] arguments = {};

        try (MockedStatic<SpringApplication> springApplication = mockStatic(SpringApplication.class)) {

            EntrePatasApplication.main(arguments);

            springApplication.verify(() -> SpringApplication.run(
                    EntrePatasApplication.class,
                    arguments));
        }
    }

    @Test
    void shouldCreateApplicationInstance() {
        // Act
        EntrePatasApplication application = new EntrePatasApplication();

        // Assert
        assertNotNull(application);
    }
}