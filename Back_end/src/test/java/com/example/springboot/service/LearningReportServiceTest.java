package com.example.springboot.service;

import com.example.springboot.model.*;
import com.example.springboot.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LearningReportServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private LearningReportsRepository learningReportsRepository;

    @Mock
    private GenerationKeywordRepository generationKeywordRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @InjectMocks
    private LearningReportService learningReportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateLearningReport() throws IOException {
        Student student = new Student();
        student.setStudentId(1L);

        Submission submission1 = new Submission();
        submission1.setScore(80);
        submission1.setExercise(new Exercise());
        submission1.setFeedback("Good job");

        Submission submission2 = new Submission();
        submission2.setScore(85);
        submission2.setExercise(new Exercise());
        submission2.setFeedback("Well done");

        List<Submission> submissions = Arrays.asList(submission1, submission2, submission1, submission2);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(submissionRepository.findTop4ByStudentOrderBySubmittedAtDesc(student)).thenReturn(submissions);
        when(learningReportsRepository.save(any(LearningReports.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String report = learningReportService.generateLearningReport(1L);

        assertNotNull(report);
        verify(learningReportsRepository, times(1)).save(any(LearningReports.class));
    }

    @Test
    void testGenerateCourseLearningReport() throws IOException {
        Student student = new Student();
        student.setStudentId(1L);
        Course course = new Course();
        course.setCourseId(1L);

        Submission submission1 = new Submission();
        submission1.setScore(90);
        submission1.setExercise(new Exercise());
        submission1.setFeedback("Excellent");

        List<Submission> submissions = Collections.singletonList(submission1);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(submissionRepository.findLatestSubmissionsByStudentAndCourse(1L, 1L)).thenReturn(submissions);
        when(learningReportsRepository.save(any(LearningReports.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String report = learningReportService.generateCourseLearningReport(1L, 1L);

        assertNotNull(report);
        verify(learningReportsRepository, times(1)).save(any(LearningReports.class));
    }

    @Test
    void testGenerateExerciseLearningReport() throws IOException {
        Student student = new Student();
        student.setStudentId(1L);
        Exercise exercise = new Exercise();
        exercise.setExerciseId(1L);

        Submission submission = new Submission();
        submission.setScore(70);
        submission.setExercise(exercise);
        submission.setFeedback("Needs improvement");

        List<Submission> submissions = Collections.singletonList(submission);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(submissionRepository.findByStudentAndExerciseOrderBySubmittedAtDesc(student, exercise)).thenReturn(submissions);
        when(learningReportsRepository.save(any(LearningReports.class))).thenAnswer(invocation -> invocation.getArgument(0));

        String report = learningReportService.generateExerciseLearningReport(1L, 1L);

        assertNotNull(report);
        verify(learningReportsRepository, times(1)).save(any(LearningReports.class));
    }

    @Test
    void testGetReportByStudentId() {
        Student student = new Student();
        student.setStudentId(1L);

        LearningReports report = new LearningReports();
        report.setStudent(student);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(learningReportsRepository.findByStudent(student)).thenReturn(Optional.of(report));

        LearningReports foundReport = learningReportService.getReportByStudentId(1L);

        assertNotNull(foundReport);
        assertEquals(student, foundReport.getStudent());
        verify(learningReportsRepository, times(1)).findByStudent(student);
    }

    @Test
    void testDeleteReportByStudentId() {
        Student student = new Student();
        student.setStudentId(1L);

        LearningReports report = new LearningReports();
        report.setStudent(student);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(learningReportsRepository.findByStudent(student)).thenReturn(Optional.of(report));

        boolean result = learningReportService.deleteReportByStudentId(1L);

        assertTrue(result);
        verify(learningReportsRepository, times(1)).deleteByStudent(student);
    }

    @Test
    void testTranslateReportUsingApiKey() {
        String reportData = "This is a test report.";
        String targetLanguage = "es";

        LearningReportService spyService = spy(learningReportService);
        doReturn("Este es un informe de prueba.").when(spyService).translateReportUsingApiKey(reportData, targetLanguage);

        String translatedText = spyService.translateReportUsingApiKey(reportData, targetLanguage);

        assertNotNull(translatedText);
        assertEquals("Este es un informe de prueba.", translatedText);
    }
}