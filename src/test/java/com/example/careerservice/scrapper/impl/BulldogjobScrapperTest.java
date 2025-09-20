package com.example.careerservice.scrapper.impl;

import com.example.careerservice.scrapper.JobScrapper;
import com.example.careerservice.model.JobOffer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulldogjobScrapperTest {
    private final JobScrapper scrapper = new BulldogjobScrapper();

    @Test
    void shouldReturnBulldogjobPl() {
        assertEquals("bulldogjob.pl", scrapper.getSourceName());
    }

    @Nested
    class ScrapeIntegrationTests {
        private final static String OFFER = "https://bulldogjob.pl/companies/jobs/193171-starszy-programista-java-b2b-warszawa-bank-millennium";

        @Test
        @Tag("integration")
        void shouldReturnJobOffer() {
            JobOffer offer = scrapper.scrape(OFFER);
            assertNotNull(offer);
            assertEquals(OFFER, offer.getUrl());
            assertFalse(offer.getTitle().isEmpty());
            assertFalse(offer.getCompany().isEmpty());
            assertFalse(offer.getDescription().isEmpty());
            assertFalse(offer.getRequirements().isEmpty());
            assertFalse(offer.getResponsibilities().isEmpty());
        }

        @Test
        @Tag("integration")
        void shouldThrowExceptionForInvalidUrl() {
            assertThrows(IllegalArgumentException.class, () -> scrapper.scrape("https://www.noexample.com"));
        }

        @Test
        @Tag("integration")
        void shouldThrowRuntimeExceptionForNonexistentPage() {
            assertThrows(RuntimeException.class, () -> scrapper.scrape("https://www.pracuj.pl/praca/nonexistent-page,oferta,0000000"));
        }
    }
}