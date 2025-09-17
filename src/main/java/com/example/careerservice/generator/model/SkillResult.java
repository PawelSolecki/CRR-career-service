package com.example.careerservice.generator.model;

import java.util.List;
import lombok.Data;

@Data
public class SkillResult {
    private List<SkillScore> hardSkills;
    private List<SkillScore> softSkills;
    private List<SkillScore> tools;

    @Data
    public static class SkillScore {
        private String name;
        private double score;
    }
}