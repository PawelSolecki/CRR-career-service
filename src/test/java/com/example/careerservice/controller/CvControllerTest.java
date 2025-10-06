package com.example.careerservice.controller;

import com.example.careerservice.model.PrepareCvRequest;
import com.example.careerservice.model.SkillItem;
import com.example.careerservice.model.SkillResult;
import com.example.careerservice.model.UserCV;
import com.example.careerservice.service.CvService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CvController.class)
class CvControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CvService cvService;

    @Test
    void shouldPrepareCvSuccessfully() throws Exception {
        // Given
        UserCV userCV = UserCV.builder()
            .personalInfo(UserCV.PersonalInfo.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build())
            .build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(
                SkillItem.builder().name("Java").score(0.9).build(),
                SkillItem.builder().name("Python").score(0.8).build()
            ))
            .build();

        PrepareCvRequest request = PrepareCvRequest.builder()
            .userCV(userCV)
            .skillResult(skillResult)
            .build();

        Mockito.doNothing().when(cvService).prepareCv(any(UserCV.class), any(SkillResult.class), eq(-1));

        // When & Then
        mockMvc.perform(post("/api/v1/cv/prepare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.personalInfo.firstName").value("John"))
            .andExpect(jsonPath("$.personalInfo.lastName").value("Doe"))
            .andExpect(jsonPath("$.personalInfo.email").value("john.doe@example.com"));
    }

    @Test
    void shouldReturnBadRequest_whenRequestBodyIsInvalid() throws Exception {
        // Given
        String invalidRequest = "{}";

        // When & Then
        mockMvc.perform(post("/api/v1/cv/prepare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequest_whenTopNIsInvalid() throws Exception {
        // Given
        UserCV userCV = UserCV.builder()
            .personalInfo(UserCV.PersonalInfo.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build())
            .build();

        SkillResult skillResult = SkillResult.builder()
            .hardSkills(List.of(
                SkillItem.builder().name("Java").score(0.9).build(),
                SkillItem.builder().name("Python").score(0.8).build()
            ))
            .build();

        PrepareCvRequest request = PrepareCvRequest.builder()
            .userCV(userCV)
            .skillResult(skillResult)
            .build();

        // When & Then
        mockMvc.perform(post("/api/v1/cv/prepare")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .param("topN", "invalid"))
            .andExpect(status().isBadRequest());
    }
}