/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.DoctorDTO;
import com.vudo.dto.DoctorsDTO;
import com.vudo.pojo.Doctor;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public interface DoctorService {
    public Map<String, Object> getDoctors(Map<String, String> params);
    List<Doctor> getDoctorEntities(Map<String, String> params);
    DoctorDTO getDoctorById(int id);
    Doctor getDoctorEntityById(int id);
    void deleteDoctor(int id);
    void addOrUpdateDoctorEntity(Doctor d);
}