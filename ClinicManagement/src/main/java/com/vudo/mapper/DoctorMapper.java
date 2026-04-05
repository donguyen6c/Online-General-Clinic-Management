/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.mapper;

import com.vudo.dto.DoctorDTO;
import com.vudo.pojo.Doctor;

/**
 *
 * @author ADMIN
 */
public class DoctorMapper {
    public static DoctorDTO toDTO(Doctor d) {
        if (d == null) {
            return null;
        }

        DoctorDTO dto = new DoctorDTO();
        dto.setId(d.getId());
        dto.setBio(d.getBio());
        dto.setUser(UserMapper.toDTO(d.getUserId()));
        dto.setSpecialty(SpecialtyMapper.toDTO(d.getSpecialtyId()));

        return dto;
    }
}
