package com.example.careerservice.generator.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UserCV {
    public enum LanguageLevel {
        A1,
        A2,
        B1,
        B2,
        C1,
        C2
    }

    public PersonalInfo personalInfo;
    public List<String> skills;
    public List<Experience> experience;
    public List<Education> education;
    public List<Language> languages;
    public List<Certification> certifications;
    public List<Project> projects;
    @Data
    public static class PersonalInfo {

        public String firstName;
        public String lastName;
        public String email;
        public String phone;
        public String role;
        public String summary;
        public String linkedIn;
        public String github;
        public String website;
        public String other;
    }

    @Data
    public static class Experience {

        public String position;
        public String company;
        public String url;
        public String location;
        public LocalDate startDate;
        public LocalDate endDate;
        public List<Summary> summaries;
    }

    @Data
    public static class Education {

        public String school;
        public String degree;
        public String fieldOfStudy;
        public LocalDate startDate;
        public LocalDate endDate;
    }
    @Data
    public static class Language {

        public String language;
        public LanguageLevel level;
    }

    @Data
    public static class Certification {

        public String name;
        public String issuer;
        public LocalDate date;
    }

    @Data
    public static class Project {
        public String name;
        public String url;
        public List<Summary> summaries;
    }

    @Data
    public static class Summary {
        public String text;
        public List<String> technologies;
    }
}
