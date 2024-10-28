package com.example.springboot.repository;

import com.example.springboot.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    List<Submission> findByExercise(Exercise exercise);

    List<Submission> findByStudent(Student student);

    List<Submission> findTop4ByStudentOrderBySubmittedAtDesc(Student student);

    List<Submission> findByStudentAndExerciseOrderBySubmittedAtDesc(Student student, Exercise exercise);

    @Query("SELECT s FROM Submission s WHERE s.student.studentId = :studentId AND s.exercise.course.courseId = :courseId ORDER BY s.submittedAt DESC")
    List<Submission> findLatestSubmissionsByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

}
