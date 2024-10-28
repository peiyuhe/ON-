package com.example.springboot.service;

import com.example.springboot.model.Exercise;
import com.example.springboot.model.Student;
import com.example.springboot.model.Submission;
import com.example.springboot.repository.ExerciseRepository;
import com.example.springboot.repository.StudentRepository;
import com.example.springboot.repository.SubmissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ExerciseService exerciseService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private SubmissionService submissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSubmission() {
        Exercise exercise = new Exercise();
        exercise.setExerciseId(1L);

        Student student = new Student();
        student.setStudentId(1L);

        Submission submission = new Submission();
        submission.setExercise(exercise);
        submission.setStudent(student);

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(submissionRepository.save(submission)).thenReturn(submission);

        Submission createdSubmission = submissionService.createSubmission(submission);

        assertNotNull(createdSubmission);
        assertEquals(exercise, createdSubmission.getExercise());
        assertEquals(student, createdSubmission.getStudent());
        verify(submissionRepository, times(1)).save(submission);
    }

    @Test
    void testMarkSubmission() {
        Submission submission = new Submission();
        submission.setSubmissionId(1L);
        Submission submissionDetail = new Submission();
        submissionDetail.setFeedback("Good work");
        submissionDetail.setScore(90);
        submissionDetail.setGraded(true);

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(submission));
        when(submissionRepository.save(submission)).thenReturn(submission);

        Submission markedSubmission = submissionService.mark(1L, submissionDetail);

        assertEquals("Good work", markedSubmission.getFeedback());
        assertEquals(90, markedSubmission.getScore());
        assertTrue(markedSubmission.isGraded());
        verify(submissionRepository, times(1)).save(submission);
    }

    @Test
    void testGetSubmissionByExercise() {
        Exercise exercise = new Exercise();
        exercise.setExerciseId(1L);

        Submission submission1 = new Submission();
        submission1.setExercise(exercise);

        Submission submission2 = new Submission();
        submission2.setExercise(exercise);

        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(exercise));
        when(submissionRepository.findByExercise(exercise)).thenReturn(Arrays.asList(submission1, submission2));

        List<Submission> submissions = submissionService.getSubmissionByExercise(1L);

        assertEquals(2, submissions.size());
        verify(submissionRepository, times(1)).findByExercise(exercise);
    }

    @Test
    void testGetSubmissionByStudent() {
        Student student = new Student();
        student.setStudentId(1L);

        Submission submission1 = new Submission();
        submission1.setStudent(student);

        Submission submission2 = new Submission();
        submission2.setStudent(student);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(submissionRepository.findByStudent(student)).thenReturn(Arrays.asList(submission1, submission2));

        List<Submission> submissions = submissionService.getSubmissionByStudent(1L);

        assertEquals(2, submissions.size());
        verify(submissionRepository, times(1)).findByStudent(student);
    }

    @Test
    void testUpdateSubmission() {
        Submission existingSubmission = new Submission();
        existingSubmission.setSubmissionId(1L);

        Submission submissionDetails = new Submission();
        submissionDetails.setFeedback("Updated feedback");
        submissionDetails.setFilePath("new_file_path");
        submissionDetails.setScore(95);
        submissionDetails.setGraded(true);
        submissionDetails.setGradedAt(LocalDateTime.now());

        when(submissionRepository.findById(1L)).thenReturn(Optional.of(existingSubmission));
        when(submissionRepository.save(existingSubmission)).thenReturn(existingSubmission);

        Submission updatedSubmission = submissionService.updateSubmission(1L, submissionDetails);

        assertEquals("Updated feedback", updatedSubmission.getFeedback());
        assertEquals("new_file_path", updatedSubmission.getFilePath());
        assertEquals(95, updatedSubmission.getScore());
        assertTrue(updatedSubmission.isGraded());
        verify(submissionRepository, times(1)).save(existingSubmission);
    }
}