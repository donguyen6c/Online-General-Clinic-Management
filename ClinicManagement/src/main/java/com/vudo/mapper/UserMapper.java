/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.mapper;

import com.vudo.dto.UserDTO;
import com.vudo.pojo.User;

/**
 *
 * @author ADMIN
 */
public class UserMapper {

    public static UserDTO toDTO(User u) {
        if (u == null) {
            return null;
        }

        UserDTO dto = new UserDTO();

        dto.setId(u.getId());
        dto.setUsername(u.getUsername());
        dto.setEmail(u.getEmail());
        dto.setFullName(u.getFullName());
        dto.setPhone(u.getPhone());
        dto.setGender(u.getGender());
        dto.setDateOfBirth(u.getDateOfBirth());
        dto.setRole(u.getRole());
        dto.setAvatar(u.getAvatar());
        dto.setActive(u.getActive());

        return dto;
    }
}
