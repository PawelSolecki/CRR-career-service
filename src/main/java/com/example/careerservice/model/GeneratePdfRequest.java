package com.example.careerservice.model;

import com.example.careerservice.validation.ValidLanguage;
import com.example.careerservice.validation.ValidTemplate;
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

    @NotNull(message = "Template must not be blank")
    @ValidTemplate
    private  String template;

    @NotNull(message = "Language must not be null")
    @ValidLanguage
    private Language language;
}
