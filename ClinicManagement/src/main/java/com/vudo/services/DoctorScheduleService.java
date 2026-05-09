/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.DoctorScheduleRequestDTO;
import com.vudo.dto.DoctorScheduleResponseDTO;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface DoctorScheduleService {
    List<DoctorScheduleResponseDTO> getMySchedules();
    DoctorScheduleResponseDTO createSchedule(DoctorScheduleRequestDTO request);
    void deleteSchedule(int id);
}
