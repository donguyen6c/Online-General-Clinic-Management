/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.AppointmentResponseDTO;
import com.vudo.dto.AvailableSlotsResponseDTO;
import com.vudo.dto.TimeSlotDTO;
import com.vudo.dto.WorkingTimeDTO;
import com.vudo.mapper.AppointmentMapper;
import com.vudo.pojo.Appointment;
import com.vudo.pojo.DoctorSchedule;
import com.vudo.pojo.DoctorWorkingPattern;
import com.vudo.repositories.AppointmentRepository;
import com.vudo.repositories.DoctorScheduleRepository;
import com.vudo.repositories.DoctorWorkingPatternRepository;
import com.vudo.services.AppointmentService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author ADMIN
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepo;

    @Autowired
    private DoctorWorkingPatternRepository workingPatternRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Override
    public AvailableSlotsResponseDTO getAvailableSlots(int doctorId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);

        DoctorSchedule schedule = doctorScheduleRepo.getByDoctorIdAndWorkDate(doctorId, date);

        LocalTime start;
        LocalTime end;
        int slotDuration = 30;

        if (schedule != null) {

            if (schedule.getIsAvailable() == false) {
                return this.buildEmptyResponse(doctorId, dateStr);
            }

            start = schedule.getStartTime();
            end = schedule.getEndTime();

        } else {
            int dayOfWeek = date.getDayOfWeek().getValue();

            DoctorWorkingPattern pattern = workingPatternRepo
                    .getByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);

            if (pattern == null) {
                return this.buildEmptyResponse(doctorId, dateStr);
            }

            start = pattern.getStartTime();
            end = pattern.getEndTime();
        }

        List<TimeSlotDTO> availableSlots = new ArrayList<>();
        List<Appointment> appointments = appointmentRepo.getByDoctorIdAndDate(doctorId, date);

        LocalTime current = start;
        while (!current.plusMinutes(slotDuration).isAfter(end)) {
            LocalTime next = current.plusMinutes(slotDuration);

            boolean overlapped = false;
            for (Appointment ap : appointments) {
                if (current.isBefore(ap.getEndTime()) && next.isAfter(ap.getStartTime())) {
                    overlapped = true;
                    break;
                }
            }

            if (!overlapped) {
                availableSlots.add(new TimeSlotDTO(current.toString(), next.toString()));
            }

            current = next;
        }

        AvailableSlotsResponseDTO res = new AvailableSlotsResponseDTO();
        res.setDoctorId(doctorId);
        res.setDate(dateStr);
        res.setAvailable(true);
        res.setWorkingTime(new WorkingTimeDTO(start.toString(), end.toString()));
        res.setAvailableSlots(availableSlots);

        return res;
    }

    private AvailableSlotsResponseDTO buildEmptyResponse(int doctorId, String date) {
        AvailableSlotsResponseDTO res = new AvailableSlotsResponseDTO();
        res.setDoctorId(doctorId);
        res.setDate(date);
        res.setAvailable(false);
        res.setWorkingTime(null);   
        res.setAvailableSlots(new ArrayList<>());
        return res;
    }

    @Override
    @Transactional
    public List<AppointmentResponseDTO> getPatientAppointments(int patientId) {
        List<Appointment> appointments = appointmentRepo.getAppointmentsByPatientId(patientId);
        return appointments.stream().map(appointment -> AppointmentMapper.toDTO(appointment))
                .collect(Collectors.toList());
    }
}
