package com.example.springboot.controller;

import com.example.springboot.dto.EnrollmentDTO;
import com.example.springboot.model.Enrollment;
import com.example.springboot.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @PostMapping("/create")
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody Enrollment enrollment) {
        EnrollmentDTO enrollmentDTO = enrollmentService.createEnrollment(enrollment);
        return ResponseEntity.ok(enrollmentDTO);
    }

    @PutMapping("/update/{enrollmentId}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(@PathVariable Long enrollmentId, @RequestBody Enrollment enrollmentDetails) {
        EnrollmentDTO updatedEnrollment = enrollmentService.updateEnrollment(enrollmentId, enrollmentDetails);
        return ResponseEntity.ok(updatedEnrollment);
    }

    @DeleteMapping("/delete/{enrollmentId}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long enrollmentId) {
        enrollmentService.deleteEnrollment(enrollmentId);
        return ResponseEntity.ok("Enrollment deleted successfully");
    }

    @GetMapping("/{enrollmentId}")
    public ResponseEntity<EnrollmentDTO> getEnrollment(@PathVariable Long enrollmentId) {
        EnrollmentDTO enrollment = enrollmentService.getEnrollment(enrollmentId);
        return ResponseEntity.ok(enrollment);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByStudent(studentId));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<EnrollmentDTO>> getEnrollmentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentsByCourse(courseId));
    }
}