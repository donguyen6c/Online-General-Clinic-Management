/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.dto.AddServiceRequestDTO;
import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.PrescribedMedicine;
import com.vudo.pojo.MedicalRecordService;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface MedicalRecordRepository {
    List<MedicalRecord> getByPatientId(int patientId);
    MedicalRecord getMedicalRecordById(int id);
    MedicalRecord create(MedicalRecord medicalRecord);
    void addPrescriptionsToRecord(PrescribedMedicine pm);
    void addServicesToRecord(MedicalRecordService recordService);
}
