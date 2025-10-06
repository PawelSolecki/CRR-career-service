package com.example.careerservice.util;

import com.example.careerservice.model.*;

import java.util.ArrayList;
import java.util.List;

public class TestDataFactory {
    private static final String TEMPLATE_NAME = "simple";

    public GeneratePdfRequest createValidRequest() {
        return GeneratePdfRequest.builder()
                .userCV(createBasicUserCV())
                .template(TEMPLATE_NAME)
                .language(Language.PL)
                .build();
    }

    public  GeneratePdfRequest createRequestWithMatchingSkills() {
        List<String> skills = List.of("Java", "Spring", "React");
        List<SkillItem> hardSkills = List.of(
                SkillItem.builder().name("Java").score(0.9).build(),
                SkillItem.builder().name("Spring").score(0.8).build()
        );

        UserCV cv = createBasicUserCV();
        cv.setSkills(skills);

        JobOffer jobOffer = createBasicJobOffer();
        jobOffer.setTechnologies(skills);

        SkillResult skillResult = createBasicSkillResult();
        skillResult.setHardSkills(hardSkills);

        return GeneratePdfRequest.builder()
                .userCV(cv)
                .template(TEMPLATE_NAME)
                .language(Language.PL)
                .build();
    }

    public  UserCV createBasicUserCV() {
        return UserCV.builder()
                .personalInfo(UserCV.PersonalInfo.builder()
                        .firstName("John")
                        .lastName("Doe")
                        .email("john@example.com")
                        .build())
                .build();
    }

    public  JobOffer createBasicJobOffer() {
        return JobOffer.builder()
                .title("Software Engineer")
                .company("Tech Corp")
                .build();
    }

    public  SkillResult createBasicSkillResult() {
        return SkillResult.builder()
                .hardSkills(new ArrayList<>())
                .softSkills(new ArrayList<>())
                .tools(new ArrayList<>())
                .build();
    }
}