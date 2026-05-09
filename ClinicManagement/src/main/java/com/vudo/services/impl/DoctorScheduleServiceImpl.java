/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.DoctorScheduleRequestDTO;
import com.vudo.dto.DoctorScheduleResponseDTO;
import com.vudo.pojo.Doctor;
import com.vudo.pojo.DoctorSchedule;
import com.vudo.repositories.DoctorRepository;
import com.vudo.repositories.DoctorScheduleRepository;
import com.vudo.services.DoctorScheduleService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Service
public class DoctorScheduleServiceImpl implements DoctorScheduleService{

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    private Doctor getCurrentDoctor() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctor doctor = doctorRepo.getDoctorByUserName(username);
        if (doctor == null) throw new RuntimeException("Không tìm thấy bác sĩ");
        return doctor;
    }

    private DoctorScheduleResponseDTO toDTO(DoctorSchedule s) {
        DoctorScheduleResponseDTO dto = new DoctorScheduleResponseDTO();
        dto.setId(s.getId());
        dto.setWorkDate(s.getWorkDate() != null ? s.getWorkDate().toString() : null);
        dto.setStartTime(s.getStartTime() != null ? s.getStartTime().toString() : null);
        dto.setEndTime(s.getEndTime() != null ? s.getEndTime().toString() : null);
        dto.setNote(s.getNote());
        dto.setIsAvailable(s.getIsAvailable());
        return dto;
    }

    @Override
    @Transactional
    public List<DoctorScheduleResponseDTO> getMySchedules() {
        return doctorScheduleRepo.getByDoctorId(getCurrentDoctor().getId())
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DoctorScheduleResponseDTO createSchedule(DoctorScheduleRequestDTO request) {
        Doctor doctor = getCurrentDoctor();

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setDoctorId(doctor);
        schedule.setWorkDate(LocalDate.parse(request.getWorkDate()));
        schedule.setIsAvailable(request.getIsAvailable());
        schedule.setNote(request.getNote());

        if (Boolean.FALSE.equals(request.getIsAvailable())) {
            schedule.setStartTime(LocalTime.of(0, 0));
            schedule.setEndTime(LocalTime.of(23, 59));
        } else {
            if (request.getStartTime() == null || request.getEndTime() == null)
                throw new RuntimeException("Vui lòng nhập giờ bắt đầu và kết thúc");
            schedule.setStartTime(LocalTime.parse(request.getStartTime()));
            schedule.setEndTime(LocalTime.parse(request.getEndTime()));
        }

        return toDTO(doctorScheduleRepo.save(schedule));
    }

    @Override
    @Transactional
    public void deleteSchedule(int id) {
        doctorScheduleRepo.delete(id);
    }
    
}
