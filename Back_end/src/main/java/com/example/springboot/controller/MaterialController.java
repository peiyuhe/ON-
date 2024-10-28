package com.example.springboot.controller;

import com.example.springboot.model.Course;
import com.example.springboot.model.Material;
import com.example.springboot.service.CourseService;
import com.example.springboot.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @Autowired
    private CourseService courseService;

    @PostMapping("/create")
    public ResponseEntity<?> createExercise(@RequestBody Material material) {
        materialService.createMaterial(material);
        return ResponseEntity.ok("Material created successfully");
    }

    @PutMapping("update/{materialId}")
    public ResponseEntity<?> updateMaterial(@PathVariable Long materialId, @RequestBody Material materialDetails) {
        materialService.updateMaterial(materialId, materialDetails);
        return ResponseEntity.ok("Material updated successfully");
    }

    @DeleteMapping("delete/{materialId}")
    public ResponseEntity<?> deleteMaterial(@PathVariable Long materialId) {
        materialService.deleteMaterial(materialId);
        return ResponseEntity.ok("Exercise deleted successfully");
    }

    @GetMapping("{materialId}")
    public ResponseEntity<?> getMaterial(@PathVariable Long materialId) {
        Material material = materialService.getMaterial(materialId);
        return ResponseEntity.ok(material);
    }
    @GetMapping("/course/{courseId}")
    public ResponseEntity<?> getMaterialsByCourseId(@PathVariable Long courseId) {
        Course course = courseService.getCourse(courseId); // Assuming CourseService has getCourse method
        List<Material> materials = materialService.getMaterialsByCourseId(course);
        return ResponseEntity.ok(materials);
    }
}
