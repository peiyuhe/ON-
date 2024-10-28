package com.example.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LearningReportDTO {
    private Long reportId;
    private String reportData;

    public LearningReportDTO(Long reportId, String reportData) {
        this.reportId = reportId;
        this.reportData = reportData;
    }
}