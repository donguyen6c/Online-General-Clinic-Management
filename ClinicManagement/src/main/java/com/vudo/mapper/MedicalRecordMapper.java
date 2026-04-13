/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.mapper;

import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.dto.PrescriptionDTO;
import com.vudo.dto.TestResultDTO;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.PrescribedMedicine;
import com.vudo.pojo.TestResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ASUS
 */
public class MedicalRecordMapper {
    public static MedicalRecordResponseDTO toDTO(MedicalRecord record) {
        if (record == null) {
            return null;
        }

        MedicalRecordResponseDTO dto = new MedicalRecordResponseDTO();

        dto.setRecordId(record.getId());
        dto.setDate(record.getCreatedAt() != null ? record.getCreatedAt().toString() : "");

        if (record.getDoctorId() != null && record.getDoctorId().getUserId() != null) {
            dto.setDoctorName(record.getDoctorId().getUserId().getFullName());
        }

        dto.setDiagnosis(record.getDiagnosis());

        List<TestResultDTO> testDTOs = new ArrayList<>();
        Set<TestResult> testResults = record.getTestResultSet();
        if (testResults != null && !testResults.isEmpty()) {
            for (TestResult tr : testResults) {
                TestResultDTO trDto = new TestResultDTO();
                trDto.setTestName(tr.getTestName());
                trDto.setResultValue(tr.getResultValue());
                trDto.setFileUrl(tr.getFileUrl());
                testDTOs.add(trDto);
            }
        }
        dto.setTestResults(testDTOs);

        List<PrescriptionDTO> presDTOs = new ArrayList<>();
        Set<PrescribedMedicine> prescriptions = record.getPrescribedMedicineSet();
        if (prescriptions != null && !prescriptions.isEmpty()) {
            for (PrescribedMedicine pm : prescriptions) {
                PrescriptionDTO presDto = new PrescriptionDTO();
                presDto.setMedicineName(pm.getMedicineId().getName());
                presDto.setQuantity(pm.getQuantity());
                presDto.setInstruction(pm.getUsageInstruction());
                presDTOs.add(presDto);
            }
        }
        dto.setPrescriptions(presDTOs);

        return dto;
    }
}
