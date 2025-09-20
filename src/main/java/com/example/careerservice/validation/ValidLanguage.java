package com.example.careerservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = LanguageValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidLanguage {
    String message() default "Unsupported language";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}