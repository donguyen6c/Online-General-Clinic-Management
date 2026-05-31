/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.statistics;

import com.vudo.repositories.StatisticRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author ADMIN
 */
public abstract class RevenueStrategy {

    @Autowired
    protected StatisticRepository statisticRepository;

    public abstract String getType();

    public abstract Map<String, Object> getChartData(int year);

    protected void fillRevenueData(List<Object[]> results, List<Long> data) {
        for (Object[] row : results) {
            int period = ((Number) row[0]).intValue();
            long revenue = row[1] == null ? 0L : ((Number) row[1]).longValue();

            if (period >= 1 && period <= data.size()) {
                data.set(period - 1, revenue);
            }
        }
    }

    protected Map<String, Object> buildChartData(int year, String type, List<String> labels, List<Long> data) {
        long totalRevenue = 0L;

        for (Long value : data) {
            totalRevenue += value;
        }

        Map<String, Object> chartData = new HashMap<>();
        chartData.put("year", year);
        chartData.put("type", type);
        chartData.put("totalRevenue", totalRevenue);
        chartData.put("labels", labels);
        chartData.put("data", data);

        return chartData;
    }
}
