/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

/**
 *
 * @author ADMIN
 */
import com.vudo.repositories.StatisticRepository;
import com.vudo.services.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;

    @Override
    public Map<String, Object> getPatientsByGenderChart() {
        List<Object[]> results = statisticRepository.countPatientsByGender();
        return convertToChartData(results);
    }

    @Override
    public Map<String, Object> getPatientsByAgeChart() {
        List<Object[]> results = statisticRepository.countPatientsByAgeGroup();
        return convertToChartData(results);
    }

    @Override
    public Map<String, Object> getPatientsBySpecialtyChart() {
        List<Object[]> results = statisticRepository.countPatientsBySpecialty();
        return convertToChartData(results);
    }

    private Map<String, Object> convertToChartData(List<Object[]> results) {
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (Object[] row : results) {
            labels.add(String.valueOf(row[0]));

            if (row[1] == null) {
                data.add(0L);
            } else {
                data.add(((Number) row[1]).longValue());
            }
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("labels", labels);
        chartData.put("data", data);

        return chartData;
    }

    @Override
    public Map<String, Object> getMedicalServicesUsedChart() {
        List<Object[]> results = statisticRepository.countMedicalServicesUsed();
        return convertToChartData(results);
    }

    @Override
    public Map<String, Object> getPopularDiseasesChart() {
        List<Object[]> results = statisticRepository.countPopularDiseases();
        return convertToChartData(results);
    }
}
