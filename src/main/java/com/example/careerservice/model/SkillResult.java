package com.example.careerservice.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SkillResult {
    private List<SkillItem> hardSkills;
    private List<SkillItem> softSkills;
    private List<SkillItem> tools;
}