package com.example.springboot.controller;

import com.example.springboot.model.LearningPlan;
import com.example.springboot.service.LearningPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/learningPlans")
public class LearningPlanController {

    @Autowired
    private LearningPlanService learningPlanService;

    @PostMapping("/create")
    public ResponseEntity<?> createLearningPlan(@RequestBody LearningPlan learningPlan) {
        learningPlanService.createLearningPlan(learningPlan);
        return ResponseEntity.ok("Learning plan created successfully");
    }

    @PutMapping("/update/{planId}")
    public ResponseEntity<?> updateLearningPlan(@PathVariable Long planId, @RequestBody LearningPlan planDetails) {
        learningPlanService.updateLearningPlan(planId, planDetails);
        return ResponseEntity.ok("Learning plan updated successfully");
    }

    @DeleteMapping("/delete/{planId}")
    public ResponseEntity<?> deleteLearningPlan(@PathVariable Long planId) {
        learningPlanService.deleteLearningPlan(planId);
        return ResponseEntity.ok("Learning plan deleted successfully");
    }

    @GetMapping("/{planId}")
    public ResponseEntity<?> getLearningPlan(@PathVariable Long planId) {
        LearningPlan learningPlan = learningPlanService.getLearningPlan(planId);
        return ResponseEntity.ok(learningPlan);
    }
    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getLearningPlansByStudentId(@PathVariable Long studentId) {
        List<LearningPlan> learningPlans = learningPlanService.getLearningPlansByStudentId(studentId);
        return ResponseEntity.ok(learningPlans);
    }
}