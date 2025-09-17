package com.example.careerservice.util;

import java.util.Map;

public class LanguageUtil {

    public static final Map<Language, String> ABOUT_ME = Map.of(
            Language.EN, "About me",
            Language.PL, "O mnie"
    );

    public static final Map<Language, String> POSITION = Map.of(
            Language.EN, "Job Position",
            Language.PL, "Stanowisko"
    );

    public static final Map<Language, String> SKILLS = Map.of(
            Language.EN, "Skills",
            Language.PL, "Umiejętności"
    );

    public static final Map<Language, String> MATCHED_SKILLS = Map.of(
            Language.EN, "Matched skills",
            Language.PL, "Dopasowane umiejętności"
    );

    public static final Map<Language, String> WORK_EXPERIENCE = Map.of(
            Language.EN, "Work experience",
            Language.PL, "Doświadczenie zawodowe"
    );

    public static Map<String, String> getTranslations(Language lang) {
        return Map.of(
                "ABOUT_ME", ABOUT_ME.get(lang),
                "WORK_EXPERIENCE", WORK_EXPERIENCE.get(lang),
                "SKILLS", SKILLS.get(lang),
                "MATCHED_SKILLS", MATCHED_SKILLS.get(lang)
        );
    }
}