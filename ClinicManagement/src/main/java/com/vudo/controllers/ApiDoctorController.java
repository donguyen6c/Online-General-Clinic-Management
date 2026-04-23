/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.AppointmentResponseDTO;
import com.vudo.dto.DoctorDTO;
import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.services.AppointmentService;
import com.vudo.services.DoctorService;
import com.vudo.services.MedicalRecordService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/secure")
public class ApiDoctorController {

    @Autowired
    private DoctorService docService;
       
    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/doctors")
    public ResponseEntity<?> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.docService.getDoctors(params), HttpStatus.OK);
    }

    @DeleteMapping("/doctors/{doctorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable(value = "doctorId") int id) {
        this.docService.deleteDoctor(id);
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable(value = "id") int id) {
        DoctorDTO d = docService.getDoctorById(id);

        return new ResponseEntity<>(d, HttpStatus.OK);
    }
    
    @GetMapping("/doctor-schedule")
    public ResponseEntity<?> getMySchedule() {
        try {
            List<AppointmentResponseDTO> schedule = appointmentService.getDoctorAppointments();
            return ResponseEntity.ok(schedule);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
   
}
