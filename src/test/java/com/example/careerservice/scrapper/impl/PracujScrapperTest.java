package com.example.careerservice.scrapper.impl;

import com.example.careerservice.scrapper.JobScrapper;
import com.example.careerservice.scrapper.model.JobOffer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PracujScrapperTest {

    private final JobScrapper scrapper = new PracujScrapper();

    @Test
    void shouldReturnPracujPl() {
        assertEquals("pracuj.pl", scrapper.getSourceName());
    }

    @Nested
    class ScrapeIntegrationTests {
        private final static String OFFER = "https://www.pracuj.pl/praca/java-developer-warszawa,oferta,1004272427?sug=oferta_bottom_bd_3_tname_252_tgroup_D";

        @Test
        @Tag("integration")
        void shouldReturnJobOffer() {
            JobOffer offer = scrapper.scrape(OFFER);
            assertNotNull(offer);
            assertEquals(OFFER, offer.getUrl());
            assertNotNull(offer.getTitle());
            assertNotNull(offer.getCompany());
            assertNotNull(offer.getDescription());
            assertNotNull(offer.getTechnologies());
            assertNotNull(offer.getRequirements());
            assertNotNull(offer.getResponsibilities());
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