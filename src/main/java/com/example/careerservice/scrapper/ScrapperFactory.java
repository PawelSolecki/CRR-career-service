package com.example.careerservice.scrapper;

import com.example.careerservice.scrapper.impl.BulldogjobScrapper;
import com.example.careerservice.scrapper.impl.PracujScrapper;
import com.example.careerservice.scrapper.impl.TheProtocolScrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ScrapperFactory {
    @Bean
    public List<JobScrapper> scrappers() {
        return List.of(
            new PracujScrapper(),
            new TheProtocolScrapper(),
            new BulldogjobScrapper()
        );
    }
}
