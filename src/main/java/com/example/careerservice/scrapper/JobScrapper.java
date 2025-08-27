package com.example.careerservice.scrapper;

import com.example.careerservice.scrapper.model.JobOffer;

public interface JobScrapper {

    JobOffer scrape(String url);

    String getSourceName();
}
