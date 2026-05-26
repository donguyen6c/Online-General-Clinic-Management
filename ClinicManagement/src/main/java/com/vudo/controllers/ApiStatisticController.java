/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

/**
 *
 * @author ADMIN
 */
import com.vudo.services.StatisticService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/secure/statistics")
@PreAuthorize("hasAuthority('admin')") 
public class ApiStatisticController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping("/patients/gender")
    public Map<String, Object> getPatientsByGender() {
        return statisticService.getPatientsByGenderChart();
    }

    @GetMapping("/patients/age")
    public Map<String, Object> getPatientsByAge() {
        return statisticService.getPatientsByAgeChart();
    }

    @GetMapping("/patients/specialty")
    public Map<String, Object> getPatientsBySpecialty() {
        return statisticService.getPatientsBySpecialtyChart();
    }

    @GetMapping("/services")
    public Map<String, Object> getMedicalServicesUsed() {
        return statisticService.getMedicalServicesUsedChart();
    }

    @GetMapping("/diseases")
    public Map<String, Object> getPopularDiseases() {
        return statisticService.getPopularDiseasesChart();
    }
}
