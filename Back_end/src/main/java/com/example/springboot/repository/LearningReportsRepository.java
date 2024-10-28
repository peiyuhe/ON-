package com.example.springboot.repository;

import com.example.springboot.model.LearningReports;
import com.example.springboot.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningReportsRepository extends JpaRepository<LearningReports, Long> {

    Optional<LearningReports> findByStudent(Student student);

    void deleteByStudent(Student student);
    List<LearningReports> findAllByStudent(Student student);

}
