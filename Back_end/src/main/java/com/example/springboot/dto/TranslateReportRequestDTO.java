package com.example.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TranslateReportRequestDTO {
    // Getters and setters
    private Long reportId;
    private String targetLanguage;

}