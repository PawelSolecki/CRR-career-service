package com.example.careerservice.controller;

import com.example.careerservice.model.PrepareCvRequest;
import com.example.careerservice.model.UserCV;
import com.example.careerservice.service.CvService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cv")
@RequiredArgsConstructor
public class CvController {

    private final CvService cvService;

    @PostMapping("/prepare")
    public UserCV prepareCv(@RequestBody PrepareCvRequest request, @RequestParam(defaultValue = "-1") int topN) {
        cvService.prepareCv(request.getUserCV(), request.getSkillResult(), topN);
        return request.getUserCV();
    }
}
