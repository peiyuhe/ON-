package com.example.springboot.repository;

import com.example.springboot.model.LearningPlan;
import com.example.springboot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningPlanRepository extends JpaRepository<LearningPlan, Long> {
    List<LearningPlan> findByStudent(Student student);
    List<LearningPlan> findByStudentStudentId(Long studentId);
}