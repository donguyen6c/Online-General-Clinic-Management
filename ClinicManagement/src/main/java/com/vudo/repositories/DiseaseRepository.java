/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.Disease;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
public interface DiseaseRepository {
    Set<Disease> getAllById(Set<Integer> ids);
    List<Disease> getAllDiseases(Map<String, String> params);
    Disease getDiseaseById(int id);
    void updateDisease(Disease disease);
    void addDisease(Disease disease);
    void deleteDisease(int id);
}
