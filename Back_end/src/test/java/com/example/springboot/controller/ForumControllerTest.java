package com.example.springboot.controller;

import com.example.springboot.dto.PostQuestionDTO;
import com.example.springboot.model.Forum;
import com.example.springboot.service.ForumService;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ForumController.class)
@AutoConfigureMockMvc(addFilters = false)
class ForumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ForumService forumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateForum() throws Exception {
        Forum forum = new Forum();
        forum.setQuestion("Test Question");

        mockMvc.perform(post("/forums/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"Test Question\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Forum post created successfully"));

        verify(forumService, times(1)).createForum(any(Forum.class));
    }

    @Test
    void testUpdateForum() throws Exception {
        Long forumId = 1L;
        Forum forumDetails = new Forum();
        forumDetails.setQuestion("Updated Question");

        mockMvc.perform(put("/forums/update/{forumId}", forumId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"Updated Question\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Forum post updated successfully"));

        verify(forumService, times(1)).updateForum(eq(forumId), any(Forum.class));
    }

    @Test
    void testDeleteForum() throws Exception {
        Long forumId = 1L;

        mockMvc.perform(delete("/forums/delete/{forumId}", forumId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Forum post deleted successfully"));

        verify(forumService, times(1)).deleteForum(forumId);
    }

    @Test
    void testGetForum() throws Exception {
        Long forumId = 1L;
        Forum forum = new Forum();
        forum.setForumId(forumId);
        forum.setQuestion("Sample Question");

        when(forumService.getForum(forumId)).thenReturn(forum);

        mockMvc.perform(get("/forums/{forumId}", forumId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forumId").value(forumId))
                .andExpect(jsonPath("$.question").value("Sample Question"));

        verify(forumService, times(1)).getForum(forumId);
    }

    @Test
    void testGetForumsByStudent() throws Exception {
        Long studentId = 1L;
        Forum forum1 = new Forum();
        forum1.setQuestion("Question 1");
        Forum forum2 = new Forum();
        forum2.setQuestion("Question 2");

        List<Forum> forums = Arrays.asList(forum1, forum2);
        when(forumService.getForumsByStudent(studentId)).thenReturn(forums);

        mockMvc.perform(get("/forums/student/{studentId}", studentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].question").value("Question 1"))
                .andExpect(jsonPath("$[1].question").value("Question 2"));

        verify(forumService, times(1)).getForumsByStudent(studentId);
    }

    @Test
    void testPostQuestion() throws Exception {
        PostQuestionDTO request = new PostQuestionDTO();
        request.setQuestion("What is the capital of France?");
        Forum forum = new Forum();
        forum.setForumId(1L);
        forum.setQuestion("What is the capital of France?");
        forum.setAiAnswer("The capital of France is Paris.");

        when(forumService.postQuestion(any(PostQuestionDTO.class))).thenReturn(forum);

        mockMvc.perform(post("/forums/post-question")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"question\":\"What is the capital of France?\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.forumId").value(1L))
                .andExpect(jsonPath("$.question").value("What is the capital of France?"))
                .andExpect(jsonPath("$.aiAnswer").value("The capital of France is Paris."));

        verify(forumService, times(1)).postQuestion(any(PostQuestionDTO.class));
    }
}