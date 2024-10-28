package com.example.springboot.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "generation_keyword")
public class GenerationKeyword {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "exercise_id", referencedColumnName = "exercise_id", nullable = false)
    private Exercise exercise;

    @Column(name = "score", nullable = false)
    private int score;

    @Column(name = "feedback", nullable = false, length = 255)
    private String feedback;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

}
