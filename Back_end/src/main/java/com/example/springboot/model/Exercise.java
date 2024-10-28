package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long exerciseId;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @JsonBackReference("course-exercise")
    private Course course;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.REMOVE)
    @JsonManagedReference("exercise-submissions")
    private List<Submission> submissions;

    private String description;
    private String filePath;
    private LocalDateTime dueDate;
    private LocalDateTime uploadedAt;

    public Exercise(Long exerciseId) {
        this.exerciseId = exerciseId;
    }
}
