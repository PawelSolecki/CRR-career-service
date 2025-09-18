package com.example.careerservice.util;

import com.example.careerservice.model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SkillAnalyzerTest {

    @Test
    void shouldReturnEmptyList_whenAllSkillListsAreEmpty() {
        // given
        SkillResult skillResult = SkillResult.builder()
                .hardSkills(List.of())
                .softSkills(List.of())
                .tools(List.of())
                .build();

        GeneratePdfRequest request = GeneratePdfRequest.builder()
                .userCV(UserCV.builder().build())
                .jobOffer(JobOffer.builder().build())
                .skillResult(skillResult)
                .build();

        // when
        List<String> result = SkillAnalyzer.getMatchedSkillNames(request);

        // then
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldMatchHardSkills_whenSkillsExistInBothCVAndJobOffer() {
        // given
        List<String> cvSkills = List.of("Java", "Spring", "React");
        List<String> jobSkills = List.of("Java", "Spring", "Python");
        List<SkillItem> hardSkills = List.of(
                SkillItem.builder().name("Java").score(0.9).build(),
                SkillItem.builder().name("Spring").score(0.8).build(),
                SkillItem.builder().name("Python").score(0.7).build()
        );

        GeneratePdfRequest request = createRequest(cvSkills, jobSkills, hardSkills);

        // when
        List<String> result = SkillAnalyzer.getMatchedSkillNames(request);

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains("Java"));
        assertTrue(result.contains("Spring"));
    }

    @Test
    void shouldMatchCaseInsensitive() {
        // given
        List<String> cvSkills = List.of("JAVA", "spring");
        List<String> jobSkills = List.of("Java", "Spring");
        List<SkillItem> hardSkills = List.of(
                SkillItem.builder().name("java").score(0.9).build(),
                SkillItem.builder().name("SPRING").score(0.8).build()
        );

        GeneratePdfRequest request = createRequest(cvSkills, jobSkills, hardSkills);

        // when
        List<String> result = SkillAnalyzer.getMatchedSkillNames(request);

        // then
        assertEquals(2, result.size());
    }

    @Test
    void shouldCombineAllSkillTypes() {
        // given
        List<String> cvSkills = List.of("Java", "Leadership", "Jenkins");
        List<String> jobSkills = List.of("Java", "Leadership", "Jenkins");

        List<SkillItem> hardSkills = List.of(
                SkillItem.builder().name("Java").score(0.9).build()
        );
        List<SkillItem> softSkills = List.of(
                SkillItem.builder().name("Leadership").score(0.8).build()
        );
        List<SkillItem> tools = List.of(
                SkillItem.builder().name("Jenkins").score(0.7).build()
        );

        GeneratePdfRequest request = createRequestWithAllSkillTypes(
                cvSkills, jobSkills, hardSkills, softSkills, tools);

        // when
        List<String> result = SkillAnalyzer.getMatchedSkillNames(request);

        // then
        assertEquals(3, result.size());
        assertTrue(result.contains("Java"));
        assertTrue(result.contains("Leadership"));
        assertTrue(result.contains("Jenkins"));
    }

    private GeneratePdfRequest createRequestWithNullSkillResult() {
        return GeneratePdfRequest.builder()
                .userCV(UserCV.builder().build())
                .jobOffer(JobOffer.builder().build())
                .skillResult(null)
                .build();
    }

    private GeneratePdfRequest createRequest(
            List<String> cvSkills,
            List<String> jobSkills,
            List<SkillItem> hardSkills
    ) {
        UserCV cv = UserCV.builder().skills(cvSkills).build();
        JobOffer jobOffer = JobOffer.builder().technologies(jobSkills).build();
        SkillResult skillResult = SkillResult.builder().hardSkills(hardSkills).build();

        return GeneratePdfRequest.builder()
                .userCV(cv)
                .jobOffer(jobOffer)
                .skillResult(skillResult)
                .build();
    }

    private GeneratePdfRequest createRequestWithAllSkillTypes(
            List<String> cvSkills,
            List<String> jobSkills,
            List<SkillItem> hardSkills,
            List<SkillItem> softSkills,
            List<SkillItem> tools
    ) {
        UserCV cv = UserCV.builder().skills(cvSkills).build();
        JobOffer jobOffer = JobOffer.builder().technologies(jobSkills).build();
        SkillResult skillResult = SkillResult.builder()
                .hardSkills(hardSkills)
                .softSkills(softSkills)
                .tools(tools)
                .build();

        return GeneratePdfRequest.builder()
                .userCV(cv)
                .jobOffer(jobOffer)
                .skillResult(skillResult)
                .build();
    }
}