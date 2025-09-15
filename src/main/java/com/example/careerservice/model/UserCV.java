package com.example.careerservice.model;

import java.time.LocalDate;
import java.util.List;

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
    public static class PersonalInfo {

        public String firstName;
        public String lastName;
        public String email;
        public String phone;
        public String summary;
        public String linkedIn;
        public String github;
        public String website;
        public String other;
    }

    public static class Experience {

        public String position;
        public String company;
        public String url;
        public String location;
        public LocalDate startDate;
        public LocalDate endDate;
        public List<Summary> summaries;
    }

    public static class Education {

        public String school;
        public String degree;
        public String fieldOfStudy;
        public LocalDate startDate;
        public LocalDate endDate;
    }

    public static class Language {

        public String language;
        public LanguageLevel level;
    }

    public static class Certification {

        public String name;
        public String issuer;
        public LocalDate date;
    }

    public static class Project {
        public String name;
        public String url;
        public List<Summary> summaries;
    }

    public static class Summary {
        public String text;
        public List<String> technologies;
    }
}
