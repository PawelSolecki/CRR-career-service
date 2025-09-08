package com.example.careerservice.scrapper.impl;

import com.example.careerservice.scrapper.JobScrapper;
import com.example.careerservice.scrapper.model.JobOffer;
import com.example.careerservice.util.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class BulldogjobScrapper implements JobScrapper {
    @Override
    public String getSourceName() {
        return "bulldogjob.pl";
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
        String company = doc.select("body div main div div div div div div h2").first().ownText();
        String title = doc.select("body div main div div div div div div h1").text();
        String description = doc.select("body div main div div div div div section div p").get(0).text();

        List<String> technologies = List.of();

        List<String> requirements = doc.select(
            "body div main div div div div div section div"
        ).get(2).select("ul li").eachText();

        List<String> responsibilities = doc.select(
            "body div main div div div div div section div"
        ).get(0).select("ul li").eachText();

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
