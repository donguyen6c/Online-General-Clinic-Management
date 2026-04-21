/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.MedicalRecordRequestDTO;
import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.mapper.MedicalRecordMapper;
import com.vudo.pojo.Appointment;
import com.vudo.pojo.Disease;
import com.vudo.pojo.Doctor;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.User;
import com.vudo.repositories.AppointmentRepository;
import com.vudo.repositories.DiseaseRepository;
import com.vudo.repositories.DoctorRepository;
import com.vudo.repositories.MedicalRecordRepository;
import com.vudo.repositories.UserRepository;
import com.vudo.services.MedicalRecordService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DiseaseRepository diseaseRepo;

    @Override
    @Transactional
    public List<MedicalRecordResponseDTO> getPatientHistory(int patientId) {
        List<MedicalRecord> records = medicalRecordRepo.getByPatientId(patientId);
        return records.stream().map(record -> MedicalRecordMapper.toDTO(record)).collect(Collectors.toList());
    }

    @Override
    public MedicalRecordResponseDTO create(MedicalRecordRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Doctor doctor = doctorRepo.getDoctorByUserName(username);
        if (doctor == null) {
            throw new RuntimeException("Doctor not found");
        }
        Appointment appointment = null;
        User patient = null;
        if (request.getAppointmentId() == null && request.getPatientId() == null) {
            throw new RuntimeException("Phải truyền appointmentId hoặc patientId");
        }
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepo.getById(request.getAppointmentId());
            if (appointment == null) {
                throw new RuntimeException("Appointment not found");
            }

            patient = appointment.getPatientId();
            if (request.getPatientId() != null
                    && !patient.getId().equals(request.getPatientId())) {
                throw new RuntimeException("patientId không khớp với appointment");
            }
        }
        else {
            patient = userRepo.getUserById(request.getPatientId());
            if (patient == null) {
                throw new RuntimeException("Patient not found");
            }
        }

        Set<Disease> diseases = new HashSet<>();
        if (request.getDiseaseIds() != null && !request.getDiseaseIds().isEmpty()) {
            diseases = new HashSet<>(diseaseRepo.getAllById(request.getDiseaseIds()));
        }
        System.out.println(diseases.size());
        MedicalRecord record = MedicalRecordMapper.toEntity(
                request, doctor, patient, appointment, diseases
        );

        record = medicalRecordRepo.create(record);

        return MedicalRecordMapper.toDTO(record);
    }
}
