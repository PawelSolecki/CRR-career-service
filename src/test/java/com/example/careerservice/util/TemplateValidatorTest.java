package com.example.careerservice.util;

import com.example.careerservice.validation.TemplateValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TemplateValidatorTest {
    private static final String TEMPLATE_PREFIX = "classpath:/templates/";
    private static final String TEMPLATE_SUFFIX = ".html";
    private static final String TEMPLATE_NAME = "simple";

    @Mock
    private ResourceLoader resourceLoader;
    @Mock
    private Resource resource;
    @Mock
    private SpringResourceTemplateResolver templateResolver;

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
        when(templateResolver.getPrefix()).thenReturn(TEMPLATE_PREFIX);
        when(templateResolver.getSuffix()).thenReturn(TEMPLATE_SUFFIX);
        when(resourceLoader.getResource(TEMPLATE_PREFIX + TEMPLATE_NAME + TEMPLATE_SUFFIX)).thenReturn(resource);
        when(resource.exists()).thenReturn(true);

        // when & then
        assertTrue(validator.isValid(TEMPLATE_NAME, null));
    }

    @Test
    void shouldReturnFalse_whenTemplateProcessingFails() {
        // given
        when(templateResolver.getPrefix()).thenReturn(TEMPLATE_PREFIX);
        when(templateResolver.getSuffix()).thenReturn(TEMPLATE_SUFFIX);
        when(resourceLoader.getResource(TEMPLATE_PREFIX + TEMPLATE_NAME + TEMPLATE_SUFFIX)).thenReturn(resource);
        when(resource.exists()).thenReturn(false);

        // when & then
        assertFalse(validator.isValid(TEMPLATE_NAME, null));
    }
}