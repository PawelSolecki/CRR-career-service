package com.example.careerservice.controller;

import com.example.careerservice.model.JobOffer;
import com.example.careerservice.service.ScrapperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScrapperController.class)
class ScrapperControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private ScrapperService scrapperService;

    @Test
    void shouldReturnJobOffer() throws Exception {
        // given
        JobOffer mockOffer = JobOffer.builder()
            .company("Example company")
            .title("Example title")
            .build();

        Mockito.when(scrapperService.scrape("http://test.com"))
            .thenReturn(mockOffer);

        // when & then
        mockMvc.perform(get("/api/v1/scrapper")
                .param("url", "http://test.com"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(mockOffer)));

    }

    @Test
    void shouldReturnBadRequest_whenUrlIsMissing() throws Exception {
        // when & then
        mockMvc.perform(get("/api/v1/scrapper"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void should() throws Exception {
        Mockito.when(scrapperService.scrape("invalid-url"))
            .thenThrow(new IllegalArgumentException("No scrapper found for url: invalid-url"));

        mockMvc.perform(get("/api/v1/scrapper")
                .param("url", "invalid-url"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleVeryLongUrl() throws Exception {
        String longUrl = "http://example.com/" + "a".repeat(1000);

        JobOffer mockOffer = JobOffer.builder().company("X").title("Y").build();
        Mockito.when(scrapperService.scrape(longUrl)).thenReturn(mockOffer);

        mockMvc.perform(get("/api/v1/scrapper")
                .param("url", longUrl))
            .andExpect(status().isOk());
    }

    @Test
    void shouldHandleUrlWithSpecialChars() throws Exception {
        String url = "http://example.com/job?id=123&ref=test param";

        JobOffer mockOffer = JobOffer.builder().company("Spec").title("Job").build();
        Mockito.when(scrapperService.scrape(url)).thenReturn(mockOffer);

        mockMvc.perform(get("/api/v1/scrapper")
                .param("url", url))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.company").value("Spec"));
    }

    @Test
    void shouldReturnOfferException() throws Exception {
        Mockito.when(scrapperService.scrape("http://example.com/exception"));
//            .thenThrow(new RuntimeException("Scraping error")); // <- create custom exception


        mockMvc.perform(get("/api/v1/scrapper")
                .param("url", "http://example.com/exception"))
            .andExpect(status().is(1000));
//            .andExpect(status().is5xxServerError()); //<- change when custom exception is created
    }
}