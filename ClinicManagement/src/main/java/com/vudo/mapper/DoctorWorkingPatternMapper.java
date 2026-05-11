/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.mapper;

import com.vudo.dto.DoctorWorkingPatternDTO;
import com.vudo.pojo.DoctorWorkingPattern;
import java.time.LocalTime;

/**
 *
 * @author ADMIN
 */
public class DoctorWorkingPatternMapper {
    public static DoctorWorkingPatternDTO toDTO(DoctorWorkingPattern pattern) {
        if (pattern == null) {
            return null;
        }

        DoctorWorkingPatternDTO dto = new DoctorWorkingPatternDTO();

        dto.setId(pattern.getId());
        dto.setDayOfWeek(pattern.getDayOfWeek());

        if (pattern.getStartTime() != null) {
            dto.setStartTime(pattern.getStartTime().toString());
        }

        if (pattern.getEndTime() != null) {
            dto.setEndTime(pattern.getEndTime().toString());
        }

        if (pattern.getDoctorId() != null) {
            dto.setDoctor(DoctorMapper.toDTO(pattern.getDoctorId()));
        }

        return dto;
    }

    public static DoctorWorkingPattern toEntity(DoctorWorkingPatternDTO dto) {
        if (dto == null) {
            return null;
        }

        DoctorWorkingPattern pattern = new DoctorWorkingPattern();

        pattern.setId(dto.getId());
        pattern.setDayOfWeek(dto.getDayOfWeek());

        if (dto.getStartTime() != null && !dto.getStartTime().isBlank()) {
            pattern.setStartTime(LocalTime.parse(dto.getStartTime()));
        }

        if (dto.getEndTime() != null && !dto.getEndTime().isBlank()) {
            pattern.setEndTime(LocalTime.parse(dto.getEndTime()));
        }

        return pattern;
    }
}
