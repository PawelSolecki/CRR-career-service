package com.example.careerservice.controller;

import com.example.careerservice.generator.model.GeneratePdfRequest;
import com.example.careerservice.service.PdfGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class PdfController {

    private final PdfGeneratorService pdfGeneratorService;

    @PostMapping("/generate")
    public ResponseEntity<Resource> generatePdf(@RequestBody GeneratePdfRequest request) {
        Resource pdf = pdfGeneratorService.generatePdf(request);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"career-match.pdf\"")
                .body(pdf);
    }
}