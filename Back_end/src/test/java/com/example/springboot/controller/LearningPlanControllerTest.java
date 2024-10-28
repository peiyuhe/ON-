package com.example.springboot.controller;

import com.example.springboot.model.LearningPlan;
import com.example.springboot.service.LearningPlanService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LearningPlanController.class)
@AutoConfigureMockMvc(addFilters = false)
class LearningPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LearningPlanService learningPlanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateLearningPlan() throws Exception {
        LearningPlan learningPlan = new LearningPlan();
        learningPlan.setPlanDetails("Complete Java course");

        mockMvc.perform(post("/learningPlans/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"planDetails\":\"Complete Java course\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Learning plan created successfully"));

        verify(learningPlanService, times(1)).createLearningPlan(any(LearningPlan.class));
    }

    @Test
    void testUpdateLearningPlan() throws Exception {
        Long planId = 1L;
        LearningPlan planDetails = new LearningPlan();
        planDetails.setPlanDetails("Updated plan details");

        mockMvc.perform(put("/learningPlans/update/{planId}", planId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"planDetails\":\"Updated plan details\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Learning plan updated successfully"));

        verify(learningPlanService, times(1)).updateLearningPlan(eq(planId), any(LearningPlan.class));
    }

    @Test
    void testDeleteLearningPlan() throws Exception {
        Long planId = 1L;

        mockMvc.perform(delete("/learningPlans/delete/{planId}", planId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Learning plan deleted successfully"));

        verify(learningPlanService, times(1)).deleteLearningPlan(planId);
    }

    @Test
    void testGetLearningPlan() throws Exception {
        Long planId = 1L;
        LearningPlan learningPlan = new LearningPlan();
        learningPlan.setPlanId(planId);
        learningPlan.setPlanDetails("Sample plan details");

        when(learningPlanService.getLearningPlan(planId)).thenReturn(learningPlan);

        mockMvc.perform(get("/learningPlans/{planId}", planId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planId").value(planId))
                .andExpect(jsonPath("$.planDetails").value("Sample plan details"));

        verify(learningPlanService, times(1)).getLearningPlan(planId);
    }

    @Test
    void testGetLearningPlansByStudentId() throws Exception {
        Long studentId = 1L;
        LearningPlan plan1 = new LearningPlan();
        plan1.setPlanDetails("Plan 1 details");
        LearningPlan plan2 = new LearningPlan();
        plan2.setPlanDetails("Plan 2 details");

        List<LearningPlan> learningPlans = Arrays.asList(plan1, plan2);
        when(learningPlanService.getLearningPlansByStudentId(studentId)).thenReturn(learningPlans);

        mockMvc.perform(get("/learningPlans/student/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planDetails").value("Plan 1 details"))
                .andExpect(jsonPath("$[1].planDetails").value("Plan 2 details"));

        verify(learningPlanService, times(1)).getLearningPlansByStudentId(studentId);
    }
}