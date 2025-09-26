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

        String html = templateEngine.process(String.valueOf(request.getTemplate()), context);

        if (html == null || html.isBlank()) {
            throw new IllegalStateException("Generated HTML is empty");
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            HtmlConverter.convertToPdf(html, outputStream);
            byte[] pdfContent = outputStream.toByteArray();

            if (pdfContent.length == 0) {
                throw new IllegalStateException("Generated PDF is empty");
            }

            Resource resource = new ByteArrayResource(pdfContent);
            if (!resource.exists()) {
                throw new IllegalStateException("Generated PDF resource is invalid");
            }

            return resource;
        } catch (Exception e) {
            log.error("Error generating PDF", e);
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}
