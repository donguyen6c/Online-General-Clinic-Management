/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.DoctorScheduleRequestDTO;
import com.vudo.dto.DoctorWorkingPatternDTO;
import com.vudo.pojo.DoctorWorkingPattern;
import com.vudo.services.DoctorScheduleService;
import com.vudo.services.DoctorWorkingPatternService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/secure/doctor")
@PreAuthorize("hasAuthority('doctor')")
public class ApiDoctorScheduleController {

    @Autowired
    private DoctorScheduleService doctorScheduleService;
    @Autowired
    private DoctorWorkingPatternService doctorWorkingPatternService;

    @GetMapping("/schedules")
    public ResponseEntity<?> getMySchedules() {
        try {
            return ResponseEntity.ok(doctorScheduleService.getMySchedules());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/schedules")
    public ResponseEntity<?> createSchedule(@RequestBody DoctorScheduleRequestDTO request) {
        try {
            return new ResponseEntity<>(doctorScheduleService.createSchedule(request), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable("id") int id) {
        try {
            doctorScheduleService.deleteSchedule(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/working-patterns")
    public ResponseEntity<?> getMyWorkingPattern() {
        try {
            return ResponseEntity.ok(doctorWorkingPatternService.getMyWorkingPatterns());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/working-patterns")
    public ResponseEntity<?> createWorkingPattern(@RequestBody DoctorWorkingPatternDTO request) {
        try {
            doctorWorkingPatternService.addOrUpdate(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Tạo lịch làm việc thành công"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/working-patterns/{id}")
    public ResponseEntity<?> deleteWorkingPattern(@PathVariable("id") int id) {
        try {
            doctorWorkingPatternService.deleteMyWorkingPattern(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
