/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.DoctorWorkingPattern;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface DoctorWorkingPatternRepository {
    List<DoctorWorkingPattern> getByDoctorId(int doctorId);
    DoctorWorkingPattern getByDoctorIdAndDayOfWeek(int doctorId, int dayOfWeek);
    DoctorWorkingPattern getById(int id);
    void add(DoctorWorkingPattern pattern);
    void update(DoctorWorkingPattern pattern);
    void delete(int id);
}
