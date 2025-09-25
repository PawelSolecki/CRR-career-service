package com.example.careerservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Component
@RequiredArgsConstructor
public class TemplateValidator implements ConstraintValidator<ValidTemplate, String> {
    private final SpringResourceTemplateResolver templateResolver;
    private final ResourceLoader resourceLoader;

    @Override
    public void initialize(ValidTemplate constraintAnnotation) {
        templateResolver.setPrefix("classpath:/templates/");
        templateResolver.setSuffix(".html");
    }


    @Override
    public boolean isValid(String templateName, ConstraintValidatorContext context) {
        if (templateName == null || templateName.isBlank()) {
            return false;
        }

        try {
            String templateLocation = templateResolver.getPrefix() + templateName + templateResolver.getSuffix();
            return resourceLoader.getResource(templateLocation).exists();
        } catch (Exception e) {
            return false;
        }
    }
}