package com.example.springboot.service;

import com.example.springboot.model.Course;
import com.example.springboot.model.Material;
import com.example.springboot.repository.MaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MaterialServiceTest {

    @Mock
    private MaterialRepository materialRepository;

    @InjectMocks
    private MaterialService materialService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateMaterial() {
        Material material = new Material();
        material.setMaterialType("PDF");
        material.setFilePath("/path/to/material.pdf");

        when(materialRepository.save(material)).thenReturn(material);

        Material createdMaterial = materialService.createMaterial(material);

        assertNotNull(createdMaterial);
        assertEquals("PDF", createdMaterial.getMaterialType());
        assertEquals("/path/to/material.pdf", createdMaterial.getFilePath());
        verify(materialRepository, times(1)).save(material);
    }

    @Test
    void testGetMaterial() {
        Material material = new Material();
        material.setMaterialId(1L);
        material.setMaterialType("Video");

        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        Material foundMaterial = materialService.getMaterial(1L);

        assertNotNull(foundMaterial);
        assertEquals(1L, foundMaterial.getMaterialId());
        assertEquals("Video", foundMaterial.getMaterialType());
        verify(materialRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateMaterial() {
        Material existingMaterial = new Material();
        existingMaterial.setMaterialId(1L);
        existingMaterial.setMaterialType("PDF");

        Material updatedDetails = new Material();
        updatedDetails.setMaterialType("Audio");
        updatedDetails.setFilePath("/path/to/audio.mp3");

        when(materialRepository.findById(1L)).thenReturn(Optional.of(existingMaterial));
        when(materialRepository.save(existingMaterial)).thenReturn(existingMaterial);

        Material updatedMaterial = materialService.updateMaterial(1L, updatedDetails);

        assertNotNull(updatedMaterial);
        assertEquals("Audio", updatedMaterial.getMaterialType());
        assertEquals("/path/to/audio.mp3", updatedMaterial.getFilePath());
        verify(materialRepository, times(1)).save(existingMaterial);
    }

    @Test
    void testDeleteMaterial() {
        Material material = new Material();
        material.setMaterialId(1L);

        when(materialRepository.findById(1L)).thenReturn(Optional.of(material));

        materialService.deleteMaterial(1L);

        verify(materialRepository, times(1)).delete(material);
    }

    @Test
    void testGetMaterialsByCourseId() {
        Course course = new Course();
        course.setCourseId(1L);

        Material material1 = new Material();
        material1.setMaterialId(1L);
        material1.setCourse(course);

        Material material2 = new Material();
        material2.setMaterialId(2L);
        material2.setCourse(course);

        when(materialRepository.findByCourse(course)).thenReturn(Arrays.asList(material1, material2));

        List<Material> materials = materialService.getMaterialsByCourseId(course);

        assertEquals(2, materials.size());
        verify(materialRepository, times(1)).findByCourse(course);
    }
}