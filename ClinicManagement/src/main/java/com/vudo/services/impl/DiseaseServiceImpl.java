/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.DiseaseDTO;
import com.vudo.pojo.Disease;
import com.vudo.repositories.DiseaseRepository;
import com.vudo.services.DiseaseService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Service
public class DiseaseServiceImpl implements DiseaseService{

    @Autowired
    private DiseaseRepository diseaseRepo;

    @Override
    public List<DiseaseDTO> getAllDiseases(Map<String, String> params) {
        List<Disease> diseases = diseaseRepo.getAllDiseases(params);
    
        return diseases.stream().map(d -> {
            DiseaseDTO dto = new DiseaseDTO();
            dto.setId(d.getId());
            dto.setName(d.getName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DiseaseDTO updateDisease(int id, DiseaseDTO dto) {
        Disease disease = diseaseRepo.getDiseaseById(id);
        if (disease == null) {
            throw new RuntimeException("Không tìm thấy bệnh lý với ID: " + id);
        }
        
        disease.setName(dto.getName());
        diseaseRepo.updateDisease(disease);
        dto.setId(id);
        return dto;
    }

    @Override
    @Transactional
    public DiseaseDTO createDisease(DiseaseDTO dto) {
        Disease disease = new Disease();
        disease.setName(dto.getName());
       
        diseaseRepo.addDisease(disease);      
        dto.setId(disease.getId()); 
        
        return dto;
   }
   
    @Override
    public void deleteDisease(int id) {
        this.diseaseRepo.deleteDisease(id);
    }
}
