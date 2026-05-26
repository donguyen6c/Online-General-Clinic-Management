/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.AppointmentRequestDTO;
import com.vudo.dto.AppointmentResponseDTO;
import com.vudo.dto.AvailableSlotsResponseDTO;
import com.vudo.pojo.Appointment;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface AppointmentService {
    AvailableSlotsResponseDTO getSlots(int doctorId, String date);
    List<AppointmentResponseDTO> getPatientAppointments(int patientId, int page);
    AppointmentResponseDTO createAppointment(int doctorId, AppointmentRequestDTO request);
    List<AppointmentResponseDTO> getDoctorAppointments(Map<String, String> params);
    AppointmentResponseDTO cancelAppointment(int appointmentId);
    AppointmentResponseDTO completedAppointment (int appointmentId);
}
