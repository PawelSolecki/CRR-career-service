package com.example.careerservice.util;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UrlValidatorTest {

    @Test
    void shouldPass_correctUrl() {
        assertDoesNotThrow(() -> UrlValidator.validate("https://example.com/", "example.com"));
    }

    @Test
    void shouldThrow_incorrectUrl() {
        assertThrows(IllegalArgumentException.class,
            () -> UrlValidator.validate("https://www.noexample.com", "example.com"));
    }

    @Test
    void shouldThrow_notUrl() {
        assertThrows(MalformedURLException.class,
            () -> UrlValidator.validate("ht!tp://www.example.com", "example.com"));
    }

    @Test
    void shouldThrow_nullUrl() {
        assertThrows(MalformedURLException.class,
            () -> UrlValidator.validate(null, "example.com"));
    }

    @Test
    void shouldPass_subdomain() {
        assertDoesNotThrow(() -> UrlValidator.validate("https://subdomain.example.com", "example.com"));
    }

    @Test
    void caseInsensitiveDomain() {
        assertDoesNotThrow(() -> UrlValidator.validate("https://EXAMPLE.com", "example.COM"));
    }


}