/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.ClinicServiceDTO;
import java.util.List;

/**
 *
 * @author ASUS
 */
public interface ServiceService {
    List<ClinicServiceDTO> getAllServices();
    ClinicServiceDTO updateService(int id, ClinicServiceDTO dto);
    ClinicServiceDTO createService(ClinicServiceDTO dto);
}
