package com.example.careerservice.model;

import java.time.LocalDate;
import java.util.List;



public class UserCV {
    enum LanguageLevel {
        BASIC,
        INTERMEDIATE,
        ADVANCED,
        NATIVE,
    }

    enum Level {
        JUNIOR,
        MID,
        SENIOR,
        LEAD,
        PRINCIPAL,
        INTERN,
    }

    public PersonalInfo personalInfo;
    public List<String> technologies;
    public List<Experience> experience;
    public List<Education> education;
    public List<Skill> skills;
    public List<Language> languages;
    public List<Certification> certifications;
    public List<Project> projects;
    public List<Internship> interships;

    public static class Summary {

        public String text;
        public List<String> tech;
    }

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
        public Summary summary;
    }

    public static class Education {

        public String school;
        public String degree;
        public String fieldOfStudy;
        public LocalDate startDate;
        public LocalDate endDate;
    }

    public static class Skill {

        public String name;
        public Level level;
        public Double yearsOfExperience;
        public List<String> keywords;
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
        public String description;
        public String url;
        public List<String> technologies;
        public Summary summary;
    }

    public static class Internship {

        public String position;
        public String company;
        public String url;
        public String location;
        public LocalDate startDate;
        public LocalDate endDate;
        public Summary summary;
    }
}
