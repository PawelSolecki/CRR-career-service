package com.example.careerservice.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilenameUtilTest {

    @Test
    void shouldCreateBasicContentDisposition_whenNamesAreAscii() {
        // when
        String result = FilenameUtil.getContentDispositionHeader("John", "Doe");

        // then
        String expected = "attachment; filename=\"CV_John_Doe.pdf\"; filename*=UTF-8''CV_John_Doe.pdf";
        assertEquals(expected, result);
    }

    @Test
    void shouldHandleNonAsciiCharacters_whenNamesContainDiacritics() {
        // when
        String result = FilenameUtil.getContentDispositionHeader("Łukasz", "Kowalski");

        // then
        String expected = "attachment; filename=\"CV_ukasz_Kowalski.pdf\"; " +
                "filename*=UTF-8''CV_%C5%81ukasz_Kowalski.pdf";
        assertEquals(expected, result);
    }

    @Test
    void shouldHandleSpaces_whenNamesContainSpaces() {
        // when
        String result = FilenameUtil.getContentDispositionHeader("John Paul", "von Doe");

        // then
        String expected = "attachment; filename=\"CV_John Paul_von Doe.pdf\"; " +
                "filename*=UTF-8''CV_John%20Paul_von%20Doe.pdf";
        assertEquals(expected, result);
    }

    @Test
    void shouldHandleSpecialCharacters_whenNamesContainSpecialChars() {
        // when
        String result = FilenameUtil.getContentDispositionHeader("John&Paul", "O'Doe");

        // then
        String expected = "attachment; filename=\"CV_John&Paul_O'Doe.pdf\"; " +
                "filename*=UTF-8''CV_John%26Paul_O%27Doe.pdf";
        assertEquals(expected, result);
    }
}