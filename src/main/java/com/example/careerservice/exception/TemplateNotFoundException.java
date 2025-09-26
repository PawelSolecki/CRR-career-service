package com.example.careerservice.exception;

import org.springframework.http.HttpStatus;

public class TemplateNotFoundException extends AppException {
    public TemplateNotFoundException(String templateName) {
        super("Template not found: " + templateName, HttpStatus.NOT_FOUND);
    }
}