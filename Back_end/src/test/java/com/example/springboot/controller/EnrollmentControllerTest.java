package com.example.springboot.controller;

import com.example.springboot.dto.EnrollmentDTO;
import com.example.springboot.model.Enrollment;
import com.example.springboot.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
@AutoConfigureMockMvc(addFilters = false) // 禁用 CSRF 保护
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnrollmentService enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEnrollment() throws Exception {
        Enrollment enrollment = new Enrollment();
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(1L, 1L, 1L);

        when(enrollmentService.createEnrollment(any(Enrollment.class))).thenReturn(enrollmentDTO);

        mockMvc.perform(post("/enrollments/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"course\":{\"courseId\":1}, \"student\":{\"studentId\":1}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollmentId").value(1))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.studentId").value(1));

        verify(enrollmentService, times(1)).createEnrollment(any(Enrollment.class));
    }

    @Test
    void testUpdateEnrollment() throws Exception {
        Long enrollmentId = 1L;
        Enrollment enrollmentDetails = new Enrollment();
        EnrollmentDTO updatedEnrollmentDTO = new EnrollmentDTO(enrollmentId, 1L, 2L);

        when(enrollmentService.updateEnrollment(eq(enrollmentId), any(Enrollment.class))).thenReturn(updatedEnrollmentDTO);

        mockMvc.perform(put("/enrollments/update/{enrollmentId}", enrollmentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"course\":{\"courseId\":1}, \"student\":{\"studentId\":2}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollmentId").value(enrollmentId))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.studentId").value(2));

        verify(enrollmentService, times(1)).updateEnrollment(eq(enrollmentId), any(Enrollment.class));
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        Long enrollmentId = 1L;

        mockMvc.perform(delete("/enrollments/delete/{enrollmentId}", enrollmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Enrollment deleted successfully"));

        verify(enrollmentService, times(1)).deleteEnrollment(enrollmentId);
    }

    @Test
    void testGetEnrollment() throws Exception {
        Long enrollmentId = 1L;
        EnrollmentDTO enrollmentDTO = new EnrollmentDTO(enrollmentId, 1L, 1L);

        when(enrollmentService.getEnrollment(enrollmentId)).thenReturn(enrollmentDTO);

        mockMvc.perform(get("/enrollments/{enrollmentId}", enrollmentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollmentId").value(enrollmentId))
                .andExpect(jsonPath("$.courseId").value(1))
                .andExpect(jsonPath("$.studentId").value(1));

        verify(enrollmentService, times(1)).getEnrollment(enrollmentId);
    }

    @Test
    void testGetEnrollmentsByStudent() throws Exception {
        Long studentId = 1L;
        EnrollmentDTO enrollment1 = new EnrollmentDTO(1L, 1L, studentId);
        EnrollmentDTO enrollment2 = new EnrollmentDTO(2L, 2L, studentId);

        List<EnrollmentDTO> enrollments = Arrays.asList(enrollment1, enrollment2);
        when(enrollmentService.getEnrollmentsByStudent(studentId)).thenReturn(enrollments);

        mockMvc.perform(get("/enrollments/student/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].enrollmentId").value(1))
                .andExpect(jsonPath("$[0].courseId").value(1))
                .andExpect(jsonPath("$[0].studentId").value(studentId))
                .andExpect(jsonPath("$[1].enrollmentId").value(2))
                .andExpect(jsonPath("$[1].courseId").value(2))
                .andExpect(jsonPath("$[1].studentId").value(studentId));

        verify(enrollmentService, times(1)).getEnrollmentsByStudent(studentId);
    }

    @Test
    void testGetEnrollmentsByCourse() throws Exception {
        Long courseId = 1L;
        EnrollmentDTO enrollment1 = new EnrollmentDTO(1L, courseId, 1L);
        EnrollmentDTO enrollment2 = new EnrollmentDTO(2L, courseId, 2L);

        List<EnrollmentDTO> enrollments = Arrays.asList(enrollment1, enrollment2);
        when(enrollmentService.getEnrollmentsByCourse(courseId)).thenReturn(enrollments);

        mockMvc.perform(get("/enrollments/course/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].enrollmentId").value(1))
                .andExpect(jsonPath("$[0].courseId").value(courseId))
                .andExpect(jsonPath("$[0].studentId").value(1))
                .andExpect(jsonPath("$[1].enrollmentId").value(2))
                .andExpect(jsonPath("$[1].courseId").value(courseId))
                .andExpect(jsonPath("$[1].studentId").value(2));

        verify(enrollmentService, times(1)).getEnrollmentsByCourse(courseId);
    }
}