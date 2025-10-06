package com.example.careerservice.service;

import com.example.careerservice.model.SkillItem;
import com.example.careerservice.model.SkillResult;
import com.example.careerservice.model.UserCV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CvServiceTest {

    private CvService cvService;

    @BeforeEach
    void setUp() {
        cvService = new CvService();
    }

    @Test
    void shouldPrepareCvAndSortSummariesBySkillScore() {
        UserCV userCV = UserCV.builder()
            .experience(List.of(UserCV.Experience.builder()
                .summaries(Arrays.asList(
                    UserCV.Summary.builder().technologies(List.of("Java", "Spring")).build(),
                    UserCV.Summary.builder().technologies(List.of("Excel", "Word")).build(),
                    UserCV.Summary.builder().technologies(List.of("React", "JavaScript")).build()
                )).build()
            )).build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(
                SkillItem.builder().name("Java").score(0.9).build(),
                SkillItem.builder().name("Spring").score(0.8).build(),
                SkillItem.builder().name("React").score(0.7).build()
            )).build();

        cvService.prepareCv(userCV, skillResult, 0);

        List<UserCV.Summary> summaries = userCV.getExperience().get(0).getSummaries();
        assertEquals("Java", summaries.get(0).getTechnologies().get(0));
        assertEquals("React", summaries.get(1).getTechnologies().get(0));
        assertEquals("Excel", summaries.get(2).getTechnologies().get(0));
    }

    @Test
    void shouldTrimSummariesToTopN() {
        UserCV userCV = UserCV.builder()
            .experience(List.of(UserCV.Experience.builder()
                .summaries(Arrays.asList(
                    UserCV.Summary.builder().technologies(List.of("Java")).build(),
                    UserCV.Summary.builder().technologies(List.of("Spring")).build(),
                    UserCV.Summary.builder().technologies(List.of("React")).build()
                )).build()
            )).build();

        SkillResult skillResult = SkillResult.builder().build();

        cvService.prepareCv(userCV, skillResult, 2);

        assertEquals(2, userCV.getExperience().get(0).getSummaries().size());
    }

    @Test
    void shouldNotTrimSummariesWhenTopNIsMinusOne() {
        UserCV userCV = UserCV.builder()
            .experience(List.of(UserCV.Experience.builder()
                .summaries(Arrays.asList(
                    UserCV.Summary.builder().technologies(List.of("Java")).build(),
                    UserCV.Summary.builder().technologies(List.of("Spring")).build()
                )).build()
            )).build();

        SkillResult skillResult = SkillResult.builder().build();

        cvService.prepareCv(userCV, skillResult, -1);

        assertEquals(2, userCV.getExperience().get(0).getSummaries().size());
    }

    @Test
    void shouldHandleNullExperienceAndProjectsGracefully() {
        UserCV userCV = UserCV.builder().build();
        SkillResult skillResult = SkillResult.builder().build();

        cvService.prepareCv(userCV, skillResult, 0);

        assertNull(userCV.getExperience());
        assertNull(userCV.getProjects());
    }

    @Test
    void shouldSortSummariesWithMatchingTechnologiesFirst() {
        UserCV userCV = UserCV.builder()
            .experience(List.of(UserCV.Experience.builder()
                .summaries(Arrays.asList(
                    UserCV.Summary.builder().technologies(List.of("Java")).build(),
                    UserCV.Summary.builder().technologies(List.of("Excel")).build()
                )).build()
            )).build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(SkillItem.builder().name("Java").score(1.0).build()))
            .build();

        cvService.prepareCv(userCV, skillResult, 0);

        assertEquals("Java", userCV.getExperience().get(0).getSummaries().get(0).getTechnologies().get(0));
    }

    @Test
    void shouldHandleSummariesWithNullOrEmptyTechnologies() {
        UserCV userCV = UserCV.builder()
            .experience(List.of(UserCV.Experience.builder()
                .summaries(Arrays.asList(
                    UserCV.Summary.builder().technologies(null).build(),
                    UserCV.Summary.builder().technologies(Collections.emptyList()).build(),
                    UserCV.Summary.builder().technologies(List.of("Java")).build()
                )).build()
            )).build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(SkillItem.builder().name("Java").score(1.0).build()))
            .build();

        cvService.prepareCv(userCV, skillResult, 0);

        List<UserCV.Summary> summaries = userCV.getExperience().get(0).getSummaries();
        assertNull(summaries.get(1).getTechnologies());
        assertEquals("Java", summaries.get(0).getTechnologies().get(0));
    }

    @Test
    void shouldAddMissingSkillsFromScoreMap() {
        UserCV userCV = UserCV.builder()
            .skills(new ArrayList<>(List.of("Java")))
            .build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(
                SkillItem.builder().name("Java").score(0.9).build(),
                SkillItem.builder().name("Spring").score(0.8).build(),
                SkillItem.builder().name("React").score(0.7).build()
            )).build();

        cvService.prepareCv(userCV, skillResult, -1);

        assertTrue(userCV.getSkills().containsAll(List.of("Java", "Spring", "React")));
    }

    @Test
    void shouldSortSkillsByDescendingScore() {
        UserCV userCV = UserCV.builder()
            .skills(new ArrayList<>(List.of("React", "Spring", "Java")))
            .build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(
                SkillItem.builder().name("Java").score(0.9).build(),
                SkillItem.builder().name("Spring").score(0.8).build(),
                SkillItem.builder().name("React").score(0.5).build()
            )).build();

        cvService.prepareCv(userCV, skillResult, 0);

        assertEquals(List.of("Java", "Spring", "React"), userCV.getSkills());
    }

    @Test
    void shouldPreserveExistingSkillsCaseAndNotDuplicate() {
        UserCV userCV = UserCV.builder()
            .skills(new ArrayList<>(List.of("Java")))
            .build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(SkillItem.builder().name("java").score(0.9).build()))
            .build();

        cvService.prepareCv(userCV, skillResult, 0);

        assertEquals(1, userCV.getSkills().size());
        assertEquals("Java", userCV.getSkills().get(0));
    }

    @Test
    void shouldInitializeSkillsWhenNull() {
        UserCV userCV = UserCV.builder().build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(SkillItem.builder().name("Java").score(0.9).build()))
            .build();

        cvService.prepareCv(userCV, skillResult, 0);

        assertNotNull(userCV.getSkills());
        assertTrue(userCV.getSkills().contains("Java"));
    }

    @Test
    void shouldNotFailWithEmptySkillResult() {
        UserCV userCV = UserCV.builder()
            .experience(List.of(UserCV.Experience.builder()
                .summaries(new ArrayList<>(List.of(UserCV.Summary.builder().technologies(List.of("Java")).build())))
                .build()))
            .build();

        SkillResult skillResult = SkillResult.builder().build();

        cvService.prepareCv(userCV, skillResult, -1);

        assertEquals(1, userCV.getExperience().get(0).getSummaries().size());
    }

    @Test
    void shouldBeCaseInsensitiveWhenMatchingTechnologies() {
        UserCV userCV = UserCV.builder()
            .experience(List.of(UserCV.Experience.builder()
                .summaries(new ArrayList<>( List.of(UserCV.Summary.builder().technologies(List.of("JAVA")).build())))
                .build()))
            .build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(SkillItem.builder().name("java").score(1.0).build()))
            .build();

        cvService.prepareCv(userCV, skillResult, 0);

        assertEquals("JAVA", userCV.getExperience().get(0).getSummaries().get(0).getTechnologies().get(0));
    }

    @Test
    void shouldNotModifyNonTechnologyFields() {
        UserCV userCV = UserCV.builder()
            .personalInfo(UserCV.PersonalInfo.builder()
                .firstName("John")
                .lastName("Doe")
                .build())
            .build();

        SkillResult skillResult = SkillResult.builder().build();

        cvService.prepareCv(userCV, skillResult, 0);

        assertEquals("John", userCV.getPersonalInfo().getFirstName());
        assertEquals("Doe", userCV.getPersonalInfo().getLastName());
    }

    @Test
    void shouldHandleUserCvWithEmptyLists() {
        UserCV userCV = UserCV.builder()
            .experience(Collections.emptyList())
            .projects(Collections.emptyList())
            .skills(Collections.emptyList())
            .build();

        SkillResult skillResult = SkillResult.builder().build();

        cvService.prepareCv(userCV, skillResult, 0);

        assertTrue(userCV.getExperience().isEmpty());
        assertTrue(userCV.getProjects().isEmpty());
        assertTrue(userCV.getSkills().isEmpty());
    }

    @Test
    void shouldHandleNullFieldsInsideSummaries() {
        UserCV userCV = UserCV.builder()
            .experience(List.of(UserCV.Experience.builder()
                .summaries(Arrays.asList(
                    UserCV.Summary.builder().technologies(null).build(),
                    UserCV.Summary.builder().technologies(List.of("Java")).build()
                )).build()
            )).build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(SkillItem.builder().name("Java").score(1.0).build()))
            .build();

        cvService.prepareCv(userCV, skillResult, 0);

        List<UserCV.Summary> summaries = userCV.getExperience().get(0).getSummaries();
        assertNull(summaries.get(1).getTechnologies());
        assertEquals("Java", summaries.get(0).getTechnologies().get(0));
    }
}