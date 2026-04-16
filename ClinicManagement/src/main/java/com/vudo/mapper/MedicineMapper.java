/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.mapper;

import com.vudo.dto.MedicineDTO;
import com.vudo.pojo.Inventory;
import com.vudo.pojo.Medicine;

/**
 *
 * @author ASUS
 */
public class MedicineMapper {
    public static MedicineDTO toDTO(Medicine medicine, Inventory inventory) {
        if (medicine == null) return null;

        MedicineDTO dto = new MedicineDTO();
        // Ánh xạ phần Medicine
        dto.setId(medicine.getId());
        dto.setName(medicine.getName());
        dto.setUnit(medicine.getUnit());
        dto.setPrice(medicine.getPrice());

        // Ánh xạ phần Inventory (nếu có)
        if (inventory != null) {
            dto.setInventoryId(inventory.getId());
            dto.setQuantity(inventory.getQuantity());
            dto.setExpiryDate(inventory.getExpiryDate());
        }

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

    public static Inventory toInventory(MedicineDTO dto, Medicine medicine) {
        if (dto == null || medicine == null) return null;
        
        Inventory inv = new Inventory();
        inv.setId(dto.getInventoryId());
        inv.setMedicineId(medicine);
        inv.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : 0);
        inv.setExpiryDate(dto.getExpiryDate());
        
        return inv;
    }
    
    public static void updateMedicineFromDTO(Medicine m, MedicineDTO dto) {
        if (m == null || dto == null) return;
        m.setName(dto.getName());
        m.setUnit(dto.getUnit());
        m.setPrice(dto.getPrice());
    }

    public static void updateInventoryFromDTO(Inventory inv, MedicineDTO dto) {
        if (inv == null || dto == null) return;
        inv.setQuantity(dto.getQuantity() != null ? dto.getQuantity() : inv.getQuantity());
        inv.setExpiryDate(dto.getExpiryDate());
    }
}
