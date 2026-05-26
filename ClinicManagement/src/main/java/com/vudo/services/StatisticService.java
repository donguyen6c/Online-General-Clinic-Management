/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

/**
 *
 * @author ADMIN
 */
import java.util.Map;

public interface StatisticService {

    Map<String, Object> getPatientsByGenderChart();

    Map<String, Object> getPatientsByAgeChart();

    Map<String, Object> getPatientsBySpecialtyChart();
    
    Map<String, Object> getMedicalServicesUsedChart();
    
    Map<String, Object> getPopularDiseasesChart();
    
    Map<String, Object> getRevenueChart(int year, String type);
}