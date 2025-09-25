package com.example.careerservice.service;

import com.example.careerservice.exception.TemplateNotFoundException;
import com.example.careerservice.model.*;
import com.example.careerservice.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfGeneratorServiceTest {
    @Mock
    private SpringTemplateEngine templateEngine;
    private PdfGeneratorService pdfGeneratorService;
    private static final String VALID_HTML = "<html><body>Test CV</body></html>";
    private static final String TEMPLATE_NAME = "simple";

    private final TestDataFactory testDataFactory = new TestDataFactory();

    @BeforeEach
    void setUp() {
        pdfGeneratorService = new PdfGeneratorService(templateEngine);
    }

    @Test
    void shouldGeneratePdf_whenRequestIsValid() {
        // given
        GeneratePdfRequest request = testDataFactory.createValidRequest();
        when(templateEngine.process(eq(TEMPLATE_NAME), any(Context.class)))
                .thenReturn(VALID_HTML);

        // when
        Resource result = pdfGeneratorService.generatePdf(request);

        // then
        assertNotNull(result);
        assertTrue(result.exists());
        verify(templateEngine).process(eq(TEMPLATE_NAME), any(Context.class));
    }

    @Test
    void shouldThrowTemplateNotFoundException_whenTemplateNotFound() {
        // given
        GeneratePdfRequest request = testDataFactory.createValidRequest();
        when(templateEngine.process(eq(TEMPLATE_NAME), any(Context.class)))
                .thenThrow(new TemplateNotFoundException("Template not found"));

        // when & then
        assertThrows(TemplateNotFoundException.class,
                () -> pdfGeneratorService.generatePdf(request));
    }

    @Test
    void shouldIncludeMatchedSkills_whenSkillsMatch() {
        // given
        GeneratePdfRequest request = testDataFactory.createRequestWithMatchingSkills();
        when(templateEngine.process(eq(TEMPLATE_NAME), any(Context.class)))
                .thenReturn(VALID_HTML);

        // when
        pdfGeneratorService.generatePdf(request);

        // then
        ArgumentCaptor<Context> contextCaptor = ArgumentCaptor.forClass(Context.class);
        verify(templateEngine).process(eq(TEMPLATE_NAME), contextCaptor.capture());
        Context capturedContext = contextCaptor.getValue();

        @SuppressWarnings("unchecked")
        List<String> matchedSkills = (List<String>) capturedContext.getVariable("matchedSkills");
        assertNotNull(matchedSkills);
        assertTrue(matchedSkills.contains("Java"));
        assertTrue(matchedSkills.contains("Spring"));
    }

    @Test
    void shouldThrowException_whenGeneratedPdfIsNull() {
        // given
        GeneratePdfRequest request = testDataFactory.createValidRequest();
        when(templateEngine.process(eq(TEMPLATE_NAME), any(Context.class)))
                .thenReturn(null);

        // when & then
        assertThrows(IllegalStateException.class,
                () -> pdfGeneratorService.generatePdf(request));
    }
    @Test
    void shouldThrowException_whenHtmlIsEmpty() {
        // given
        GeneratePdfRequest request = testDataFactory.createValidRequest();
        when(templateEngine.process(eq(TEMPLATE_NAME), any(Context.class)))
                .thenReturn("");

        // when & then
        assertThrows(IllegalStateException.class,
                () -> pdfGeneratorService.generatePdf(request));
    }
}