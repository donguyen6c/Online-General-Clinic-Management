/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class RevenueStrategyFactory {

    private final Map<String, RevenueStrategy> strategies = new HashMap<>();

    @Autowired
    public RevenueStrategyFactory(List<RevenueStrategy> strategies) {
        for (RevenueStrategy strategy : strategies) {
            this.strategies.put(strategy.getType().toLowerCase(), strategy);
        }
    }

    public RevenueStrategy getStrategy(String type) {
        if (type == null || type.isBlank()) {
            type = "month";
        }

        RevenueStrategy strategy = strategies.get(type.toLowerCase());

        if (strategy == null) {
            return strategies.get("month");
        }

        return strategy;
    }
}
