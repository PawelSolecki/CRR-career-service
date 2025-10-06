package com.example.careerservice.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class PrepareCvRequest {

    @Valid
    @NotNull(message = "User CV is required")
    private UserCV userCV;

    @Valid
    @NotNull(message = "Skill result is required")
    private SkillResult skillResult;
}
