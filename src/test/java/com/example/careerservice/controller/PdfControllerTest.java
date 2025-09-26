package com.example.careerservice.controller;

import com.example.careerservice.model.GeneratePdfRequest;
import com.example.careerservice.model.Language;
import com.example.careerservice.service.PdfGeneratorService;
import com.example.careerservice.util.TestDataFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PdfController.class)
class PdfControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private PdfGeneratorService pdfGeneratorService;

    private final TestDataFactory testDataFactory = new TestDataFactory();

    @Test
    void shouldGeneratePdfSuccessfully() throws Exception {
        // Given
        GeneratePdfRequest request = testDataFactory.createValidRequest();
        Resource pdfResource = new ByteArrayResource("dummy-pdf-content".getBytes());
        when(pdfGeneratorService.generatePdf(any())).thenReturn(pdfResource);

        // When & Then
        mockMvc.perform(post("/api/v1/pdf/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"CV_John_Doe.pdf\"; filename*=UTF-8''CV_John_Doe.pdf"))
                .andExpect(content().bytes("dummy-pdf-content".getBytes()));
    }

    @Test
    void shouldReturnNotFound_whenUrlIsInvalid() throws Exception {
        mockMvc.perform(post("/api/v1/pdf/invalid-url")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUnsupportedMediaType_whenContentTypeIsNotJson() throws Exception {
        GeneratePdfRequest request = testDataFactory.createRequestWithMatchingSkills();

        mockMvc.perform(post("/api/v1/pdf/generate")
                        .contentType(MediaType.APPLICATION_XML)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldReturnBadRequest_whenRequestBodyHasValidationErrors() throws Exception {
        // Create request with missing required fields
        GeneratePdfRequest invalidRequest = GeneratePdfRequest.builder()
                .template("simple")
                .language(Language.PL)
                .build(); // Missing userCV, jobOffer, and skillResult

        mockMvc.perform(post("/api/v1/pdf/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnInternalServerError_whenServiceThrowsException() throws Exception {
        GeneratePdfRequest request =testDataFactory.createValidRequest();
        when(pdfGeneratorService.generatePdf(any()))
                .thenThrow(new RuntimeException("PDF generation failed"));

        mockMvc.perform(post("/api/v1/pdf/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnBadRequest_whenTemplateIsInvalid() throws Exception {
        GeneratePdfRequest request = testDataFactory.createRequestWithMatchingSkills();
        request.setTemplate("nonexistent-template");

        mockMvc.perform(post("/api/v1/pdf/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenLanguageIsInvalid() throws Exception {
        String requestWithInvalidLanguage = """
            {
                "userCV": {
                    "personalInfo": {
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john@example.com"
                    }
                },
                "template": "simple",
                "language": "INVALID"
            }
            """;

        mockMvc.perform(post("/api/v1/pdf/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestWithInvalidLanguage))
                .andExpect(status().isBadRequest());
    }
}