/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vudo.dto.DoctorDTO;
import com.vudo.mapper.DoctorMapper;
import com.vudo.pojo.Doctor;
import com.vudo.pojo.User;
import com.vudo.repositories.DoctorRepository;
import com.vudo.services.DoctorService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ASUS
 */
@Service
public class DoctorServiceImpl implements DoctorService{
    @Autowired
    private DoctorRepository docRepo;
    
    @Autowired
    private Cloudinary cloudinary;

    @Override
    public List<DoctorDTO> getDoctors(Map<String, String> params) {
        return this.docRepo.getDoctors(params).stream().map(DoctorMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public DoctorDTO getDoctorById(int id) {
        Doctor doctor = this.docRepo.getDoctorById(id);
        if (doctor == null) {
            return null;
        }

        return DoctorMapper.toDTO(doctor);
    }

    @Override
    public void deleteDoctor(int id) {
        this.docRepo.deleteDoctor(id);
    }

    @Override
    public void addOrUpdateDoctorEntity(Doctor d) {
        User user = d.getUserId();
        
        if (user.getFile() != null && !user.getFile().isEmpty()) {
                try {
                    Map res = this.cloudinary.uploader().upload(user.getFile().getBytes(), 
                            ObjectUtils.asMap("resource_type", "auto"));
                    
                    user.setAvatar(res.get("secure_url").toString());
                    
                } catch (IOException ex) {
                    Logger.getLogger(DoctorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                    throw new RuntimeException("Lỗi khi tải ảnh đại diện lên server!");
                }
            }
        
        if (user != null) {
            String phoneRegex = "^\\d{9}$";
            if (user.getPhone() == null || !user.getPhone().matches(phoneRegex)) {
                throw new IllegalArgumentException("Số điện thoại không hợp lệ! Vui lòng nhập đúng 9 chữ số.");
            }
            
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
            if (user.getEmail() == null || !user.getEmail().matches(emailRegex)) {
                throw new IllegalArgumentException("Email không đúng định dạng.");
            }
        }
        this.docRepo.addOrUpdateDoctor(d);
    }

    @Override
    public List<Doctor> getDoctorEntities(Map<String, String> params) {
        return this.docRepo.getDoctors(params);
    }

    @Override
    public Doctor getDoctorEntityById(int id) {
        return this.docRepo.getDoctorById(id);
    }


}
