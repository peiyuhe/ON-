package com.example.springboot.controller;

import com.example.springboot.model.Exercise;
import com.example.springboot.model.Submission;
import com.example.springboot.service.ExerciseService;
import com.example.springboot.service.SubmissionService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExerciseController.class)
@AutoConfigureMockMvc(addFilters = false)
class ExerciseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService exerciseService;

    @MockBean
    private SubmissionService submissionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateExercise() throws Exception {
        Exercise exercise = new Exercise();
        exercise.setDescription("Test Exercise");

        mockMvc.perform(post("/exercises/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Test Exercise\", \"filePath\":\"/path/to/file\", \"dueDate\":\"2024-12-31T23:59:59\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise created successfully"));

        verify(exerciseService, times(1)).createExercise(any(Exercise.class));
    }

    @Test
    void testUpdateExercise() throws Exception {
        Long exerciseId = 1L;
        Exercise exerciseDetails = new Exercise();
        exerciseDetails.setDescription("Updated Exercise");

        mockMvc.perform(put("/exercises/update/{exerciseId}", exerciseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"description\":\"Updated Exercise\", \"filePath\":\"/path/to/updated/file\", \"dueDate\":\"2024-12-31T23:59:59\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise updated successfully"));

        verify(exerciseService, times(1)).updateExercise(eq(exerciseId), any(Exercise.class));
    }

    @Test
    void testDeleteExercise() throws Exception {
        Long exerciseId = 1L;

        mockMvc.perform(delete("/exercises/delete/{exerciseId}", exerciseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise deleted successfully"));

        verify(exerciseService, times(1)).deleteExercise(exerciseId);
    }

    @Test
    void testGetExercise() throws Exception {
        Long exerciseId = 1L;
        Exercise exercise = new Exercise();
        exercise.setExerciseId(exerciseId);
        exercise.setDescription("Test Exercise");

        when(exerciseService.getExercise(exerciseId)).thenReturn(exercise);

        mockMvc.perform(get("/exercises/{exerciseId}", exerciseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exerciseId").value(exerciseId))
                .andExpect(jsonPath("$.description").value("Test Exercise"));

        verify(exerciseService, times(1)).getExercise(exerciseId);
    }

    @Test
    void testGetExercisesByCourseId() throws Exception {
        Long courseId = 1L;
        Exercise exercise1 = new Exercise(1L);
        exercise1.setDescription("Exercise 1");
        Exercise exercise2 = new Exercise(2L);
        exercise2.setDescription("Exercise 2");

        List<Exercise> exercises = Arrays.asList(exercise1, exercise2);
        when(exerciseService.getExercisesByCourseId(courseId)).thenReturn(exercises);

        mockMvc.perform(get("/exercises/course/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Exercise 1"))
                .andExpect(jsonPath("$[1].description").value("Exercise 2"));

        verify(exerciseService, times(1)).getExercisesByCourseId(courseId);
    }

    @Test
    void testGetAllSubmissions() throws Exception {
        Long exerciseId = 1L;
        Submission submission1 = new Submission();
        submission1.setSubmissionId(1L);
        submission1.setFilePath("/path/to/submission1");
        Submission submission2 = new Submission();
        submission2.setSubmissionId(2L);
        submission2.setFilePath("/path/to/submission2");

        List<Submission> submissions = Arrays.asList(submission1, submission2);
        when(submissionService.getSubmissionByExercise(exerciseId)).thenReturn(submissions);

        mockMvc.perform(get("/exercises/{exerciseId}/submission", exerciseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].submissionId").value(1L))
                .andExpect(jsonPath("$[0].filePath").value("/path/to/submission1"))
                .andExpect(jsonPath("$[1].submissionId").value(2L))
                .andExpect(jsonPath("$[1].filePath").value("/path/to/submission2"));

        verify(submissionService, times(1)).getSubmissionByExercise(exerciseId);
    }
}