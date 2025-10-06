package com.example.careerservice.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrepareCvRequest {

    private UserCV userCV;
    private SkillResult skillResult;
}
