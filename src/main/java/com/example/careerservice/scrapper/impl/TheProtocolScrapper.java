package com.example.careerservice.scrapper.impl;

import com.example.careerservice.scrapper.JobScrapper;
import com.example.careerservice.model.JobOffer;
import com.example.careerservice.util.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

public class TheProtocolScrapper implements JobScrapper {
    @Override
    public String getSourceName() {
        return "theprotocol.it";
    }

    @Override
    public JobOffer scrape(String url) {
        try {
            UrlValidator.validate(url, getSourceName());
            Document doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.5993.90 Safari/537.36")
                .referrer("https://www.google.com")
                .timeout(10_000)
                .get();
            JobOffer jobOffer = parseDocument(doc);
            jobOffer.setUrl(url);
            return jobOffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private JobOffer parseDocument(Document doc) {
        String company = doc.select("a[data-test=anchor-company-link]").first().ownText();
        String title = doc.select("h1[data-test=text-offerTitle]").first().text();
        String description = doc.select("div[data-test=PROJECT] div[data-test=text-sectionItem]").text();

        List<String> technologies = doc.select(
            "div[data-test=section-technologies] div[data-test=chip-technology]"
        ).eachText();

        List<String> requirements = doc.select(
            "div[data-test=section-requirements] li"
        ).eachText();

        List<String> responsibilities = doc.select(
            "div[data-test=section-responsibilities] li"
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
