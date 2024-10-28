package com.example.springboot.service;

import com.example.springboot.model.*;
import com.example.springboot.repository.ExerciseRepository;
import com.example.springboot.repository.StudentRepository;
import com.example.springboot.repository.SubmissionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Service
public class SubmissionService {

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ExerciseService exerciseService;

    @Autowired
    private StudentService studentService;

    public Submission createSubmission(Submission submission) {
        Exercise exercise = exerciseRepository.findById(submission.getExercise().getExerciseId())
                .orElseThrow(() -> new RuntimeException("Exercise not found"));


        Student student = studentRepository.findById(submission.getStudent().getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));


        submission.setExercise(exercise);
        submission.setStudent(student);
        submission.setSubmittedAt(LocalDateTime.now());

        return submissionRepository.save(submission);
    }

    public Submission mark(Long submissionId, Submission submissionDetail) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setFeedback(submissionDetail.getFeedback());
        submission.setScore(submissionDetail.getScore());
        submission.setGraded(true);

        return submissionRepository.save(submission);
    }

    public List<Submission> getSubmissionByExercise(Long exerciseId) {

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new RuntimeException("Exercise not found"));

        return submissionRepository.findByExercise(exercise);
    }

    public List<Submission> getSubmissionByStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return submissionRepository.findByStudent(student);
    }
    public List<Submission> getSubmissionsByExerciseId(Long exerciseId) {
        Exercise exercise = exerciseService.getExercise(exerciseId);
        return submissionRepository.findByExercise(exercise);
    }

    public List<Submission> getSubmissionsByStudentId(Long studentId) {
        Student student = studentService.getStudent(studentId);
        return submissionRepository.findByStudent(student);
    }
    @Transactional
    public Submission updateSubmission(Long submissionId, Submission submissionDetails) {
        Submission submission = submissionRepository.findById(submissionId).orElseThrow(() -> new RuntimeException("Submission not found"));

        submission.setFeedback(submissionDetails.getFeedback());
        submission.setFilePath(submissionDetails.getFilePath());
        submission.setScore(submissionDetails.getScore());
        submission.setGraded(submissionDetails.isGraded());
        submission.setGradedAt(submissionDetails.getGradedAt());

        return submissionRepository.save(submission);
    }
}