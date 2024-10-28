package com.example.springboot.controller;

import com.example.springboot.model.Exercise;
import com.example.springboot.model.Submission;
import com.example.springboot.repository.SubmissionRepository;
import com.example.springboot.service.ExerciseService;
import com.example.springboot.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/submission")
public class SubmissionController {

    @Autowired
    private SubmissionService submissionService;
    @Autowired
    private SubmissionRepository submissionRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createExercise(@RequestBody Submission submission) {
        submissionService.createSubmission(submission);
        return ResponseEntity.ok("Submission created successfully");
    }

    @PutMapping("/update/{submissionId}")
    public ResponseEntity<?> updateSubmission(@PathVariable Long submissionId, @RequestBody Submission submissionDetails) {
        Submission updatedSubmission = submissionService.updateSubmission(submissionId, submissionDetails);
        return ResponseEntity.ok("Submission updated successfully");
    }

    @PutMapping("/{submissionId}")
    public ResponseEntity<?> updateExercise(@PathVariable Long submissionId, @RequestBody Submission submissionDetails) {
        submissionService.mark(submissionId, submissionDetails);
        return ResponseEntity.ok("Submission updated successfully");
    }
    @GetMapping("/exercise/{exerciseId}")
    public ResponseEntity<List<Submission>> getSubmissionsByExerciseId(@PathVariable Long exerciseId) {
        List<Submission> submissions = submissionService.getSubmissionsByExerciseId(exerciseId);
        return ResponseEntity.ok(submissions);
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Submission>> getSubmissionsByStudentId(@PathVariable Long studentId) {
        List<Submission> submissions = submissionService.getSubmissionsByStudentId(studentId);
        return ResponseEntity.ok(submissions);
    }
}
