/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.MedicalRecordResponseDTO;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface MedicalRecordService {
    public List<MedicalRecordResponseDTO> getPatientHistory(int patientId);
}
