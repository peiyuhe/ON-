package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@Entity
@NoArgsConstructor
@JsonIgnoreProperties({"student", "exercise"})
@Table(name = "Submission")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @JsonBackReference("student-submissions")
    private Student student;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "exercise_id", referencedColumnName = "exercise_id")
    @JsonBackReference("exercise-submissions")
    private Exercise exercise;


    private String feedback;
    private String filePath;
    private LocalDateTime submittedAt;
    private boolean graded;
    private int score;
    private LocalDateTime gradedAt;
}
