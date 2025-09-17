package com.example.careerservice.util;

import com.example.careerservice.generator.model.GeneratePdfRequest;
import com.example.careerservice.generator.model.MatchedSkill;
import com.example.careerservice.generator.model.SkillResult;
import com.example.careerservice.scrapper.model.JobOffer;
import com.example.careerservice.generator.model.UserCV;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SkillAnalyzer {

    public static List<String> getMatchedSkillNames(GeneratePdfRequest request) {
        UserCV cv = request.getUserCV();
        JobOffer jobOffer = request.getJobOffer();
        SkillResult skillResult = request.getSkillResult();

        if (skillResult == null || skillResult.getHardSkills() == null) {
            return Collections.emptyList();
        }

        return skillResult.getHardSkills().stream()
                .map(skill -> MatchedSkill.builder()
                        .name(skill.getName())
                        .score(skill.getScore())
                        .inCV(isSkillInCV(skill.getName(), cv))
                        .inJobOffer(isSkillInJobOffer(skill.getName(), jobOffer))
                        .build())
                .filter(skill -> skill.isInCV() && skill.isInJobOffer())
                .sorted((s1, s2) -> Double.compare(s2.getScore(), s1.getScore()))
                .map(MatchedSkill::getName)
                .collect(Collectors.toList());
    }

    private static boolean isSkillInCV(String skill, UserCV cv) {
        return cv.getSkills().stream()
                .anyMatch(s -> s.equalsIgnoreCase(skill));
    }

    private static boolean isSkillInJobOffer(String skill, JobOffer jobOffer) {
        return jobOffer.getTechnologies().stream()
                .anyMatch(t -> t.equalsIgnoreCase(skill));
    }
}