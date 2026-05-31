/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vudo.dto.ClinicServiceDTO;
import com.vudo.dto.DiseaseDTO;
import com.vudo.pojo.Doctor;
import com.vudo.services.DiseaseService;
import com.vudo.services.DoctorService;
import com.vudo.services.ServiceService;

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
    
    @Autowired
    private DoctorService docService;

    @PutMapping("/diseases/{id}")
    @PreAuthorize("hasAuthority('admin')") 
    public ResponseEntity<?> updateDisease(@PathVariable("id") int id, @RequestBody DiseaseDTO requestDTO) {
        try {
            DiseaseDTO updated = diseaseService.updateDisease(id, requestDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/diseases")
    @PreAuthorize("hasAuthority('admin')") 
    public ResponseEntity<?> createDisease(@RequestBody DiseaseDTO requestDTO) {
        try {
            DiseaseDTO created = diseaseService.createDisease(requestDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/services/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> updateService(@PathVariable("id") int id, @RequestBody ClinicServiceDTO requestDTO) {
        try {
            ClinicServiceDTO updated = serviceService.updateService(id, requestDTO);
            return new ResponseEntity<>(updated, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/services")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> createService(@RequestBody ClinicServiceDTO requestDTO) {
        try {
            ClinicServiceDTO created = serviceService.createService(requestDTO);
            return new ResponseEntity<>(created, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/diseases/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> deleteDisease(@PathVariable("id") int id) {
        try {
            diseaseService.deleteDisease(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/services/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<?> deleteService(@PathVariable("id") int id) {
        try {
            serviceService.deleteService(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping(path = "/doctors")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<String> addDoctor(@ModelAttribute Doctor doctor) {
        try {
            doctor.getUserId().setRole("doctor");
            this.docService.addOrUpdateDoctorEntity(doctor);
            return new ResponseEntity<>("Thêm bác sĩ thành công!", HttpStatus.CREATED);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>("Lỗi hệ thống: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping(path = "/doctors/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<String> updateDoctor(@PathVariable("id") int id,@ModelAttribute Doctor doctor) {
        try {
            doctor.setId(id);
            this.docService.addOrUpdateDoctorEntity(doctor);
            return new ResponseEntity<>("Cập nhật thành công!", HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<>("Lỗi cập nhật: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/doctors/{id}")
    @PreAuthorize("hasAuthority('admin')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "id") int id) {
        this.docService.deleteDoctor(id);
    }
    
}
