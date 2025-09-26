package com.example.careerservice.service;

import com.example.careerservice.exception.UnsupportedSourceException;
import com.example.careerservice.model.JobOffer;
import com.example.careerservice.scrapper.JobScrapper;
import com.example.careerservice.util.UrlValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.InvalidUrlException;

import java.net.MalformedURLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrapperService {

    private final List<JobScrapper> scrappers;

    public JobOffer scrape(String url) {
        JobScrapper scrapper = findScrapper(url);
        return scrapper.scrape(url);
    }

    private JobScrapper findScrapper(String url) {
        for (JobScrapper scrapper : scrappers) {
            try {
                UrlValidator.validate(url, scrapper.getSourceName());
                return scrapper;
            } catch (IllegalArgumentException ignored) {

            } catch (MalformedURLException e) {
                throw new InvalidUrlException("Invalid URL: " + url, e);
            }
        }
        throw new UnsupportedSourceException(url, scrappers.stream().map(JobScrapper::getSourceName).toList());
    }

}
