package com.example.careerservice.util;

import com.example.careerservice.model.*;

import java.util.*;
import java.util.stream.Stream;

public class SkillUtil {

    public static Map<String, Double> buildSkillScoreMap(SkillResult skillResult) {
        Map<String, Double> map = new HashMap<>();
        Stream.of(
                skillResult.getHardSkills(),
                skillResult.getSoftSkills(),
                skillResult.getTools()
            )
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .forEach(item -> map.put(item.getName(), item.getScore()));
        return map;
    }
}