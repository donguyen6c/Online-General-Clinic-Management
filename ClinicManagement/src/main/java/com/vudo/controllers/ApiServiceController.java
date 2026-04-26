/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.ClinicServiceDTO;
import com.vudo.services.ServiceService;
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
@RequestMapping("/api/services")
public class ApiServiceController {
    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ClinicServiceDTO>> getAllServices(@RequestParam Map<String, String> params) {
        return ResponseEntity.ok(serviceService.getAllServices(params));
    }
}
