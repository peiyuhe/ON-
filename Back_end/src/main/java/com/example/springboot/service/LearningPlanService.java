package com.example.springboot.service;

import com.example.springboot.model.LearningPlan;
import com.example.springboot.repository.LearningPlanRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LearningPlanService {

    @Autowired
    private LearningPlanRepository learningPlanRepository;

    @Transactional
    public LearningPlan createLearningPlan(LearningPlan learningPlan) {
        return learningPlanRepository.save(learningPlan);
    }

    @Transactional
    public LearningPlan getLearningPlan(Long planId) {
        return learningPlanRepository.findById(planId).orElseThrow();
    }

    @Transactional
    public LearningPlan updateLearningPlan(Long planId, LearningPlan planDetails) {
        LearningPlan learningPlan = learningPlanRepository.findById(planId).orElseThrow();

        learningPlan.setPlanDetails(planDetails.getPlanDetails());
        learningPlan.setStartDate(planDetails.getStartDate());
        learningPlan.setEndDate(planDetails.getEndDate());
        learningPlan.setCompletionStatus(planDetails.isCompletionStatus());
        learningPlan.setReminderSent(planDetails.isReminderSent());

        return learningPlanRepository.save(learningPlan);
    }

    @Transactional
    public void deleteLearningPlan(Long planId) {
        LearningPlan learningPlan = learningPlanRepository.findById(planId).orElseThrow();
        learningPlanRepository.delete(learningPlan);
    }
    public List<LearningPlan> getLearningPlansByStudentId(Long studentId) {
        return learningPlanRepository.findByStudentStudentId(studentId);
    }
}