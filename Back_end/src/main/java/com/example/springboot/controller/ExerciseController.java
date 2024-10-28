package com.example.springboot.controller;

import com.example.springboot.model.Exercise;
import com.example.springboot.model.Submission;
import com.example.springboot.service.ExerciseService;
import com.example.springboot.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/exercises")
public class ExerciseController {

    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private SubmissionService submissionService;

    @PostMapping("/create")
    public ResponseEntity<?> createExercise(@RequestBody Exercise exercise) {
        exerciseService.createExercise(exercise);
        return ResponseEntity.ok("Exercise created successfully");
    }

    @PutMapping("update/{exerciseId}")
    public ResponseEntity<?> updateExercise(@PathVariable Long exerciseId, @RequestBody Exercise exerciseDetails) {
        exerciseService.updateExercise(exerciseId, exerciseDetails);
        return ResponseEntity.ok("Exercise updated successfully");
    }

    @DeleteMapping("delete/{exerciseId}")
    public ResponseEntity<?> deleteExercise(@PathVariable Long exerciseId) {
        exerciseService.deleteExercise(exerciseId);
        return ResponseEntity.ok("Exercise deleted successfully");
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<?> getExercise(@PathVariable Long exerciseId) {
        Exercise exercise = exerciseService.getExercise(exerciseId);
        return ResponseEntity.ok(exercise);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getExercisesByCourseId(@PathVariable Long courseId) {
        List<Exercise> exercises = exerciseService.getExercisesByCourseId(courseId);
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{exerciseId}/submission")
    public ResponseEntity<?> getAllSubmissions(@PathVariable Long exerciseId) {
        List<Submission> submissions = submissionService.getSubmissionByExercise(exerciseId);
        return ResponseEntity.ok(submissions);
    }

}
