/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.MedicineDTO;
import com.vudo.repositories.PharmacyRepository;
import com.vudo.services.PharmacyService;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ASUS
 */
@Service
public class PharmacyServiceImpl implements PharmacyService{
    @Autowired
    private PharmacyRepository pharmacyRepo;

    @Override
    public List<MedicineDTO> getMedicines(Map<String, String> params) {
        return this.pharmacyRepo.getMedicines(params);
    }

    @Override
    public boolean addMedicine(MedicineDTO medicineDTO) {
        return this.pharmacyRepo.addMedicine(medicineDTO);
    }

    @Override
    public boolean updateMedicine(int id, MedicineDTO medicineDTO) {
        return this.pharmacyRepo.updateMedicine(id, medicineDTO);
    }
    
    
}
