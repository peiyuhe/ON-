package com.example.springboot.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubmissionDTO {
    private Long submissionId;
    private Long studentId;
    private String filePath;
    private String feedback;
    private LocalDateTime submittedAt;
    private boolean graded;
    private int score;
    private LocalDateTime gradedAt;
}
