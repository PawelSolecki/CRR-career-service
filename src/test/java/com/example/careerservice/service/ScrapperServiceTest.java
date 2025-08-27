package com.example.careerservice.service;

import com.example.careerservice.scrapper.JobScrapper;
import com.example.careerservice.scrapper.model.JobOffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ScrapperServiceTest {
    private JobScrapper pracujScrapper;
    private JobScrapper protocolScrapper;
    private ScrapperService scrapperService;

    @BeforeEach
    void setUp() {
        pracujScrapper = mock(JobScrapper.class);
        protocolScrapper = mock(JobScrapper.class);

        when(pracujScrapper.getSourceName()).thenReturn("pracuj.pl");
        when(protocolScrapper.getSourceName()).thenReturn("theprotocol.it");

        scrapperService = new ScrapperService(List.of(pracujScrapper, protocolScrapper));
    }

    @Test
    void shouldUsePracujScrapper_whenUrlMatches() {
        // given
        String url = "https://www.pracuj.pl/oferta/123";
        JobOffer expectedOffer = JobOffer.builder().title("Java Dev").build();
        when(pracujScrapper.scrape(url)).thenReturn(expectedOffer);

        // when
        JobOffer result = scrapperService.scrape(url);

        // then
        assertEquals("Java Dev", result.getTitle());
        verify(pracujScrapper).scrape(url);
        verify(protocolScrapper, never()).scrape(anyString());
    }

    @Test
    void shouldUseProtocolScrapper_whenUrlMatches() {
        // given
        String url = "https://theprotocol.it/job/456";
        JobOffer expectedOffer = JobOffer.builder().title("Backend Dev").build();
        when(protocolScrapper.scrape(url)).thenReturn(expectedOffer);

        // when
        JobOffer result = scrapperService.scrape(url);

        // then
        assertEquals("Backend Dev", result.getTitle());
        verify(protocolScrapper).scrape(url);
        verify(pracujScrapper, never()).scrape(anyString());
    }

    @Test
    void shouldThrow_whenNoScrapperMatches() {
        // given
        String url = "https://example.com/job/999";

        // when + then

        assertThrows(IllegalArgumentException.class,
            () -> scrapperService.scrape(url));
        verify(pracujScrapper, never()).scrape(anyString());
        verify(protocolScrapper, never()).scrape(anyString());
    }

    @Test
    void shouldThrow_whenMalformedUrl() {
        // given
        String badUrl = "ht!tp://pracuj.pl/job";

        // when + then
        assertThrows(IllegalArgumentException.class,
            () -> scrapperService.scrape(badUrl));

        verify(pracujScrapper, never()).scrape(anyString());
        verify(protocolScrapper, never()).scrape(anyString());
    }

}