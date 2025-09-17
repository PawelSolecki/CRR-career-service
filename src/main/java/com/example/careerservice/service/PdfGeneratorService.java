package com.example.careerservice.service;

import com.example.careerservice.client.GotenbergClient;
import com.example.careerservice.generator.model.GeneratePdfRequest;
import com.example.careerservice.util.Language;
import com.example.careerservice.util.SkillAnalyzer;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mock.web.MockMultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static com.example.careerservice.util.LanguageUtil.getTranslations;

@Service
@RequiredArgsConstructor
public class PdfGeneratorService {

    private final SpringTemplateEngine templateEngine;
    private final GotenbergClient gotenbergClient;

    public Resource generatePdf(GeneratePdfRequest request) {
        try {
            Context context = new Context();
            context.setVariable("cv", request.getUserCV());
            context.setVariable("translations", getTranslations(
                    Language.valueOf(request.getLanguage().name())
            ));
            context.setVariable("matchedSkills", SkillAnalyzer.getMatchedSkillNames(request));

            String html = templateEngine.process(request.getTemplate(), context);

            MultipartFile file = new MockMultipartFile(
                    "file",
                    "index.html",
                    "text/html",
                    html.getBytes()
            );

            ResponseEntity<byte[]> response = gotenbergClient.convertHtml(file);

            return new ByteArrayResource(response.getBody());
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }
}