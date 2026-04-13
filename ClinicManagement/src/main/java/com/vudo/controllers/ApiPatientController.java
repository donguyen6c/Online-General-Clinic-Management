/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.AppointmentResponseDTO;
import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.services.AppointmentService;
import com.vudo.services.MedicalRecordService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/patients")
public class ApiPatientController {
    @Autowired
    private MedicalRecordService medicalRecordService;
    
    @Autowired
    private AppointmentService appointmentService;
    
    @GetMapping("/{patientId}/medical-records")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getHistory(@PathVariable("patientId") int patientId) {
        
        List<MedicalRecordResponseDTO> history = medicalRecordService.getPatientHistory(patientId);
        
        if (history == null || history.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
    
    @GetMapping("/{patientId}/appointments")
    public ResponseEntity<List<AppointmentResponseDTO>> getAppointments(@PathVariable("patientId") int patientId) {
        
        List<AppointmentResponseDTO> appointments = appointmentService.getPatientAppointments(patientId);
        
        if (appointments == null || appointments.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        
        return new ResponseEntity<>(appointments, HttpStatus.OK);
    }
}
