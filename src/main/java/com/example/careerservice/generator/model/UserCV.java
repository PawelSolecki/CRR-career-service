package com.example.careerservice.generator.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class UserCV {
    private enum LanguageLevel {
        A1,
        A2,
        B1,
        B2,
        C1,
        C2
    }

    @Valid
    @NotNull(message = "Personal info is required")
    private PersonalInfo personalInfo;
    private List<String> skills;
    private List<Experience> experience;
    private List<Education> education;
    private List<Language> languages;
    private List<Certification> certifications;
    private List<Project> projects;
    @Data
    @Builder
    public static class PersonalInfo {
        @NotBlank(message = "First name is required")
        private String firstName;
        @NotBlank(message = "Last name is required")
        private String lastName;
        @Email(message = "Email should be valid")
        private String email;
        private String phone;
        private String role;
        private String summary;
        private String linkedIn;
        private String github;
        private String website;
        private String other;
    }

    @Data
    @Builder
    public static class Experience {
        private String position;
        private String company;
        private String url;
        private String location;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<Summary> summaries;
    }

    @Data
    @Builder
    public static class Education {
        private String school;
        private String degree;
        private String fieldOfStudy;
        private LocalDate startDate;
        private LocalDate endDate;
    }
    @Data
    @Builder
    public static class Language {
        private String language;
        private LanguageLevel level;
    }

    @Data
    @Builder
    public static class Certification {
        private String name;
        private String issuer;
        private LocalDate date;
    }

    @Data
    @Builder
    public static class Project {
        private String name;
        private String url;
        private List<Summary> summaries;
    }

    @Data
    @Builder
    public static class Summary {
        private String text;
        private List<String> technologies;
    }
}
