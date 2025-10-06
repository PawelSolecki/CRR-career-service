package com.example.careerservice.service;

import com.example.careerservice.model.SkillResult;
import com.example.careerservice.model.UserCV;
import com.example.careerservice.util.SkillUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CvService {

    public void prepareCv(UserCV userCV, SkillResult skillResult, int topN) {
        Map<String, Double> scoreMap = SkillUtil.buildSkillScoreMap(skillResult);

        if (userCV.getExperience() != null) {
            for (UserCV.Experience exp : userCV.getExperience()) {
                sortSummariesBySkillScore(exp.getSummaries(), scoreMap);

                if (topN > 0 && exp.getSummaries() != null && exp.getSummaries().size() > topN) {
                    exp.setSummaries(exp.getSummaries().subList(0, topN));
                }
            }
        }

        if (userCV.getProjects() != null) {
            for (UserCV.Project project : userCV.getProjects()) {
                sortSummariesBySkillScore(project.getSummaries(), scoreMap);

                if (topN > 0 && project.getSummaries() != null && project.getSummaries().size() > topN) {
                    project.setSummaries(project.getSummaries().subList(0, topN));
                }
            }
        }

        mergeSkills(userCV, scoreMap);
    }


    private void sortSummariesBySkillScore(List<UserCV.Summary> summaries, Map<String, Double> scoreMap) {

        summaries.sort((s1, s2) -> {
            double score1 = s1.getTechnologies() == null ? 0 :
                s1.getTechnologies().stream()
                    .mapToDouble(t -> scoreMap.entrySet().stream()
                        .filter(e -> e.getKey().equalsIgnoreCase(t))
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .orElse(0.0))
                    .sum();

            double score2 = s2.getTechnologies() == null ? 0 :
                s2.getTechnologies().stream()
                    .mapToDouble(t -> scoreMap.entrySet().stream()
                        .filter(e -> e.getKey().equalsIgnoreCase(t))
                        .findFirst()
                        .map(Map.Entry::getValue)
                        .orElse(0.0))
                    .sum();

            return Double.compare(score2, score1);
        });
    }

    private void mergeSkills(UserCV userCV, Map<String, Double> scoreMap) {
        if (userCV.getSkills() == null) {
            userCV.setSkills(new ArrayList<>());
        }

        Set<String> currentLower = userCV.getSkills().stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());

        scoreMap.keySet().stream()
            .filter(skill -> !currentLower.contains(skill.toLowerCase()))
            .forEach(skill -> userCV.getSkills().add(skill));


        userCV.getSkills().sort((s1, s2) -> {
            double score1 = scoreMap.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(s1))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(0.0);

            double score2 = scoreMap.entrySet().stream()
                .filter(e -> e.getKey().equalsIgnoreCase(s2))
                .findFirst()
                .map(Map.Entry::getValue)
                .orElse(0.0);

            return Double.compare(score2, score1); // malejąco
        });
    }
}
