/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.AddPrescriptionRequestDTO;
import com.vudo.dto.AddServiceRequestDTO;
import com.vudo.dto.MedicalRecordRequestDTO;
import com.vudo.dto.MedicalRecordResponseDTO;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface MedicalRecordService {
    public List<MedicalRecordResponseDTO> getPatientHistory(int patientId);
    MedicalRecordResponseDTO getMedicalRecordDetail(int patientId, int recordId);
    MedicalRecordResponseDTO create(MedicalRecordRequestDTO request);
    void addPrescriptionsToRecord(Integer medicalRecordId, AddPrescriptionRequestDTO request);
    void addServicesToRecord(Integer medicalRecordId, AddServiceRequestDTO request);
}
