package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Setter
@Getter
@Data
@Entity
@NoArgsConstructor
@Table(name = "Enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enrollmentId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonBackReference("course-enrollments")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    @JsonBackReference("student-enrollments")
    private Student student;
}