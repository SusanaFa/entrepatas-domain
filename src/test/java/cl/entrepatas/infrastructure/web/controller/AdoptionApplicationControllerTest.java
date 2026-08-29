package cl.entrepatas.infrastructure.web.controller;

import cl.entrepatas.application.usecase.CreateAdoptionApplicationUseCase;
import cl.entrepatas.application.usecase.GetAdoptionApplicationUseCase;
import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.ApplicationStatus;
import cl.entrepatas.domain.valueobject.PetId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdoptionApplicationController.class)
class AdoptionApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateAdoptionApplicationUseCase createUseCase;

    @MockBean
    private GetAdoptionApplicationUseCase getUseCase;

    @Test
    void shouldCreateAdoptionApplication() throws Exception {
        // Arrange
        AdoptionApplication application = new AdoptionApplication(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));

        when(createUseCase.createApplication(
                any(PetId.class),
                any(ApplicantEmail.class)))
                .thenReturn(application);

        // Act and Assert
        mockMvc.perform(post("/api/v1/adoption-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "petId": "pet-001",
                          "applicantEmail": "applicant@example.com"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("application-001"))
                .andExpect(jsonPath("$.petId").value("pet-001"))
                .andExpect(jsonPath("$.applicantEmail")
                        .value("applicant@example.com"))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(createUseCase).createApplication(
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));
    }

    @Test
    void shouldReturnAdoptionApplicationById() throws Exception {
        // Arrange
        AdoptionApplication application = AdoptionApplication.restore(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"),
                ApplicationStatus.APPROVED);

        when(getUseCase.getApplication(
                new AdoptionApplicationId("application-001")))
                .thenReturn(application);

        // Act and Assert
        mockMvc.perform(get(
                "/api/v1/adoption-applications/application-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("application-001"))
                .andExpect(jsonPath("$.petId").value("pet-001"))
                .andExpect(jsonPath("$.applicantEmail")
                        .value("applicant@example.com"))
                .andExpect(jsonPath("$.status").value("APPROVED"));

        verify(getUseCase).getApplication(
                new AdoptionApplicationId("application-001"));
    }

    @Test
    void shouldReturnBadRequestWhenCreateRequestIsInvalid()
            throws Exception {

        mockMvc.perform(post("/api/v1/adoption-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "petId": "",
                          "applicantEmail": "invalid-email"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}