/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.pojo.Appointment;
import com.vudo.pojo.Doctor;
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
    
    @Autowired
    private DoctorRepository doctorRepo;
    
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

        // 3. Kiểm tra trạng thái
        if (!appointment.getStatus().equals("scheduled")) {
            throw new RuntimeException("Lịch hẹn chưa sẵn sàng hoặc đã kết thúc");
        }

        // 4. KIỂM TRA QUYỀN TRUY CẬP (ĐÃ FIX LOGIC)
        boolean isPatient = false;
        boolean isDoctor = false;

        // ID của người đang đăng nhập (Là một con số Integer/Long, ví dụ: 6)
        String currentUserIdStr = String.valueOf(currentUser.getId());

        // 4.1. Kiểm tra Bệnh nhân:
        // appointment.getPatientId() đang trả về Object User, nên ta .getId() một lần nữa để lấy số 6
        if (appointment.getPatientId() != null) {
            // Lấy ID từ Object User
            String apptPatientIdStr = String.valueOf(appointment.getPatientId().getId()); 
            if (apptPatientIdStr.equals(currentUserIdStr)) {
                isPatient = true;
            }
        }

        // 4.2. Kiểm tra Bác sĩ:
        // appointment.getDoctorId() đang trả về Object Doctor, ta .getUserId().getId() để lấy ra ID của User
        if (appointment.getDoctorId() != null && appointment.getDoctorId().getUserId() != null) {
            // Lấy ID User từ Object Doctor
            String apptDoctorUserIdStr = String.valueOf(appointment.getDoctorId().getUserId().getId());
            if (apptDoctorUserIdStr.equals(currentUserIdStr)) {
                isDoctor = true;
            }
        }

        if (!isPatient && !isDoctor) {
            throw new RuntimeException("Bạn không có quyền tham gia phòng khám này!");
        }

        // 5. Nếu chưa có meeting_url thì tạo mới
        if (appointment.getMeetingUrl() == null || appointment.getMeetingUrl().isEmpty()) {
            String newRoomName = "phong-kham-tv-" + appointment.getId() + "-" + UUID.randomUUID().toString().substring(0, 8);
            appointment.setMeetingUrl(newRoomName);
            
        }

        return appointment.getMeetingUrl();
    }
    
}
