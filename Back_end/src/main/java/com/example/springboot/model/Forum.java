package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.time.LocalDateTime;

@Setter
@Getter
@Data
@Entity
@NoArgsConstructor
@Table(name = "Forum")
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forumId;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "student_id",referencedColumnName = "student_id")
    @JsonBackReference("student-forums")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "course_id")
    @JsonBackReference("course-forums")
    private Course course;

    @Column(name = "question", nullable = false)
    private String question;

    @Lob
    @Column(name = "ai_answer", columnDefinition = "LONGTEXT")
    private String aiAnswer;

    @Lob
    @Column(name = "teacher_answer", columnDefinition = "LONGTEXT")
    private String teacherAnswer;

    @Column(name = "post_at", nullable = true)
    private LocalDateTime postedAt = LocalDateTime.now();

    @Column(name = "answer_at", nullable = true)
    private LocalDateTime answeredAt = LocalDateTime.now();

}