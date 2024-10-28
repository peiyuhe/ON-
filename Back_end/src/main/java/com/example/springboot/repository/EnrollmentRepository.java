package com.example.springboot.repository;

import com.example.springboot.model.Course;
import com.example.springboot.model.Enrollment;
import com.example.springboot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByStudent(Student student);
    List<Enrollment> findByCourse(Course course);
}