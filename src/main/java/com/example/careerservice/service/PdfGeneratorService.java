package com.example.careerservice.service;

import static com.example.careerservice.util.LanguageUtil.getTranslations;

import com.example.careerservice.exception.TemplateNotFoundException;
import com.example.careerservice.exception.UnsupportedLanguageException;
import com.example.careerservice.generator.model.GeneratePdfRequest;
import com.example.careerservice.util.LanguageUtil;
import com.example.careerservice.util.SkillAnalyzer;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.validation.Valid;
import java.io.ByteArrayOutputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.context.Context;
import org.thymeleaf.exceptions.TemplateInputException;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final SpringTemplateEngine templateEngine;

    public Resource generatePdf(@Valid GeneratePdfRequest request) {
        if (!LanguageUtil.isValidLanguage(request.getLanguage().name())) {
            throw new UnsupportedLanguageException(request.getLanguage().name());
        }

        Context context = new Context();
        context.setVariable("cv", request.getUserCV());
        context.setVariable("translations", getTranslations(request.getLanguage()));
        context.setVariable("matchedSkills", SkillAnalyzer.getMatchedSkillNames(request));

        String html;
        try {
            html = templateEngine.process(request.getTemplate(), context);
            if (html == null || html.trim().isEmpty()) {
                throw new TemplateNotFoundException("Template returned empty content: " + request.getTemplate());
            }
        } catch (TemplateInputException e) {
            throw new TemplateNotFoundException("Template not found: " + request.getTemplate());
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(html, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
