package com.example.springboot.controller;

import com.example.springboot.dto.LearningReportDTO;
import com.example.springboot.dto.TranslateReportRequestDTO;
import com.example.springboot.model.LearningReports;
import com.example.springboot.service.LearningReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/learning-report")
public class LearningReportController {

    @Autowired
    private LearningReportService learningReportService;

    @PostMapping("/generate-recent-report")
    public ResponseEntity<LearningReportDTO> generateRecentLearningReport(@RequestParam Long studentId) throws IOException {
        LearningReportDTO reportDTO = learningReportService.generateLearningReport(studentId);
        return ResponseEntity.ok(reportDTO);
    }

    @PostMapping("/generate-by-course")
    public ResponseEntity<LearningReportDTO> generateReportByCourse(@RequestBody Map<String, Long> requestBody) throws IOException {
        Long studentId = requestBody.get("studentId");
        Long courseId = requestBody.get("courseId");

        LearningReportDTO reportDTO = learningReportService.generateCourseLearningReport(studentId, courseId);
        return ResponseEntity.ok(reportDTO);
    }

    @PostMapping("/generate-by-exercise")
    public ResponseEntity<LearningReportDTO> generateReportByExercise(@RequestBody Map<String, Long> requestBody) throws IOException {
        Long studentId = requestBody.get("studentId");
        Long exerciseId = requestBody.get("exerciseId");

        LearningReportDTO reportDTO = learningReportService.generateExerciseLearningReport(studentId, exerciseId);
        return ResponseEntity.ok(reportDTO);
    }


    @GetMapping("/get/{studentId}")
    public ResponseEntity<List<LearningReports>> viewReport(@PathVariable Long studentId) {
        List<LearningReports> reports = learningReportService.getReportsByStudentId(studentId);
        if (reports != null && !reports.isEmpty()) {
            return ResponseEntity.ok(reports);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-report/{studentId}")
    public ResponseEntity<Void> deleteReportByStudentId(@PathVariable Long studentId) {
        learningReportService.deleteReportByStudentId(studentId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/translate-report")
    public ResponseEntity<String> translateReport(@RequestBody TranslateReportRequestDTO translateReportRequestDTO) {
        Long reportId = translateReportRequestDTO.getReportId();
        String targetLanguage = translateReportRequestDTO.getTargetLanguage();

        LearningReports report = learningReportService.getReportById(reportId);
        if (report == null) {
            return ResponseEntity.notFound().build();
        }

        String reportData = report.getReportData();
        String translatedText = learningReportService.translateReportUsingApiKey(reportData, targetLanguage);

        return ResponseEntity.ok(translatedText);
    }
}
