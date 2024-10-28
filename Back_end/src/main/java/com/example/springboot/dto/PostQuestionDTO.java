package com.example.springboot.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostQuestionDTO {
    // Getters and setters
    private Long courseId;
    private Long studentId;
    private String question;

}