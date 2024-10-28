package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Setter
@Getter
@Data
@Entity
@NoArgsConstructor
@Table(name = "Learning_Plans")
public class LearningPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long planId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @JsonBackReference("student-learningplans")
    private Student student;

    private String planDetails;

    private LocalDate startDate;
    private LocalDate endDate;

    private boolean completionStatus;
    private boolean reminderSent;
}