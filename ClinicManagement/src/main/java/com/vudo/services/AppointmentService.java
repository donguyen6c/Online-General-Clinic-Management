/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.AppointmentResponseDTO;
import com.vudo.dto.AvailableSlotsResponseDTO;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface AppointmentService {
    AvailableSlotsResponseDTO getSlots(int doctorId, String date);
    List<AppointmentResponseDTO> getPatientAppointments(int patientId);
}
