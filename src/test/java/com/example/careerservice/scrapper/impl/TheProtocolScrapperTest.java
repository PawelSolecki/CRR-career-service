package com.example.careerservice.scrapper.impl;

import com.example.careerservice.scrapper.JobScrapper;
import com.example.careerservice.model.JobOffer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TheProtocolScrapperTest {
    private final JobScrapper scrapper = new TheProtocolScrapper();

    @Test
    void shouldReturnTheProtocolIt() {
        assertEquals("theprotocol.it", scrapper.getSourceName());
    }

    @Nested
    class ScrapeIntegrationTests {
        private final static String OFFER = "https://theprotocol.it/szczegoly/praca/programista-java-warszawa,oferta,dbe30000-d4f2-5aeb-0e2e-08ddd4f20754?s=-4682230642&searchId=0dfd1446-6dd0-4866-88e3-4f1a1c59504e";

        @Test
        @Tag("integration")
        void shouldReturnJobOffer() {
            JobOffer offer = scrapper.scrape(OFFER);
            assertNotNull(offer);
            assertEquals(OFFER, offer.getUrl());
            assertFalse(offer.getTitle().isEmpty());
            assertFalse(offer.getCompany().isEmpty());
            assertFalse(offer.getDescription().isEmpty());
            assertFalse(offer.getTechnologies().isEmpty());
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