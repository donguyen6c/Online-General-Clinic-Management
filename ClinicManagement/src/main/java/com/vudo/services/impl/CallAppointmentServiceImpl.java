/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.pojo.Appointment;
import com.vudo.pojo.User;
import com.vudo.repositories.AppointmentRepository;
import com.vudo.repositories.DoctorRepository;
import com.vudo.repositories.UserRepository;
import com.vudo.services.CallAppointmentService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Service
@Transactional
public class CallAppointmentServiceImpl implements CallAppointmentService{

    @Autowired
    private AppointmentRepository appointmentRepo;
    
    @Autowired
    private UserRepository userRepo;
    
    @Override
    public String getAuthorizedMeetingUrl(Integer appointmentId, String currentUsername) {
        User currentUser = userRepo.getUserByUsername(currentUsername);
        if (currentUser == null) {
            throw new RuntimeException("Không tìm thấy user");
        }

        Appointment appointment = appointmentRepo.getById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("Không tìm thấy lịch hẹn");
        }

        if (!appointment.getStatus().equals("scheduled")) {
            throw new RuntimeException("Lịch hẹn chưa sẵn sàng hoặc đã kết thúc");
        }

        boolean isPatient = false;
        boolean isDoctor = false;

        String currentUserIdStr = String.valueOf(currentUser.getId());

        if (appointment.getPatientId() != null) {
            String apptPatientIdStr = String.valueOf(appointment.getPatientId().getId()); 
            if (apptPatientIdStr.equals(currentUserIdStr)) {
                isPatient = true;
            }
        }

        if (appointment.getDoctorId() != null && appointment.getDoctorId().getUserId() != null) {
            String apptDoctorUserIdStr = String.valueOf(appointment.getDoctorId().getUserId().getId());
            if (apptDoctorUserIdStr.equals(currentUserIdStr)) {
                isDoctor = true;
            }
        }

        if (!isPatient && !isDoctor) {
            throw new RuntimeException("Bạn không có quyền tham gia phòng khám này!");
        }

        if (appointment.getMeetingUrl() == null || appointment.getMeetingUrl().isEmpty()) {
            String newRoomName = "phong-kham-tv-" + appointment.getId() + "-" + UUID.randomUUID().toString().substring(0, 8);
            appointment.setMeetingUrl(newRoomName);
        }

        return appointment.getMeetingUrl();
    }
    
}
