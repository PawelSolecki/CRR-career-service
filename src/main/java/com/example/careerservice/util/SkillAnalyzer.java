package com.example.careerservice.util;

import com.example.careerservice.model.*;

import java.util.ArrayList;
import java.util.List;

public class SkillAnalyzer {

    public static List<String> getMatchedSkillNames(GeneratePdfRequest request) {
        List<String> matchedSkills = new ArrayList<>();
        JobOffer jobOffer = request.getJobOffer();
        UserCV cv = request.getUserCV();
        SkillResult skillResult = request.getSkillResult();

        // Process hard skills
        matchedSkills.addAll(
                filterAndSortSkills(skillResult.getHardSkills(), cv, jobOffer)
        );

        // Process soft skills
        matchedSkills.addAll(
                filterAndSortSkills(skillResult.getSoftSkills(), cv, jobOffer)
        );

        // Process tools
        matchedSkills.addAll(
                filterAndSortSkills(skillResult.getTools(), cv, jobOffer)
        );

        return matchedSkills;
    }

    private static List<String> filterAndSortSkills(
            List<SkillItem> skills,
            UserCV cv,
            JobOffer jobOffer
    ) {
        if (skills == null) {
            return List.of();
        }

        return skills.stream()
                .filter(skill -> isSkillInCV(skill.getName(), cv)
                        && isSkillInJobOffer(skill.getName(), jobOffer))
                .map(SkillItem::getName)
                .toList();
    }

    private static boolean isSkillInCV(String skill, UserCV cv) {
        return cv.getSkills() != null && cv.getSkills().stream()
                .anyMatch(s -> s.equalsIgnoreCase(skill));
    }

    private static boolean isSkillInJobOffer(String skill, JobOffer jobOffer) {
        return jobOffer.getTechnologies() != null && jobOffer.getTechnologies().stream()
                .anyMatch(t -> t.equalsIgnoreCase(skill));
    }
}