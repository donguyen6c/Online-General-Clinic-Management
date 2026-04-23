/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.MeetingUrlDTO;
import com.vudo.services.CallAppointmentService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/secure/appointments")
public class AppointmentController {
    @Autowired
    private CallAppointmentService callAppointmentService;

    @GetMapping("/{id}/meeting-url")
    public ResponseEntity<?> getMeetingUrl(@PathVariable("id") Integer appointmentId, Principal principal) {
        try {
            String meetingUrl = callAppointmentService.getAuthorizedMeetingUrl(appointmentId, principal.getName());
            return ResponseEntity.ok(new MeetingUrlDTO(meetingUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
