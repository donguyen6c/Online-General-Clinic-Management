/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.DiseaseDTO;
import com.vudo.services.DiseaseService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/diseases")
public class ApiDiseaseController {
    @Autowired
    private DiseaseService diseaseService;

    @GetMapping
    public ResponseEntity<List<DiseaseDTO>> getAllDiseases(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(diseaseService.getAllDiseases(params));
    }
}
