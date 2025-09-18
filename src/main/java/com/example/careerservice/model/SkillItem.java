package com.example.careerservice.model;

import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class SkillItem {
        private String name;
        private double score;
}
