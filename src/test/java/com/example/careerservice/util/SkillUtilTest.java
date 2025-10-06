package com.example.careerservice.util;

import com.example.careerservice.model.SkillItem;
import com.example.careerservice.model.SkillResult;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SkillUtilTest {

    @Test
    void shouldBuildMapFromAllSkillTypes(){
        // given
        List<SkillItem> hardSkills = List.of(
            SkillItem.builder().name("Java").score(0.9).build(),
            SkillItem.builder().name("Python").score(0.5).build(),
            SkillItem.builder().name("Docker").score(0.2).build()
        );

        List<SkillItem> softSkills = List.of(
            SkillItem.builder().name("communication").score(0.999).build(),
            SkillItem.builder().name("teamwork").score(0.12).build(),
            SkillItem.builder().name("problem solving").score(0.42).build()
        );
        List<SkillItem> tools = List.of(
            SkillItem.builder().name("Git").score(0.6).build(),
            SkillItem.builder().name("Jira").score(0.6).build(),
            SkillItem.builder().name("Maven").score(0.5).build()
        );

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(hardSkills)
            .softSkills(softSkills)
            .tools(tools)
            .build();

        // when
        var result = SkillUtil.buildSkillScoreMap(skillResult);

        // then
        assertNotNull(result);
        assertEquals(9, result.size());
        assertEquals(0.9, result.get("Java"));
        assertEquals(0.5, result.get("Python"));
        assertEquals(0.999, result.get("communication"));
        assertEquals(0.12, result.get("teamwork"));
        assertEquals(0.6, result.get("Git"));
        assertEquals(0.6, result.get("Jira"));
        assertNull(result.get("docker")); // case sensitivity check
    }

    @Test
    void shouldIgnoreNullSkillLists(){
        // given
        List<SkillItem> hardSkills = List.of(
            SkillItem.builder().name("Java").score(0.9).build(),
            SkillItem.builder().name("Python").score(0.5).build()
        );

        List<SkillItem> softSkills = null;

        List<SkillItem> tools = List.of(
            SkillItem.builder().name("Git").score(0.6).build(),
            SkillItem.builder().name("Jira").score(0.6).build()
        );

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(hardSkills)
            .softSkills(softSkills)
            .tools(tools)
            .build();

        // when
        var result = SkillUtil.buildSkillScoreMap(skillResult);

        // then
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(0.9, result.get("Java"));
        assertEquals(0.5, result.get("Python"));
        assertEquals(0.6, result.get("Git"));
        assertEquals(0.6, result.get("Jira"));
        assertNull(result.get("communication")); // not present
    }

    @Test
    void shouldReturnEmptyMapWhenAllListsAreEmpty(){
        // given
        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of())
            .softSkills(List.of())
            .tools(List.of())
            .build();

        // when
        var result = SkillUtil.buildSkillScoreMap(skillResult);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldHandleExtremeScoreValues(){
        // given
        List<SkillItem> hardSkills = List.of(
            SkillItem.builder().name("Java").score(1.0).build(),
            SkillItem.builder().name("Python").score(Double.MAX_VALUE).build(),
            SkillItem.builder().name("C++").score(-0.5).build() // negative score
        );

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(hardSkills)
            .softSkills(List.of())
            .tools(List.of())
            .build();

        // when
        var result = SkillUtil.buildSkillScoreMap(skillResult);

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1.0, result.get("Java"));
        assertEquals(Double.MAX_VALUE, result.get("Python"));
        assertEquals(-0.5, result.get("C++")); // negative score preserved
    }

    @Test
    void shouldPreserveCaseSensitivityInSkillNames(){
        // given
        List<SkillItem> hardSkills = List.of(
            SkillItem.builder().name("Java").score(0.9).build(),
            SkillItem.builder().name("java").score(0.5).build(), // same name different case
            SkillItem.builder().name("JAVA").score(0.2).build()  // same name different case
        );

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(hardSkills)
            .softSkills(List.of())
            .tools(List.of())
            .build();

        // when
        var result = SkillUtil.buildSkillScoreMap(skillResult);

        // then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(0.9, result.get("Java"));
        assertEquals(0.5, result.get("java"));
        assertEquals(0.2, result.get("JAVA"));

    }

    @Test
    void shouldThrowExceptionWhenSkillResultIsNull(){
        // given
        SkillResult skillResult = null;

        // when / then
        assertThrows(NullPointerException.class, () -> {
            SkillUtil.buildSkillScoreMap(skillResult);
        });
    }

    @Test
    void shouldNotModifyInputSkillResult(){
        // given
        List<SkillItem> hardSkills = List.of(
            SkillItem.builder().name("Java").score(0.9).build(),
            SkillItem.builder().name("Python").score(0.5).build()
        );

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(hardSkills)
            .softSkills(List.of())
            .tools(List.of())
            .build();

        // make a copy of the original list
        List<SkillItem> originalHardSkills = List.copyOf(skillResult.getHardSkills());

        // when
        SkillUtil.buildSkillScoreMap(skillResult);

        // then
        assertEquals(originalHardSkills, skillResult.getHardSkills());
    }

}