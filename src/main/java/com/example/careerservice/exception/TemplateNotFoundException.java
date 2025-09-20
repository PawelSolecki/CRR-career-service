package com.example.careerservice.exception;

public class TemplateNotFoundException extends RuntimeException {
    public TemplateNotFoundException(String templateName) {
        super("Template not found: " + templateName);
    }
}