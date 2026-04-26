/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.AddPrescriptionRequestDTO;
import com.vudo.dto.AddServiceRequestDTO;
import com.vudo.dto.MedicalRecordRequestDTO;
import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.services.MedicalRecordService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/secure")
public class ApiMedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    @PostMapping("/medical-records")
    @PreAuthorize("hasAuthority('doctor')")
    public ResponseEntity<?> createMedicalRecord(
            @RequestBody MedicalRecordRequestDTO request) {

        try {
            MedicalRecordResponseDTO record = medicalRecordService.create(request);
            return new ResponseEntity<>(record, HttpStatus.CREATED);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
    
    @PostMapping("/medical-records/{medicalRecordId}/prescriptions")
    @PreAuthorize("hasAuthority('doctor')")
    public ResponseEntity<?> addPrescriptions(
            @PathVariable("medicalRecordId") Integer medicalRecordId,
            @RequestBody AddPrescriptionRequestDTO request) {
        
        try {
            medicalRecordService.addPrescriptionsToRecord(medicalRecordId, request);
            
            return ResponseEntity.ok(Map.of("success","Kê đơn thuốc thành công"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/{patientId}/medical-records")
    @PreAuthorize("hasAuthority('doctor')")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getHistory(@PathVariable("patientId") int patientId) {
        
        List<MedicalRecordResponseDTO> history = medicalRecordService.getPatientHistory(patientId);
        
        if (history == null || history.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
    
    @GetMapping("/{patientId}/medical-records/{recordId}")
    @PreAuthorize("hasAuthority('doctor')or hasAuthority('pharmacist')")
    public ResponseEntity<MedicalRecordResponseDTO> getMedicalRecordDetail(
            @PathVariable("patientId") int patientId,
            @PathVariable("recordId") int recordId) {
        
        MedicalRecordResponseDTO recordDetail = medicalRecordService.getMedicalRecordDetail(patientId, recordId);
        
        if (recordDetail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
        return new ResponseEntity<>(recordDetail, HttpStatus.OK);
    }
    
    @PostMapping("/medical-records/{medicalRecordId}/services")
    @PreAuthorize("hasAuthority('doctor') or hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<?> addServices(
            @PathVariable("medicalRecordId") Integer medicalRecordId,
            @RequestBody AddServiceRequestDTO request) {
        
        try {
            medicalRecordService.addServicesToRecord(medicalRecordId, request);
            
            return ResponseEntity.ok(Map.of("success","Đăng ký dịch vụ thành công"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
}
