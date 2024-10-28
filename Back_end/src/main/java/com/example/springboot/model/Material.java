package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "Material")
public class Material {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long materialId;


    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @JsonBackReference("course-materials")
    private Course course;

    private String materialType;
    private String filePath;
    private LocalDateTime uploadedAt;
}
