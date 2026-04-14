/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.UserDTO;
import java.util.Map;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ASUS
 */
public interface UserService extends UserDetailsService{
    UserDTO getUserByUsername(String username);
    UserDTO addUser(Map<String, String> params, MultipartFile avatar);
    boolean authenticate(String username, String password);
}
