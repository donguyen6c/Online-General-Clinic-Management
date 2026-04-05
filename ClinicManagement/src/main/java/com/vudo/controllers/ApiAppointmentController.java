/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.AvailableSlotsResponseDTO;
import com.vudo.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */

@RestController
@RequestMapping("/api")
public class ApiAppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @GetMapping("/doctors/{doctorId}/available-slots")
    public ResponseEntity<AvailableSlotsResponseDTO> getAvailableSlots(
            @PathVariable("doctorId") int doctorId, @RequestParam("date") String date) {
        AvailableSlotsResponseDTO sl = appointmentService.getAvailableSlots(doctorId, date);
        return new ResponseEntity<>(sl, HttpStatus.OK);
    }
}
