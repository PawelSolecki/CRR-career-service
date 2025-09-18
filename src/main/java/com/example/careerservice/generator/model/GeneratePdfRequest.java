package com.example.careerservice.generator.model;

import com.example.careerservice.scrapper.model.JobOffer;
import com.example.careerservice.util.Language;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneratePdfRequest {

    @Valid
    @NotNull(message = "Personal info is required")
    private  UserCV userCV;

    @Valid
    @NotNull(message = "Job offer must not be null")
    private  JobOffer jobOffer;

    @Valid
    @NotNull(message = "Skill result must not be null")
    private  SkillResult skillResult;

    @NotNull(message = "Template must not be blank")
    private  String template;

    @NotNull(message = "Language must not be null")
    private  Language language;
}
