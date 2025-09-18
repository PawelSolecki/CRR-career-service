package com.example.careerservice.service;

import com.example.careerservice.exception.TemplateNotFoundException;
import com.example.careerservice.generator.model.GeneratePdfRequest;
import com.example.careerservice.generator.model.SkillResult;
import com.example.careerservice.generator.model.UserCV;
import com.example.careerservice.scrapper.model.JobOffer;
import com.example.careerservice.util.Language;
import com.example.careerservice.util.LanguageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
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
    private static final String VALID_TEMPLATE = "simple";

    @BeforeEach
    void setUp() {
        pdfGeneratorService = new PdfGeneratorService(templateEngine);
    }

    @Nested
    @DisplayName("Basic PDF Generation")
    class BasicPdfGeneration {

        @Test
        @DisplayName("Should generate PDF when request is valid")
        void shouldGeneratePdf_whenRequestIsValid() throws IOException {
            // given
            GeneratePdfRequest request = createValidRequest();
            when(templateEngine.process(eq(VALID_TEMPLATE), any(Context.class)))
                    .thenReturn(VALID_HTML);

            // when
            Resource result = pdfGeneratorService.generatePdf(request);

            // then
            assertNotNull(result);
            assertTrue(result.getInputStream().readAllBytes().length > 0);
            verify(templateEngine).process(eq(VALID_TEMPLATE), any(Context.class));
        }

        @Test
        @DisplayName("Should generate PDF with Polish language")
        void shouldGeneratePdf_withPolishLanguage() throws IOException {
            // given
            GeneratePdfRequest request = createValidRequestWithLanguage(Language.PL);
            when(templateEngine.process(eq(VALID_TEMPLATE), any(Context.class)))
                    .thenReturn(VALID_HTML);

            // when
            Resource result = pdfGeneratorService.generatePdf(request);

            // then
            assertNotNull(result);
            assertTrue(result.getInputStream().readAllBytes().length > 0);
        }
    }

    @Nested
    @DisplayName("Template Processing")
    class TemplateProcessing {

        @Test
        @DisplayName("Should throw TemplateNotFoundException when template is not found")
        void shouldThrowTemplateNotFoundException_whenTemplateNotFound() {
            // given
            GeneratePdfRequest request = createValidRequest();
            when(templateEngine.process(eq(VALID_TEMPLATE), any(Context.class)))
                    .thenThrow(new TemplateInputException("Template not found"));

            // when & then
            TemplateNotFoundException exception = assertThrows(
                    TemplateNotFoundException.class,
                    () -> pdfGeneratorService.generatePdf(request)
            );
            assertTrue(exception.getMessage().contains("Template not found"));
        }

        @Test
        @DisplayName("Should throw TemplateNotFoundException when template returns empty content")
        void shouldThrowTemplateNotFoundException_whenTemplateReturnsEmptyContent() {
            // given
            GeneratePdfRequest request = createValidRequest();
            when(templateEngine.process(eq(VALID_TEMPLATE), any(Context.class)))
                    .thenReturn("");

            // when & then
            TemplateNotFoundException exception = assertThrows(
                    TemplateNotFoundException.class,
                    () -> pdfGeneratorService.generatePdf(request)
            );
            assertTrue(exception.getMessage().contains("Template returned empty content"));
        }

        @Test
        @DisplayName("Should throw TemplateNotFoundException when template returns null")
        void shouldThrowTemplateNotFoundException_whenTemplateReturnsNull() {
            // given
            GeneratePdfRequest request = createValidRequest();
            when(templateEngine.process(eq(VALID_TEMPLATE), any(Context.class)))
                    .thenReturn(null);

            // when & then
            TemplateNotFoundException exception = assertThrows(
                    TemplateNotFoundException.class,
                    () -> pdfGeneratorService.generatePdf(request)
            );
            assertTrue(exception.getMessage().contains("Template returned empty content"));
        }
    }

    @Nested
    @DisplayName("Language Support")
    class LanguageSupport {

        @Test
        @DisplayName("Should generate PDF when language is PL")
        void shouldGeneratePdf_whenLanguageIsPL() throws IOException {
            // given
            GeneratePdfRequest request = createValidRequestWithLanguage(Language.PL);
            when(templateEngine.process(eq(VALID_TEMPLATE), any(Context.class)))
                    .thenReturn(VALID_HTML);

            // when
            Resource result = pdfGeneratorService.generatePdf(request);

            // then
            assertNotNull(result);
            assertTrue(result.getInputStream().readAllBytes().length > 0);
        }

        @Test
        @DisplayName("Should generate PDF when language is EN")
        void shouldGeneratePdf_whenLanguageIsEN() throws IOException {
            // given
            GeneratePdfRequest request = createValidRequestWithLanguage(Language.EN);
            when(templateEngine.process(eq(VALID_TEMPLATE), any(Context.class)))
                    .thenReturn(VALID_HTML);

            // when
            Resource result = pdfGeneratorService.generatePdf(request);

            // then
            assertNotNull(result);
            assertTrue(result.getInputStream().readAllBytes().length > 0);
        }

        @Test
        @DisplayName("Should validate language strings correctly")
        void shouldValidateLanguageStringsCorrectly() {
            // Valid languages
            assertTrue(LanguageUtil.isValidLanguage("PL"));
            assertTrue(LanguageUtil.isValidLanguage("EN"));

            // Invalid languages
            assertFalse(LanguageUtil.isValidLanguage("FR"));
            assertFalse(LanguageUtil.isValidLanguage(""));
        }
    }


    @Nested
    @DisplayName("Skills Processing")
    class SkillsProcessing {

        @Test
        @DisplayName("Should generate PDF with matched skills")
        void shouldGeneratePdf_withMatchedSkills() throws IOException {
            // given
            GeneratePdfRequest request = createRequestWithMatchedSkills();
            when(templateEngine.process(eq(VALID_TEMPLATE), any(Context.class)))
                    .thenReturn(VALID_HTML);

            // when
            Resource result = pdfGeneratorService.generatePdf(request);

            // then
            assertNotNull(result);
            assertTrue(result.getInputStream().readAllBytes().length > 0);
        }
    }

    // Helper methods
    private GeneratePdfRequest createValidRequest() {
        return createValidRequestWithLanguage(Language.PL);
    }

    private GeneratePdfRequest createValidRequestWithLanguage(Language language) {
        return GeneratePdfRequest.builder()
                .userCV(createValidUserCV())
                .jobOffer(createValidJobOffer())
                .skillResult(createValidSkillResult())
                .template(VALID_TEMPLATE)
                .language(language)
                .build();
    }

    private GeneratePdfRequest createRequestWithMatchedSkills() {
        UserCV cv = createValidUserCV();
        cv.setSkills(List.of("Java", "Spring", "React"));

        JobOffer jobOffer = createValidJobOffer();
        jobOffer.setTechnologies(List.of("Java", "Spring", "Angular"));

        return GeneratePdfRequest.builder()
                .userCV(cv)
                .jobOffer(jobOffer)
                .skillResult(createValidSkillResult())
                .template(VALID_TEMPLATE)
                .language(Language.PL)
                .build();
    }

    private UserCV createValidUserCV() {
        return UserCV.builder()
                .personalInfo(UserCV.PersonalInfo.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .build())
                .build();
    }

    private JobOffer createValidJobOffer() {
        return JobOffer.builder()
                .url("http://example.com/job")
                .company("Tech Corp")
                .title("Software Engineer")
                .description("Job description")
                .technologies(new ArrayList<>())
                .requirements(new ArrayList<>())
                .responsibilities(new ArrayList<>())
                .build();
    }

    private SkillResult createValidSkillResult() {
        return SkillResult.builder()
                .hardSkills(new ArrayList<>())
                .softSkills(new ArrayList<>())
                .tools(new ArrayList<>())
                .build();
    }
}
