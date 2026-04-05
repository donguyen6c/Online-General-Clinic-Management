/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.SpecialtyDTO;
import com.vudo.mapper.SpecialtyMapper;
import com.vudo.pojo.Specialty;
import com.vudo.repositories.SpecialtyRepository;
import com.vudo.services.SpecialtyService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ASUS
 */
@Service
public class SpecialtyServiceImpl implements SpecialtyService{
    @Autowired
    private SpecialtyRepository specRepo;

    @Override
    public List<SpecialtyDTO> getSpec() {
        List<Specialty> specs = this.specRepo.getSpec();

        return specs.stream().map(SpecialtyMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<Specialty> getSpecEntity() {
        return this.specRepo.getSpec();
    }
    
}
