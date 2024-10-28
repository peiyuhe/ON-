package com.example.springboot.service;

import com.example.springboot.model.Course;
import com.example.springboot.model.Enrollment;
import com.example.springboot.model.Student;
import com.example.springboot.repository.EnrollmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.springboot.dto.EnrollmentDTO;
import java.util.List;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    private EnrollmentDTO convertToDTO(Enrollment enrollment) {
        return new EnrollmentDTO(
                enrollment.getEnrollmentId(),
                enrollment.getCourse().getCourseId(),
                enrollment.getStudent().getStudentId()
        );
    }
    @Transactional
    public EnrollmentDTO createEnrollment(Enrollment enrollment) {
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(savedEnrollment);
    }

    @Transactional
    public EnrollmentDTO getEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        return new EnrollmentDTO(
                enrollment.getEnrollmentId(),
                enrollment.getCourse().getCourseId(),
                enrollment.getStudent().getStudentId());
    }

    @Transactional
    public List<EnrollmentDTO> getEnrollmentsByStudent(Long studentId) {
        Student student = new Student();
        student.setStudentId(studentId);
        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        // 转换为 EnrollmentDTO 列表
        return enrollments.stream()
                .map(enrollment -> new EnrollmentDTO(
                        enrollment.getEnrollmentId(),
                        enrollment.getCourse().getCourseId(),
                        enrollment.getStudent().getStudentId()))
                .toList();
    }
    @Transactional
    public List<EnrollmentDTO> getEnrollmentsByCourse(Long courseId) {
        Course course = new Course();
        course.setCourseId(courseId);
        List<Enrollment> enrollments = enrollmentRepository.findByCourse(course);

        // 转换为 EnrollmentDTO 列表
        return enrollments.stream()
                .map(enrollment -> new EnrollmentDTO(
                        enrollment.getEnrollmentId(),
                        enrollment.getCourse().getCourseId(),
                        enrollment.getStudent().getStudentId()))
                .toList();
    }

    @Transactional
    public EnrollmentDTO updateEnrollment(Long enrollmentId, Enrollment enrollmentDetails) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        enrollment.setCourse(enrollmentDetails.getCourse());
        enrollment.setStudent(enrollmentDetails.getStudent());
        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return convertToDTO(updatedEnrollment);
    }

    @Transactional
    public void deleteEnrollment(Long enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        enrollmentRepository.delete(enrollment);
    }
}