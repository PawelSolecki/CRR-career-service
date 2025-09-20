package com.example.careerservice.validation;

import com.example.careerservice.model.Language;
import com.example.careerservice.util.LanguageUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LanguageValidator implements ConstraintValidator<ValidLanguage, Language> {
    @Override
    public boolean isValid(Language language, ConstraintValidatorContext context) {
        return LanguageUtil.isValidLanguage(language.name());
    }
}