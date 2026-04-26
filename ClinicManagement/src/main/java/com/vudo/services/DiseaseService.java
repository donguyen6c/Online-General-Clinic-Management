/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.DiseaseDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public interface DiseaseService {
    List<DiseaseDTO> getAllDiseases(Map<String, String> params);
    DiseaseDTO updateDisease(int id, DiseaseDTO dto);
    DiseaseDTO createDisease(DiseaseDTO dto);
    void deleteDisease(int id);
}
