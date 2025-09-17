package com.example.careerservice.generator.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MatchedSkill {
    private String name;
    private double score;
    private boolean inCV;
    private boolean inJobOffer;
}