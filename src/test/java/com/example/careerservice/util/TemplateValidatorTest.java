package com.example.careerservice.validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TemplateValidatorTest {

    @Mock
    private SpringTemplateEngine templateEngine;

    @InjectMocks
    private TemplateValidator validator;

    @Test
    void shouldReturnFalse_whenTemplateNameIsNull() {
        assertFalse(validator.isValid(null, null));
    }

    @Test
    void shouldReturnFalse_whenTemplateNameIsEmpty() {
        assertFalse(validator.isValid("", null));
        assertFalse(validator.isValid("  ", null));
    }

    @Test
    void shouldReturnTrue_whenTemplateExists() {
        // given
        when(templateEngine.process(eq("simple"), any(Context.class)))
                .thenReturn("<html>template content</html>");

        // when & then
        assertTrue(validator.isValid("simple", null));
    }

    @Test
    void shouldReturnFalse_whenTemplateProcessingFails() {
        // given
        when(templateEngine.process(eq("simple"), any(Context.class)))
                .thenThrow(new RuntimeException("Template not found"));

        // when & then
        assertFalse(validator.isValid("simple", null));
    }
}