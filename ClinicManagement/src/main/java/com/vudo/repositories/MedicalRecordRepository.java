/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.MedicalRecord;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface MedicalRecordRepository {
    List<MedicalRecord> getByPatientId(int patientId);
    MedicalRecord getMedicalRecordById(int id);
    MedicalRecord create(MedicalRecord medicalRecord);
}
