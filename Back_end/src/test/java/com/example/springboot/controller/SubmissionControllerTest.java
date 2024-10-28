package com.example.springboot.controller;

import com.example.springboot.model.Submission;
import com.example.springboot.repository.SubmissionRepository;
import com.example.springboot.service.SubmissionService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SubmissionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SubmissionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SubmissionRepository submissionRepository;

    @Autowired
    private ObjectMapper objectMapper;



    @MockBean
    private SubmissionService submissionService;

    private Submission submission;

    @BeforeEach
    public void setUp() {
        submission = new Submission();
        submission.setSubmissionId(1L);
        submission.setFeedback("Good work");
        submission.setFilePath("path/to/file");
        objectMapper = new ObjectMapper();    }

    @Test
    public void testCreateSubmission() throws Exception {

        Submission submission = new Submission();
        submission.setFeedback("Sample feedback");


        when(submissionService.createSubmission(any(Submission.class))).thenReturn(submission);


        ObjectMapper objectMapper = null;
        String submissionJson = objectMapper.writeValueAsString(submission);


        mockMvc.perform(post("/submission/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(submissionJson))
                .andExpect(status().isOk())
                .andExpect(content().string("Submission created successfully"));
    }

    @Test
    public void testUpdateSubmission() throws Exception {
        when(submissionService.updateSubmission(anyLong(), any(Submission.class))).thenReturn(submission);

        mockMvc.perform(put("/submission/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"feedback\":\"Updated feedback\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Submission updated successfully"));

        verify(submissionService, times(1)).updateSubmission(anyLong(), any(Submission.class));
    }

    @Test
    public void testGetSubmissionsByExerciseId() throws Exception {
        when(submissionService.getSubmissionsByExerciseId(anyLong())).thenReturn(List.of(submission));

        mockMvc.perform(get("/submission/exercise/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedback").value("Good work"))
                .andExpect(jsonPath("$[0].filePath").value("path/to/file"));

        verify(submissionService, times(1)).getSubmissionsByExerciseId(anyLong());
    }

    @Test
    public void testGetSubmissionsByStudentId() throws Exception {
        when(submissionService.getSubmissionsByStudentId(anyLong())).thenReturn(List.of(submission));

        mockMvc.perform(get("/submission/student/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].feedback").value("Good work"))
                .andExpect(jsonPath("$[0].filePath").value("path/to/file"));

        verify(submissionService, times(1)).getSubmissionsByStudentId(anyLong());
    }
}