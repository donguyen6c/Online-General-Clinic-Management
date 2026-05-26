/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.services.StatisticService;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ASUS
 */
@Controller
@RequestMapping("/admin/statistics")
@PreAuthorize("hasAuthority('admin')")
public class StatsController {

    @Autowired
    private StatisticService statisticService;

    @GetMapping
    public String statistics(Model model, @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "type", defaultValue = "month") String type) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }

        model.addAttribute("genderChart", statisticService.getPatientsByGenderChart());
        model.addAttribute("ageChart", statisticService.getPatientsByAgeChart());
        model.addAttribute("specialtyChart", statisticService.getPatientsBySpecialtyChart());
        model.addAttribute("servicesChart", statisticService.getMedicalServicesUsedChart());
        model.addAttribute("diseasesChart", statisticService.getPopularDiseasesChart());

        model.addAttribute("revenueChart", statisticService.getRevenueChart(year, type));

        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedType", type);

        return "statistics";
    }
}