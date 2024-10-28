package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "teacher")
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teacher_id")
    private Long teacherId;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_Id")
    private User user;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE)
    @JsonManagedReference("teacher-courses")
    private List<Course> courses;

    private String facility;
}