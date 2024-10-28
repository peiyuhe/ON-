package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Setter
@Getter
@Data
@Entity
@NoArgsConstructor
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long studentId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_Id")
    private User user;

    private String facility;

    private String major;

    public Student(Long studentId) {
        this.studentId = studentId;
    }

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    @JsonManagedReference("student-learningplans")
    private List<LearningPlan> learningPlans;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    @JsonManagedReference("student-enrollments")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    @JsonManagedReference("student-submissions")
    private List<Submission> submissions;
}