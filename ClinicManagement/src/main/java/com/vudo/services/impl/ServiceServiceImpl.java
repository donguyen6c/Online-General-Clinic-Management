/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.ClinicServiceDTO;
import com.vudo.pojo.MedicalRecordService;
import com.vudo.pojo.Service;
import com.vudo.repositories.ServicesRepository;
import com.vudo.services.ServiceService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@org.springframework.stereotype.Service
public class ServiceServiceImpl implements ServiceService{

    @Autowired
    private ServicesRepository serviceRepo;

    @Override
    @Transactional
    public List<ClinicServiceDTO> getAllServices() {
        return serviceRepo.getAllServices().stream().map(s -> {
            ClinicServiceDTO dto = new ClinicServiceDTO();
            dto.setId(s.getId());
            dto.setName(s.getName());
            dto.setPrice(s.getPrice());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClinicServiceDTO updateService(int id, ClinicServiceDTO dto) {
        Service clinicService = serviceRepo.getServiceById(id);
        if (clinicService == null) {
            throw new RuntimeException("Không tìm thấy dịch vụ với ID: " + id);
        }
        
        clinicService.setName(dto.getName());
        clinicService.setPrice(dto.getPrice());
        
        serviceRepo.updateService(clinicService);
        dto.setId(id);
        return dto;
    }

    @Override
    public ClinicServiceDTO createService(ClinicServiceDTO dto) {
        Service clinicService = new Service();
        clinicService.setName(dto.getName());
        clinicService.setPrice(dto.getPrice());

        serviceRepo.addService(clinicService);

        dto.setId(clinicService.getId());
        return dto;
    }
    
}
