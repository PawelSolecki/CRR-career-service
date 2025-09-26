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
        private final static String OFFER = "https://theprotocol.it/szczegoly/praca/gitlab-administrator-devops-warszawa,oferta,c9b20000-1c44-4a04-fc4a-08ddfb6986e3";

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
            assertThrows(RuntimeException.class, () -> scrapper.scrape("https://theprotocol.it/praca/nonexistent-page,oferta,0000000"));
        }

        @Test
        @Tag("integration")
        void shouldThrowOfferNotFoundForExpiredOffer() {
            assertThrows(com.example.careerservice.exception.OfferNotFound.class, () -> scrapper.scrape("https://theprotocol.it"));
        }
    }

}