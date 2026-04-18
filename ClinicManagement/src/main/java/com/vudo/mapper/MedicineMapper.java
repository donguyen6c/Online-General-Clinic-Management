/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.mapper;

import com.vudo.dto.InventoryDTO;
import com.vudo.dto.MedicineDTO;
import com.vudo.pojo.Inventory;
import com.vudo.pojo.Medicine;

/**
 *
 * @author ASUS
 */
public class MedicineMapper {
    public static MedicineDTO toDTO(Medicine medicine) {
        if (medicine == null) return null;

        MedicineDTO dto = new MedicineDTO();
        dto.setId(medicine.getId());
        dto.setName(medicine.getName());
        dto.setUnit(medicine.getUnit());
        dto.setPrice(medicine.getPrice());

        return dto;
    }

    public static Medicine toMedicine(MedicineDTO dto) {
        if (dto == null) return null;
        
        Medicine m = new Medicine();
        m.setId(dto.getId()); 
        m.setName(dto.getName());
        m.setUnit(dto.getUnit());
        m.setPrice(dto.getPrice());
        
        return m;
    }
    
    public static void updateMedicineFromDTO(Medicine m, MedicineDTO dto) {
        if (m == null || dto == null) return;
        m.setName(dto.getName());
        m.setUnit(dto.getUnit());
        m.setPrice(dto.getPrice());
    }
}
