package com.example.careerservice.service;

import com.example.careerservice.exception.TemplateNotFoundException;
import com.example.careerservice.model.*;
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

    @BeforeEach
    void setUp() {
        pdfGeneratorService = new PdfGeneratorService(templateEngine);
    }

    @Test
    void shouldGeneratePdf_whenRequestIsValid() {
        // given
        GeneratePdfRequest request = createValidRequest();
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
        GeneratePdfRequest request = createValidRequest();
        when(templateEngine.process(eq(TEMPLATE_NAME), any(Context.class)))
                .thenThrow(new TemplateNotFoundException("Template not found"));

        // when & then
        assertThrows(TemplateNotFoundException.class,
                () -> pdfGeneratorService.generatePdf(request));
    }

    @Test
    void shouldIncludeMatchedSkills_whenSkillsMatch() {
        // given
        GeneratePdfRequest request = createRequestWithMatchingSkills();
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


    private GeneratePdfRequest createValidRequest() {
        return GeneratePdfRequest.builder()
                .userCV(createBasicUserCV())
                .jobOffer(createBasicJobOffer())
                .skillResult(createBasicSkillResult())
                .template(TEMPLATE_NAME)
                .language(Language.PL)
                .build();
    }

    private GeneratePdfRequest createRequestWithMatchingSkills() {
        List<String> skills = List.of("Java", "Spring", "React");
        List<SkillItem> hardSkills = List.of(
                SkillItem.builder().name("Java").score(0.9).build(),
                SkillItem.builder().name("Spring").score(0.8).build()
        );

        UserCV cv = createBasicUserCV();
        cv.setSkills(skills);

        JobOffer jobOffer = createBasicJobOffer();
        jobOffer.setTechnologies(skills);

        SkillResult skillResult = createBasicSkillResult();
        skillResult.setHardSkills(hardSkills);

        return GeneratePdfRequest.builder()
                .userCV(cv)
                .jobOffer(jobOffer)
                .skillResult(skillResult)
                .template(TEMPLATE_NAME)
                .language(Language.PL)
                .build();
    }

    private UserCV createBasicUserCV() {
        return UserCV.builder()
                .personalInfo(UserCV.PersonalInfo.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .build())
                .build();
    }

    private JobOffer createBasicJobOffer() {
        return JobOffer.builder()
                .title("Software Engineer")
                .company("Tech Corp")
                .build();
    }

    private SkillResult createBasicSkillResult() {
        return SkillResult.builder()
                .hardSkills(new ArrayList<>())
                .softSkills(new ArrayList<>())
                .tools(new ArrayList<>())
                .build();
    }
}