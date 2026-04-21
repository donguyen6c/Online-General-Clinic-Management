/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.ClinicServiceDTO;
import com.vudo.dto.DiseaseDTO;
import com.vudo.services.DiseaseService;
import com.vudo.services.ServiceService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/secure/admin")
public class ApiAdminController {
    
    @Autowired
    private DiseaseService diseaseService;
    
    @Autowired
    private ServiceService serviceService;

    @PutMapping("/diseases/{id}")
    @PreAuthorize("hasAuthority('admin')") 
    public ResponseEntity<?> updateDisease(
            @PathVariable("id") int id, 
            @RequestBody DiseaseDTO requestDTO) {
        try {
            DiseaseDTO updated = diseaseService.updateDisease(id, requestDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/diseases")
    @PreAuthorize("hasAuthority('admin')") 
    public ResponseEntity<?> createDisease(@RequestBody DiseaseDTO requestDTO) {
        try {
            DiseaseDTO created = diseaseService.createDisease(requestDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/services/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> updateService(
            @PathVariable("id") int id, 
            @RequestBody ClinicServiceDTO requestDTO) {
        try {
            ClinicServiceDTO updated = serviceService.updateService(id, requestDTO);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PostMapping("/services")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createService(@RequestBody ClinicServiceDTO requestDTO) {
        try {
            ClinicServiceDTO created = serviceService.createService(requestDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
