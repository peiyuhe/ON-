package com.example.springboot.service;

import com.example.springboot.dto.EnrollmentDTO;
import com.example.springboot.model.Course;
import com.example.springboot.model.Enrollment;
import com.example.springboot.model.Student;
import com.example.springboot.repository.EnrollmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);
        Course course = new Course();
        course.setCourseId(1L);
        Student student = new Student();
        student.setStudentId(1L);
        enrollment.setCourse(course);
        enrollment.setStudent(student);

        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        EnrollmentDTO result = enrollmentService.createEnrollment(enrollment);

        assertNotNull(result);
        assertEquals(1L, result.getEnrollmentId());
        assertEquals(1L, result.getCourseId());
        assertEquals(1L, result.getStudentId());
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testGetEnrollment_whenEnrollmentExists() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);
        Course course = new Course();
        course.setCourseId(1L);
        Student student = new Student();
        student.setStudentId(1L);
        enrollment.setCourse(course);
        enrollment.setStudent(student);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        EnrollmentDTO result = enrollmentService.getEnrollment(1L);

        assertNotNull(result);
        assertEquals(1L, result.getEnrollmentId());
        assertEquals(1L, result.getCourseId());
        assertEquals(1L, result.getStudentId());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollment_whenEnrollmentDoesNotExist() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enrollmentService.getEnrollment(1L));
    }

    @Test
    void testGetEnrollmentsByStudent() {
        Student student = new Student();
        student.setStudentId(1L);
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setEnrollmentId(1L);
        enrollment1.setStudent(student);
        Course course1 = new Course();
        course1.setCourseId(1L);
        enrollment1.setCourse(course1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setEnrollmentId(2L);
        enrollment2.setStudent(student);
        Course course2 = new Course();
        course2.setCourseId(2L);
        enrollment2.setCourse(course2);

        when(enrollmentRepository.findByStudent(student)).thenReturn(List.of(enrollment1, enrollment2));

        List<EnrollmentDTO> result = enrollmentService.getEnrollmentsByStudent(1L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getEnrollmentId());
        assertEquals(2L, result.get(1).getEnrollmentId());
        verify(enrollmentRepository, times(1)).findByStudent(student);
    }

    @Test
    void testGetEnrollmentsByCourse() {
        Course course = new Course();
        course.setCourseId(1L);
        Enrollment enrollment1 = new Enrollment();
        enrollment1.setEnrollmentId(1L);
        enrollment1.setCourse(course);
        Student student1 = new Student();
        student1.setStudentId(1L);
        enrollment1.setStudent(student1);

        Enrollment enrollment2 = new Enrollment();
        enrollment2.setEnrollmentId(2L);
        enrollment2.setCourse(course);
        Student student2 = new Student();
        student2.setStudentId(2L);
        enrollment2.setStudent(student2);

        when(enrollmentRepository.findByCourse(course)).thenReturn(List.of(enrollment1, enrollment2));

        List<EnrollmentDTO> result = enrollmentService.getEnrollmentsByCourse(1L);

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getEnrollmentId());
        assertEquals(2L, result.get(1).getEnrollmentId());
        verify(enrollmentRepository, times(1)).findByCourse(course);
    }

    @Test
    void testUpdateEnrollment_whenEnrollmentExists() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);

        Course newCourse = new Course();
        newCourse.setCourseId(2L);
        Student newStudent = new Student();
        newStudent.setStudentId(2L);
        Enrollment enrollmentDetails = new Enrollment();
        enrollmentDetails.setCourse(newCourse);
        enrollmentDetails.setStudent(newStudent);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        EnrollmentDTO result = enrollmentService.updateEnrollment(1L, enrollmentDetails);

        assertNotNull(result);
        assertEquals(2L, result.getCourseId());
        assertEquals(2L, result.getStudentId());
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testUpdateEnrollment_whenEnrollmentDoesNotExist() {
        Enrollment enrollmentDetails = new Enrollment();
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enrollmentService.updateEnrollment(1L, enrollmentDetails));
    }

    @Test
    void testDeleteEnrollment_whenEnrollmentExists() {
        Enrollment enrollment = new Enrollment();
        enrollment.setEnrollmentId(1L);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        enrollmentService.deleteEnrollment(1L);

        verify(enrollmentRepository, times(1)).delete(enrollment);
    }

    @Test
    void testDeleteEnrollment_whenEnrollmentDoesNotExist() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> enrollmentService.deleteEnrollment(1L));
    }
}