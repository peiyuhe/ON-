package com.example.springboot.service;

import com.example.springboot.model.Course;
import com.example.springboot.model.Material;
import com.example.springboot.repository.MaterialRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Transactional
    public Material createMaterial(Material material) {return materialRepository.save(material);}

    @Transactional
    public Material getMaterial(Long materialId) {return materialRepository.findById(materialId).orElseThrow();}


    @Transactional
    public Material updateMaterial(Long materialId, Material materialDetails) {
        Material material = materialRepository.findById(materialId).orElseThrow();

        material.setMaterialType(materialDetails.getMaterialType());
        material.setFilePath(materialDetails.getFilePath());
        return materialRepository.save(material);
    }

    @Transactional
    public void deleteMaterial(Long materialId) {
        Material material = materialRepository.findById(materialId).orElseThrow();
        materialRepository.delete(material);
    }
    @Transactional
    public List<Material> getMaterialsByCourseId(Course course) {
        return materialRepository.findByCourse(course);
    }

}
