/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class YearRevenueStrategy extends RevenueStrategy {

    @Override
    public String getType() {
        return "year";
    }

    @Override
    public Map<String, Object> getChartData(int year) {
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        int startYear = year - 4;

        for (int i = startYear; i <= year; i++) {
            List<Object[]> results = statisticRepository.revenueByPeriod(i, this.getType());
            labels.add("Năm " + i);

            if (results.isEmpty() || results.get(0)[1] == null) {
                data.add(0L);
            } else {
                data.add(((Number) results.get(0)[1]).longValue());
            }
        }

        return this.buildChartData(year, this.getType(), labels, data);
    }
}
