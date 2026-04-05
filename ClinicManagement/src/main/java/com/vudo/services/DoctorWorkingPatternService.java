/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.DoctorWorkingPatternDTO;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface DoctorWorkingPatternService {
    public List<DoctorWorkingPatternDTO> getByDoctorId(int id);
}
