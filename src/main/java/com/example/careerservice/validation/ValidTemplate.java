package com.example.careerservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TemplateValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTemplate {
    String message() default "Invalid template name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}