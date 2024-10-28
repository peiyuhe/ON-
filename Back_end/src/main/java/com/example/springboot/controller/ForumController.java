package com.example.springboot.controller;

import com.example.springboot.dto.PostQuestionDTO;
import com.example.springboot.model.Forum;
import com.example.springboot.service.ForumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/forums")
public class ForumController {

    @Autowired
    private ForumService forumService;

    @PostMapping("/create")
    public ResponseEntity<?> createForum(@RequestBody Forum forum) {
        forumService.createForum(forum);
        return ResponseEntity.ok("Forum post created successfully");
    }

    @PutMapping("/update/{forumId}")
    public ResponseEntity<?> updateForum(@PathVariable Long forumId, @RequestBody Forum forumDetails) {
        forumService.updateForum(forumId, forumDetails);
        return ResponseEntity.ok("Forum post updated successfully");
    }

    @DeleteMapping("/delete/{forumId}")
    public ResponseEntity<?> deleteForum(@PathVariable Long forumId) {
        forumService.deleteForum(forumId);
        return ResponseEntity.ok("Forum post deleted successfully");
    }

    @GetMapping("/{forumId}")
    public ResponseEntity<?> getForum(@PathVariable Long forumId) {
        Forum forum = forumService.getForum(forumId);
        return ResponseEntity.ok(forum);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getForumsByStudent(@PathVariable Long studentId) {
        List<Forum> forums = forumService.getForumsByStudent(studentId);
        return ResponseEntity.ok(forums);
    }
    @PostMapping("/post-question")
    public ResponseEntity<Forum> postQuestion(@RequestBody PostQuestionDTO request) throws IOException {
        Forum forum = forumService.postQuestion(request);
        return ResponseEntity.ok(forum);
    }
    @GetMapping("/get/all")
    public ResponseEntity<?> getAllForums() {
        List<Forum> forums = forumService.getAllForums();
        return ResponseEntity.ok(forums);
    }
}