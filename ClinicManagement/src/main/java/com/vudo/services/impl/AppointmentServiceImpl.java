/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.AppointmentRequestDTO;
import com.vudo.dto.AppointmentResponseDTO;
import com.vudo.dto.AvailableSlotsResponseDTO;
import com.vudo.dto.TimeSlotDTO;
import com.vudo.dto.WorkingTimeDTO;
import com.vudo.mapper.AppointmentMapper;
import com.vudo.pojo.Appointment;
import com.vudo.pojo.Doctor;
import com.vudo.pojo.DoctorSchedule;
import com.vudo.pojo.DoctorWorkingPattern;
import com.vudo.pojo.User;
import com.vudo.repositories.AppointmentRepository;
import com.vudo.repositories.DoctorRepository;
import com.vudo.repositories.DoctorScheduleRepository;
import com.vudo.repositories.DoctorWorkingPatternRepository;
import com.vudo.repositories.UserRepository;
import com.vudo.services.AppointmentService;
import com.vudo.services.NotificationService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private NotificationService notificationService;

    @Override
    public AvailableSlotsResponseDTO getSlots(int doctorId, String dateStr) {
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

            availableSlots.add(new TimeSlotDTO(
                    current.toString(),
                    next.toString(),
                    !overlapped
            ));

            current = next;
        }

        AvailableSlotsResponseDTO res = new AvailableSlotsResponseDTO();
        res.setDoctorId(doctorId);
        res.setDate(dateStr);
        res.setAvailable(true);
        res.setWorkingTime(new WorkingTimeDTO(start.toString(), end.toString()));
        res.setSlots(availableSlots);

        return res;
    }

    private AvailableSlotsResponseDTO buildEmptyResponse(int doctorId, String date) {
        AvailableSlotsResponseDTO res = new AvailableSlotsResponseDTO();
        res.setDoctorId(doctorId);
        res.setDate(date);
        res.setAvailable(false);
        res.setWorkingTime(null);
        res.setSlots(new ArrayList<>());
        return res;
    }

    @Override
    @Transactional
    public List<AppointmentResponseDTO> getPatientAppointments(int patientId) {
        List<Appointment> appointments = appointmentRepo.getAppointmentsByPatientId(patientId);
        return appointments.stream().map(appointment -> AppointmentMapper.toDTO(appointment))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AppointmentResponseDTO createAppointment(int doctorId, AppointmentRequestDTO request) {
        AvailableSlotsResponseDTO slotsResponse = this.getSlots(doctorId, request.getDate());

        if (!slotsResponse.isAvailable()) {
            throw new IllegalArgumentException("Bác sĩ không làm việc trong ngày này");
        }

        boolean validSlot = slotsResponse.getSlots().stream().anyMatch(slot
                -> slot.getStartTime().equals(request.getStartTime())
                && slot.getEndTime().equals(request.getEndTime())
                && slot.isAvailable()
        );
        if (!validSlot) {
            throw new IllegalArgumentException("Khung giờ này không khả dụng");
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User patient = userRepo.getUserByUsername(username);
        Doctor doctor = doctorRepo.getDoctorById(doctorId);
        Appointment appointment = AppointmentMapper.toEntity(request, doctor, patient);
        Appointment saved = appointmentRepo.add(appointment);
        notificationService.createBookingNotification(
                patient,
                doctor.getUserId().getFullName(),
                request.getDate()+ " " + request.getStartTime()
        );
        return AppointmentMapper.toDTO(saved);
    }
}
