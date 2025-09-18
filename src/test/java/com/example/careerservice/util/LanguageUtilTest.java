package com.example.careerservice.util;

import com.example.careerservice.model.Language;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class LanguageUtilTest {

    @ParameterizedTest
    @EnumSource(Language.class)
    void shouldReturnValidTranslations_forGivenLanguage(Language language) {
        // when
        Map<String, String> translations = LanguageUtil.getTranslations(language);

        // then
        assertEquals(LanguageUtil.ABOUT_ME.get(language), translations.get("ABOUT_ME"));
        assertEquals(LanguageUtil.SKILLS.get(language), translations.get("SKILLS"));
        assertEquals(LanguageUtil.WORK_EXPERIENCE.get(language), translations.get("WORK_EXPERIENCE"));
        assertEquals(LanguageUtil.MATCHED_SKILLS.get(language), translations.get("MATCHED_SKILLS"));
    }

    @Test
    void shouldReturnTrue_whenLanguageIsValid() {
        // when & then
        assertTrue(LanguageUtil.isValidLanguage("PL"));
        assertTrue(LanguageUtil.isValidLanguage("EN"));
    }

    @Test
    void shouldReturnFalse_whenLanguageIsInvalid() {
        // when & then
        assertFalse(LanguageUtil.isValidLanguage("DE"));
        assertFalse(LanguageUtil.isValidLanguage(""));
        assertFalse(LanguageUtil.isValidLanguage(null));
    }
}