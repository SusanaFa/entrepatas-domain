package cl.entrepatas.infrastructure.web.controller;

import cl.entrepatas.application.usecase.CreateAdoptionApplicationUseCase;
import cl.entrepatas.application.usecase.GetAdoptionApplicationUseCase;
import cl.entrepatas.domain.entity.AdoptionApplication;
import cl.entrepatas.domain.exception.AdoptionApplicationNotFoundException;
import cl.entrepatas.domain.exception.DuplicateApplicationException;
import cl.entrepatas.domain.valueobject.AdoptionApplicationId;
import cl.entrepatas.domain.valueobject.ApplicantEmail;
import cl.entrepatas.domain.valueobject.ApplicationStatus;
import cl.entrepatas.domain.valueobject.PetId;
import cl.entrepatas.infrastructure.web.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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
@Import(GlobalExceptionHandler.class)
class AdoptionApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateAdoptionApplicationUseCase createUseCase;

    @MockBean
    private GetAdoptionApplicationUseCase getUseCase;

    @Test
    void shouldCreateAdoptionApplication() throws Exception {
        AdoptionApplication application = new AdoptionApplication(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"));

        when(createUseCase.createApplication(
                any(PetId.class),
                any(ApplicantEmail.class)))
                .thenReturn(application);

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
        AdoptionApplication application = AdoptionApplication.restore(
                new AdoptionApplicationId("application-001"),
                new PetId("pet-001"),
                new ApplicantEmail("applicant@example.com"),
                ApplicationStatus.APPROVED);

        when(getUseCase.getApplication(
                new AdoptionApplicationId("application-001")))
                .thenReturn(application);

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
    void shouldReturnBadRequestWhenCreateRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/adoption-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "petId": "pet-001",
                          "applicantEmail": "invalid-email"
                        }
                        """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value("Applicant email must have a valid format"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/adoption-applications"));
    }

    @Test
    void shouldReturnNotFoundWhenApplicationDoesNotExist() throws Exception {
        when(getUseCase.getApplication(
                new AdoptionApplicationId("missing-application")))
                .thenThrow(new AdoptionApplicationNotFoundException(
                        "Adoption application not found: missing-application"));

        mockMvc.perform(get(
                "/api/v1/adoption-applications/missing-application"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.code")
                        .value("ADOPTION_APPLICATION_NOT_FOUND"))
                .andExpect(jsonPath("$.message")
                        .value("Adoption application not found: missing-application"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/adoption-applications/missing-application"));
    }

    @Test
    void shouldReturnUnprocessableEntityWhenApplicationIsDuplicated()
            throws Exception {
        when(createUseCase.createApplication(
                any(PetId.class),
                any(ApplicantEmail.class)))
                .thenThrow(new DuplicateApplicationException(
                        "An adoption application already exists"));

        mockMvc.perform(post("/api/v1/adoption-applications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                          "petId": "pet-001",
                          "applicantEmail": "applicant@example.com"
                        }
                        """))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.code")
                        .value("DUPLICATE_ADOPTION_APPLICATION"))
                .andExpect(jsonPath("$.message")
                        .value("An adoption application already exists"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/adoption-applications"));
    }

    @Test
    void shouldReturnInternalServerErrorWhenUnexpectedErrorOccurs()
            throws Exception {
        when(getUseCase.getApplication(
                new AdoptionApplicationId("application-001")))
                .thenThrow(new RuntimeException("Database connection failed"));

        mockMvc.perform(get(
                "/api/v1/adoption-applications/application-001"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.code").value("INTERNAL_ERROR"))
                .andExpect(jsonPath("$.message")
                        .value("An unexpected error occurred"))
                .andExpect(jsonPath("$.path")
                        .value("/api/v1/adoption-applications/application-001"));
    }
}