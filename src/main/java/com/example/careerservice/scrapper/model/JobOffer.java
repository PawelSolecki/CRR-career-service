package com.example.careerservice.scrapper.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class JobOffer {

    private String url;
    private String company;
    private String title;
    private String description;
    private List<String> technologies;
    private List<String> requirements;
    private List<String> responsibilities;
}
