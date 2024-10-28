package com.example.springboot.service;

import com.example.springboot.model.LearningPlan;
import com.example.springboot.repository.LearningPlanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LearningPlanServiceTest {

    @Mock
    private LearningPlanRepository learningPlanRepository;

    @InjectMocks
    private LearningPlanService learningPlanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLearningPlan() {
        LearningPlan learningPlan = new LearningPlan();
        learningPlan.setPlanDetails("Study Java Basics");

        when(learningPlanRepository.save(learningPlan)).thenReturn(learningPlan);

        LearningPlan createdPlan = learningPlanService.createLearningPlan(learningPlan);

        assertNotNull(createdPlan);
        assertEquals("Study Java Basics", createdPlan.getPlanDetails());
        verify(learningPlanRepository, times(1)).save(learningPlan);
    }

    @Test
    void testGetLearningPlan() {
        LearningPlan learningPlan = new LearningPlan();
        learningPlan.setPlanId(1L);
        learningPlan.setPlanDetails("Study Java Basics");

        when(learningPlanRepository.findById(1L)).thenReturn(Optional.of(learningPlan));

        LearningPlan foundPlan = learningPlanService.getLearningPlan(1L);

        assertNotNull(foundPlan);
        assertEquals(1L, foundPlan.getPlanId());
        assertEquals("Study Java Basics", foundPlan.getPlanDetails());
        verify(learningPlanRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateLearningPlan() {
        LearningPlan existingPlan = new LearningPlan();
        existingPlan.setPlanId(1L);
        existingPlan.setPlanDetails("Study Java Basics");
        existingPlan.setStartDate(LocalDate.of(2024, 1, 1));
        existingPlan.setEndDate(LocalDate.of(2024, 12, 31));
        existingPlan.setCompletionStatus(false);
        existingPlan.setReminderSent(false);

        LearningPlan updatedDetails = new LearningPlan();
        updatedDetails.setPlanDetails("Advanced Java Study");
        updatedDetails.setStartDate(LocalDate.of(2024, 2, 1));
        updatedDetails.setEndDate(LocalDate.of(2024, 11, 30));
        updatedDetails.setCompletionStatus(true);
        updatedDetails.setReminderSent(true);

        when(learningPlanRepository.findById(1L)).thenReturn(Optional.of(existingPlan));
        when(learningPlanRepository.save(existingPlan)).thenReturn(existingPlan);

        LearningPlan updatedPlan = learningPlanService.updateLearningPlan(1L, updatedDetails);

        assertNotNull(updatedPlan);
        assertEquals("Advanced Java Study", updatedPlan.getPlanDetails());
        assertEquals(LocalDate.of(2024, 2, 1), updatedPlan.getStartDate());
        assertEquals(LocalDate.of(2024, 11, 30), updatedPlan.getEndDate());
        assertTrue(updatedPlan.isCompletionStatus());
        assertTrue(updatedPlan.isReminderSent());
        verify(learningPlanRepository, times(1)).save(existingPlan);
    }

    @Test
    void testDeleteLearningPlan() {
        LearningPlan learningPlan = new LearningPlan();
        learningPlan.setPlanId(1L);

        when(learningPlanRepository.findById(1L)).thenReturn(Optional.of(learningPlan));

        learningPlanService.deleteLearningPlan(1L);

        verify(learningPlanRepository, times(1)).delete(learningPlan);
    }

    @Test
    void testGetLearningPlansByStudentId() {
        Long studentId = 1L;

        LearningPlan plan1 = new LearningPlan();
        plan1.setPlanId(1L);
        plan1.setPlanDetails("Study Java Basics");

        LearningPlan plan2 = new LearningPlan();
        plan2.setPlanId(2L);
        plan2.setPlanDetails("Study Spring Boot");

        when(learningPlanRepository.findByStudentStudentId(studentId)).thenReturn(Arrays.asList(plan1, plan2));

        List<LearningPlan> plans = learningPlanService.getLearningPlansByStudentId(studentId);

        assertEquals(2, plans.size());
        verify(learningPlanRepository, times(1)).findByStudentStudentId(studentId);
    }
}