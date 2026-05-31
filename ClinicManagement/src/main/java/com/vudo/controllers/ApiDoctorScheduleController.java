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
            return new ResponseEntity<>(doctorScheduleService.getMySchedules(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/schedules")
    public ResponseEntity<?> createSchedule(@RequestBody DoctorScheduleRequestDTO request) {
        try {
            return new ResponseEntity<>(doctorScheduleService.createSchedule(request), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/schedules/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable("id") int id) {
        try {
            doctorScheduleService.deleteSchedule(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/working-patterns")
    public ResponseEntity<?> getMyWorkingPattern() {
        try {
            return new ResponseEntity<>(doctorWorkingPatternService.getMyWorkingPatterns(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/working-patterns")
    public ResponseEntity<?> createWorkingPattern(@RequestBody DoctorWorkingPatternDTO request) {
        try {
            doctorWorkingPatternService.addOrUpdate(request);
            return new ResponseEntity<>(Map.of("message", "Tạo lịch làm việc thành công"), HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/working-patterns/{id}")
    public ResponseEntity<?> deleteWorkingPattern(@PathVariable("id") int id) {
        try {
            doctorWorkingPatternService.deleteMyWorkingPattern(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
