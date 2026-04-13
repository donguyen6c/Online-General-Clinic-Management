/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.mapper.MedicalRecordMapper;
import com.vudo.pojo.MedicalRecord;
import com.vudo.repositories.MedicalRecordRepository;
import com.vudo.services.MedicalRecordService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    @Transactional
    public List<MedicalRecordResponseDTO> getPatientHistory(int patientId) {
        List<MedicalRecord> records = medicalRecordRepo.getByPatientId(patientId);
        return records.stream().map(record -> MedicalRecordMapper.toDTO(record)).collect(Collectors.toList());
    }
}


