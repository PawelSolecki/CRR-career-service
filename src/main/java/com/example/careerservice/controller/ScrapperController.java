package com.example.careerservice.controller;

import com.example.careerservice.model.JobOffer;
import com.example.careerservice.service.ScrapperService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/scrapper")
@RequiredArgsConstructor
public class ScrapperController implements IScrapperController{

    private final ScrapperService scrapperService;

    @GetMapping()
    public JobOffer scrape(@RequestParam String url) {
        return scrapperService.scrape(url);
    }
}
