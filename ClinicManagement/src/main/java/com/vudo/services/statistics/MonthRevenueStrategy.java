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
public class MonthRevenueStrategy extends RevenueStrategy {

    @Override
    public String getType() {
        return "month";
    }

    @Override
    public Map<String, Object> getChartData(int year) {
        List<Object[]> results = statisticRepository.revenueByPeriod(year, this.getType());
        List<String> labels = new ArrayList<>();
        List<Long> data = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            labels.add("Tháng " + i + "/" + year);
            data.add(0L);
        }

        this.fillRevenueData(results, data);

        return this.buildChartData(year, this.getType(), labels, data);
    }
}
