/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface StatisticRepository {
    List<Object[]> countPatientsByGender();

    List<Object[]> countPatientsByAgeGroup();

    List<Object[]> countPatientsBySpecialty();
    
    List<Object[]> countMedicalServicesUsed();
}
