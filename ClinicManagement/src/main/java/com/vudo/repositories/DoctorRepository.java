/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.Doctor;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public interface DoctorRepository {
    List<Doctor> getDoctors(Map<String, String> params);
    Doctor getDoctorById(int id);
    void addOrUpdateDoctor(Doctor d);
    void deleteDoctor(int id);
}
