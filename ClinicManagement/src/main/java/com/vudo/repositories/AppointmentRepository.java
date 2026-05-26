/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.Appointment;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface AppointmentRepository {
    List<Appointment> getByDoctorIdAndDate(int doctorId, LocalDate appointmentDate);
    List<Appointment> getAppointmentsByPatientId(int patientId, int page);
    Appointment add(Appointment appointment);
    Appointment getById(int id);
    List<Appointment> getAppointmentsByDoctorId(int doctorId, Map<String, String> params);
    Appointment cancel(Appointment appointment);
    Appointment completed (Appointment appointment);
}
