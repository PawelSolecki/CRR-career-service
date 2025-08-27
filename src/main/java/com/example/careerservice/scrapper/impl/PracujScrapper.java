package com.example.careerservice.scrapper.impl;

import com.example.careerservice.scrapper.JobScrapper;
import com.example.careerservice.scrapper.model.JobOffer;
import com.example.careerservice.util.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class PracujScrapper implements JobScrapper {
    @Override
    public String getSourceName() {
        return "pracuj.pl";
    }

    @Override
    public JobOffer scrape(String url) {
        try {
            UrlValidator.validate(url, getSourceName());
            Document doc = Jsoup.connect(url).get();
            JobOffer jobOffer = parseDocument(doc);
            jobOffer.setUrl(url);
            return jobOffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JobOffer parseDocument(Document doc) {
        String company = doc.select("h2[data-test=text-employerName]").first().ownText();
        String title = doc.select("h1[data-test=text-positionName]").text();
        String description = doc.select("ul[data-test=text-about-project]").text();

        List<String> technologies = doc.select(
            "div[data-test=section-technologies-expected] ul[data-test=aggregate-open-dictionary-model] li," +
                "div[data-test=section-technologies-optional] ul[data-test=aggregate-open-dictionary-model] li"
        ).eachText();

        List<String> requirements = doc.select(
            "section[data-test=section-requirements] ul[data-test=aggregate-bullet-model] li"
        ).eachText();

        List<String> responsibilities = doc.select(
            "section[data-test=section-responsibilities] ul[data-test=aggregate-bullet-model] li"
        ).eachText();

        return JobOffer.builder()
            .company(company)
            .title(title)
            .description(description)
            .technologies(technologies)
            .requirements(requirements)
            .responsibilities(responsibilities)
            .build();
    }


}
