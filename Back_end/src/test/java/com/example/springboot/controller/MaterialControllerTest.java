package com.example.springboot.controller;

import com.example.springboot.model.Course;
import com.example.springboot.model.Material;
import com.example.springboot.service.CourseService;
import com.example.springboot.service.MaterialService;
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

@WebMvcTest(MaterialController.class)
@AutoConfigureMockMvc(addFilters = false)
class MaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MaterialService materialService;

    @MockBean
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMaterial() throws Exception {
        Material material = new Material();
        material.setMaterialType("PDF");
        material.setFilePath("path/to/material.pdf");

        mockMvc.perform(post("/materials/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"materialType\":\"PDF\", \"filePath\":\"path/to/material.pdf\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Material created successfully"));

        verify(materialService, times(1)).createMaterial(any(Material.class));
    }

    @Test
    void testUpdateMaterial() throws Exception {
        Long materialId = 1L;
        Material materialDetails = new Material();
        materialDetails.setMaterialType("Video");
        materialDetails.setFilePath("path/to/material.mp4");

        mockMvc.perform(put("/materials/update/{materialId}", materialId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"materialType\":\"Video\", \"filePath\":\"path/to/material.mp4\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Material updated successfully"));

        verify(materialService, times(1)).updateMaterial(eq(materialId), any(Material.class));
    }

    @Test
    void testDeleteMaterial() throws Exception {
        Long materialId = 1L;

        mockMvc.perform(delete("/materials/delete/{materialId}", materialId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Exercise deleted successfully"));

        verify(materialService, times(1)).deleteMaterial(materialId);
    }

    @Test
    void testGetMaterial() throws Exception {
        Long materialId = 1L;
        Material material = new Material();
        material.setMaterialId(materialId);
        material.setMaterialType("PDF");
        material.setFilePath("path/to/material.pdf");

        when(materialService.getMaterial(materialId)).thenReturn(material);

        mockMvc.perform(get("/materials/{materialId}", materialId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.materialId").value(materialId))
                .andExpect(jsonPath("$.materialType").value("PDF"))
                .andExpect(jsonPath("$.filePath").value("path/to/material.pdf"));

        verify(materialService, times(1)).getMaterial(materialId);
    }

    @Test
    void testGetMaterialsByCourseId() throws Exception {
        Long courseId = 1L;
        Course course = new Course();
        course.setCourseId(courseId);

        Material material1 = new Material();
        material1.setMaterialType("PDF");
        material1.setFilePath("path/to/material1.pdf");

        Material material2 = new Material();
        material2.setMaterialType("Video");
        material2.setFilePath("path/to/material2.mp4");

        List<Material> materials = Arrays.asList(material1, material2);

        when(courseService.getCourse(courseId)).thenReturn(course);
        when(materialService.getMaterialsByCourseId(course)).thenReturn(materials);

        mockMvc.perform(get("/materials/course/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].materialType").value("PDF"))
                .andExpect(jsonPath("$[0].filePath").value("path/to/material1.pdf"))
                .andExpect(jsonPath("$[1].materialType").value("Video"))
                .andExpect(jsonPath("$[1].filePath").value("path/to/material2.mp4"));

        verify(courseService, times(1)).getCourse(courseId);
        verify(materialService, times(1)).getMaterialsByCourseId(course);
    }
}