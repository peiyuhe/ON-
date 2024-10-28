package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "Courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    @JsonBackReference("teacher-courses")
    private Teacher teacher;

    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String courseDescription;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    @JsonManagedReference("course-materials")
    private List<Material> materials;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    @JsonManagedReference("course-exercise")
    private List<Exercise> exercise;

    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JsonManagedReference("course-enrollments")
    private List<Enrollment> enrollments;


    @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
    @JsonManagedReference("course-forums")
    private List<Forum> forums;
}