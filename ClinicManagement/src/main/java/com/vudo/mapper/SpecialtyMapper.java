/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.mapper;

import com.vudo.dto.SpecialtyDTO;
import com.vudo.pojo.Specialty;

/**
 *
 * @author ADMIN
 */
public class SpecialtyMapper {
    public static SpecialtyDTO toDTO(Specialty s) {
        if (s == null)
            return null;

        SpecialtyDTO dto = new SpecialtyDTO();
        dto.setId(s.getId());
        dto.setName(s.getName());
        dto.setDescription(s.getDescription());

        return dto;
    }
}
