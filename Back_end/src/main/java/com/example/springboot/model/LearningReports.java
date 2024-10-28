package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@Entity
@Table(name = "learning_reports")
public class LearningReports {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id", nullable = false)  // 删除 unique = true
    @JsonBackReference("student-learningreports")
    private Student student;

    @Lob
    @Column(name = "report_data", columnDefinition = "LONGTEXT")
    private String reportData;

    @Column(name = "generated_at", nullable = false)
    private LocalDateTime generatedAt;

}
