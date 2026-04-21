/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.DiseaseDTO;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface DiseaseService {
    List<DiseaseDTO> getAllDiseases();
    DiseaseDTO updateDisease(int id, DiseaseDTO dto);
    DiseaseDTO createDisease(DiseaseDTO dto);
}
