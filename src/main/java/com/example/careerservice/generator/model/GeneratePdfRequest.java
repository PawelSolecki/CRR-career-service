package com.example.careerservice.generator.model;

import com.example.careerservice.scrapper.model.JobOffer;
import com.example.careerservice.util.Language;
import lombok.Data;

@Data
public class GeneratePdfRequest {
    private UserCV userCV;
    private JobOffer jobOffer;
    private SkillResult skillResult;
    private String template = "simple";
    private Language language = Language.EN;
}