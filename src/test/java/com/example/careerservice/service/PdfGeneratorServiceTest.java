package com.example.careerservice.service;

import com.example.careerservice.client.GotenbergClient;
import com.example.careerservice.generator.model.GeneratePdfRequest;
import com.example.careerservice.generator.model.UserCV;
import com.example.careerservice.util.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorServiceTest {

    @Mock
    private SpringTemplateEngine templateEngine;

    @Mock
    private GotenbergClient gotenbergClient;

    private PdfGeneratorService pdfGeneratorService;

    @BeforeEach
    void setUp() {
        pdfGeneratorService = new PdfGeneratorService(templateEngine, gotenbergClient);
    }

    @Test
    void shouldGeneratePdf_whenValidRequest() {
        // given
        GeneratePdfRequest request = createSampleRequest();
        String htmlContent = "<html><body>Test CV</body></html>";
        byte[] pdfContent = "PDF content".getBytes();

        when(templateEngine.process(eq("simple"), any(Context.class))).thenReturn(htmlContent);
        when(gotenbergClient.convertHtml(any(MultipartFile.class)))
                .thenReturn(ResponseEntity.ok(pdfContent));

        // when
        Resource result = pdfGeneratorService.generatePdf(request);

        // then
        assertNotNull(result);
        verify(templateEngine).process(eq("simple"), any(Context.class));
        verify(gotenbergClient).convertHtml(any(MultipartFile.class));
    }

    @Test
    void shouldThrowException_whenTemplateProcessingFails() {
        // given
        GeneratePdfRequest request = createSampleRequest();
        when(templateEngine.process(eq("simple"), any(Context.class)))
                .thenThrow(new RuntimeException("Template processing failed"));

        // when & then
        assertThrows(RuntimeException.class, () -> pdfGeneratorService.generatePdf(request));
    }

    @Test
    void shouldUseDefaultLanguage_whenLanguageNotSet() {
        // given
        GeneratePdfRequest request = new GeneratePdfRequest();
        request.setUserCV(new UserCV());
        String htmlContent = "<html><body>Test CV</body></html>";
        byte[] pdfContent = "PDF content".getBytes();

        when(templateEngine.process(eq("simple"), any(Context.class))).thenReturn(htmlContent);
        when(gotenbergClient.convertHtml(any(MultipartFile.class)))
                .thenReturn(ResponseEntity.ok(pdfContent));

        // when
        Resource result = pdfGeneratorService.generatePdf(request);

        // then
        assertNotNull(result);
        assertEquals(Language.EN, request.getLanguage());
    }

    @Test
    void shouldUseDefaultTemplate_whenTemplateNotSet() {
        // given
        GeneratePdfRequest request = new GeneratePdfRequest();
        request.setUserCV(new UserCV());
        String htmlContent = "<html><body>Test CV</body></html>";
        byte[] pdfContent = "PDF content".getBytes();

        when(templateEngine.process(eq("simple"), any(Context.class))).thenReturn(htmlContent);
        when(gotenbergClient.convertHtml(any(MultipartFile.class)))
                .thenReturn(ResponseEntity.ok(pdfContent));

        // when
        Resource result = pdfGeneratorService.generatePdf(request);

        // then
        assertNotNull(result);
        assertEquals("simple", request.getTemplate());
    }

    @Test
    void shouldThrowException_whenGotenbergClientFails() {
        // given
        GeneratePdfRequest request = createSampleRequest();
        String htmlContent = "<html><body>Test CV</body></html>";

        when(templateEngine.process(eq("simple"), any(Context.class))).thenReturn(htmlContent);
        when(gotenbergClient.convertHtml(any(MultipartFile.class)))
                .thenThrow(new RuntimeException("Conversion failed"));

        // when & then
        Exception exception = assertThrows(RuntimeException.class,
                () -> pdfGeneratorService.generatePdf(request));
        assertTrue(exception.getMessage().contains("Error generating PDF"));
    }

    @Test
    void shouldThrowException_whenUserCVIsNull() {
        // given
        GeneratePdfRequest request = new GeneratePdfRequest();

        // when & then
        Exception exception = assertThrows(RuntimeException.class,
                () -> pdfGeneratorService.generatePdf(request));
        assertTrue(exception.getMessage().contains("Error generating PDF"));
    }

    private GeneratePdfRequest createSampleRequest() {
        UserCV cv = new UserCV();
        UserCV.PersonalInfo personalInfo = new UserCV.PersonalInfo();
        personalInfo.setFirstName("John");
        personalInfo.setLastName("Doe");
        cv.setPersonalInfo(personalInfo);

        GeneratePdfRequest request = new GeneratePdfRequest();
        request.setUserCV(cv);
        request.setLanguage(Language.EN);
        return request;
    }
}