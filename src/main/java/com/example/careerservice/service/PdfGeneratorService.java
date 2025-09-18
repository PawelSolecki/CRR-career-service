package com.example.careerservice.service;

import com.example.careerservice.model.GeneratePdfRequest;
import com.example.careerservice.util.SkillAnalyzer;
import com.itextpdf.html2pdf.HtmlConverter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;

import static com.example.careerservice.util.LanguageUtil.getTranslations;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final SpringTemplateEngine templateEngine;

    public Resource generatePdf(@Valid GeneratePdfRequest request) {
        Context context = new Context();
        context.setVariable("cv", request.getUserCV());
        context.setVariable("translations", getTranslations(request.getLanguage()));
        context.setVariable("matchedSkills", SkillAnalyzer.getMatchedSkillNames(request));

        String html = templateEngine.process(request.getTemplate(), context);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(html, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());
        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
