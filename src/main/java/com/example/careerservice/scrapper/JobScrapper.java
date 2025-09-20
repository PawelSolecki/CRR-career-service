package com.example.careerservice.scrapper;

import com.example.careerservice.model.JobOffer;

public interface JobScrapper {
    String getSourceName();

    JobOffer scrape(String url);

}
