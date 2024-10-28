package com.example.springboot.controller;

import com.example.springboot.dto.TranslateReportRequestDTO;
import com.example.springboot.model.LearningReports;
import com.example.springboot.service.LearningReportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LearningReportController.class)
@AutoConfigureMockMvc(addFilters = false)
class LearningReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LearningReportService learningReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateRecentLearningReport() throws Exception {
        Long studentId = 1L;
        String report = "Recent learning report data";

        when(learningReportService.generateLearningReport(studentId)).thenReturn(report);

        mockMvc.perform(post("/learning-report/generate-recent-report")
                        .param("studentId", studentId.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(report));

        verify(learningReportService, times(1)).generateLearningReport(studentId);
    }

    @Test
    void testGenerateReportByCourse() throws Exception {
        Long studentId = 1L;
        Long courseId = 1L;
        String report = "Course-based learning report data";

        when(learningReportService.generateCourseLearningReport(studentId, courseId)).thenReturn(report);

        mockMvc.perform(post("/learning-report/generate-by-course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":1, \"courseId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string(report));

        verify(learningReportService, times(1)).generateCourseLearningReport(studentId, courseId);
    }

    @Test
    void testGenerateReportByExercise() throws Exception {
        Long studentId = 1L;
        Long exerciseId = 1L;
        String report = "Exercise-based learning report data";

        when(learningReportService.generateExerciseLearningReport(studentId, exerciseId)).thenReturn(report);

        mockMvc.perform(post("/learning-report/generate-by-exercise")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"studentId\":1, \"exerciseId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().string(report));

        verify(learningReportService, times(1)).generateExerciseLearningReport(studentId, exerciseId);
    }

    @Test
    void testViewReport_Success() throws Exception {
        Long studentId = 1L;
        LearningReports report = new LearningReports();
        report.setReportId(1L);
        report.setReportData("Report data");
        report.setGeneratedAt(LocalDateTime.now());

        when(learningReportService.getReportByStudentId(studentId)).thenReturn(report);

        mockMvc.perform(get("/learning-report/view-report/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value(1))
                .andExpect(jsonPath("$.reportData").value("Report data"));

        verify(learningReportService, times(1)).getReportByStudentId(studentId);
    }

    @Test
    void testViewReport_NotFound() throws Exception {
        Long studentId = 1L;

        when(learningReportService.getReportByStudentId(studentId)).thenReturn(null);

        mockMvc.perform(get("/learning-report/view-report/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(learningReportService, times(1)).getReportByStudentId(studentId);
    }

    @Test
    void testDeleteReportByStudentId() throws Exception {
        Long studentId = 1L;

        mockMvc.perform(delete("/learning-report/delete-report/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(learningReportService, times(1)).deleteReportByStudentId(studentId);
    }

    @Test
    void testTranslateReport_Success() throws Exception {
        Long reportId = 1L;
        String targetLanguage = "fr";
        String translatedText = "Texte traduit";

        LearningReports report = new LearningReports();
        report.setReportId(reportId);
        report.setReportData("Report data");

        when(learningReportService.getReportById(reportId)).thenReturn(report);
        when(learningReportService.translateReportUsingApiKey(report.getReportData(), targetLanguage)).thenReturn(translatedText);

        mockMvc.perform(post("/learning-report/translate-report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reportId\":1, \"targetLanguage\":\"fr\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(translatedText));

        verify(learningReportService, times(1)).getReportById(reportId);
        verify(learningReportService, times(1)).translateReportUsingApiKey(report.getReportData(), targetLanguage);
    }

    @Test
    void testTranslateReport_NotFound() throws Exception {
        Long reportId = 1L;
        String targetLanguage = "fr";

        when(learningReportService.getReportById(reportId)).thenReturn(null);

        mockMvc.perform(post("/learning-report/translate-report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reportId\":1, \"targetLanguage\":\"fr\"}"))
                .andExpect(status().isNotFound());

        verify(learningReportService, times(1)).getReportById(reportId);
    }
}