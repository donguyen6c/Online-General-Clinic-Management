/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.DoctorSchedule;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface DoctorScheduleRepository {
    DoctorSchedule getByDoctorIdAndWorkDate(int doctorId, LocalDate workDate);
}
