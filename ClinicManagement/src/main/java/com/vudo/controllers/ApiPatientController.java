/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.AppointmentResponseDTO;
import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.dto.UserDTO;
import com.vudo.pojo.User;
import com.vudo.services.AppointmentService;
import com.vudo.services.MedicalRecordService;
import com.vudo.services.UserService;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/secure/patients")
public class ApiPatientController {
    @Autowired
    private AppointmentService appointmentService;
    
    @Autowired
    private MedicalRecordService medicalRecordService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/current-user/appointments")
    @PreAuthorize("hasAuthority('patient')")
    public ResponseEntity<List<AppointmentResponseDTO>> getCurrentUserAppointments(Principal principal, @RequestParam(value = "page", defaultValue = "1") int page) {
        int patientId = userService.getUserByUsername(principal.getName()).getId();
        List<AppointmentResponseDTO> appointments = appointmentService.getPatientAppointments(patientId, page);
        return new ResponseEntity<>(appointments == null ? new ArrayList<>() : appointments, HttpStatus.OK);
    }
     
    @GetMapping("/current-user/medical-records")
    @PreAuthorize("hasAuthority('patient')")
    public ResponseEntity<List<MedicalRecordResponseDTO>> getMedicalRecords(Principal principal) {
        String username = principal.getName();
        UserDTO currentUser = userService.getUserByUsername(username);
        List<MedicalRecordResponseDTO> history = medicalRecordService.getPatientHistory(currentUser.getId());
        if (history == null || history.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }
        return new ResponseEntity<>(history, HttpStatus.OK);
    }
    
    @GetMapping("/current-user/medical-records/{recordId}")
    @PreAuthorize("hasAuthority('patient')")
    public ResponseEntity<MedicalRecordResponseDTO> getCurrentUserMedicalRecordDetail(@PathVariable("recordId") int recordId, Principal principal) {
        
        String username = principal.getName();
        int patientId = userService.getUserByUsername(username).getId();

        MedicalRecordResponseDTO recordDetail = medicalRecordService.getMedicalRecordDetail(patientId, recordId);
        
        if (recordDetail == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
        }
        
        return new ResponseEntity<>(recordDetail, HttpStatus.OK);
    }
    
    @GetMapping("/")
    @PreAuthorize("hasAuthority('doctor')")
    public ResponseEntity<List<UserDTO>> searchPatients(@RequestParam("kw") String kw) {
        return new ResponseEntity<>(userService.searchPatients(kw), HttpStatus.OK);
    }
    
}
