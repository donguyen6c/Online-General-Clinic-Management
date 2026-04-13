/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.mapper;

import com.vudo.dto.AppointmentResponseDTO;
import com.vudo.pojo.Appointment;

/**
 *
 * @author ASUS
 */
public class AppointmentMapper {
    public static AppointmentResponseDTO toDTO(Appointment a) {
        if (a == null) {
            return null;
        }

        AppointmentResponseDTO dto = new AppointmentResponseDTO();
        
        dto.setId(a.getId());
        dto.setDate(a.getAppointmentDate() != null ? a.getAppointmentDate().toString() : "");
        
        String startTime = a.getStartTime() != null ? a.getStartTime().toString() : "";
        String endTime = a.getEndTime() != null ? a.getEndTime().toString() : "";
        dto.setTime(startTime + " - " + endTime);

        dto.setReason(a.getReason());
        dto.setStatus(a.getStatus());

        if (a.getDoctorId() != null) {
            if (a.getDoctorId().getUserId() != null) {
                dto.setDoctorName(a.getDoctorId().getUserId().getFullName());
            }
            if (a.getDoctorId().getSpecialtyId() != null) {
                dto.setSpecialty(a.getDoctorId().getSpecialtyId().getName());
            }
        }

        return dto;
    }
}
