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
public class InventoryMapper {
    public static InventoryDTO toDTO(Inventory inventory) {
        if (inventory == null) return null;

        InventoryDTO dto = new InventoryDTO();
        dto.setId(inventory.getId());
        dto.setQuantity(inventory.getQuantity());
        dto.setExpiryDate(inventory.getExpiryDate());

        if (inventory.getMedicineId() != null) {
            dto.setMedicine(MedicineMapper.toDTO(inventory.getMedicineId()));
        }

        return dto;
    }

    public static Inventory toInventory(InventoryDTO dto, Medicine medicine) {
        if (dto == null || medicine == null) return null;
        
        Inventory inv = new Inventory();
        inv.setMedicineId(medicine);
        inv.setQuantity( dto.getQuantity());
        inv.setExpiryDate(dto.getExpiryDate());
        
        return inv;
    }

    public static void updateInventoryFromDTO(Inventory inv, InventoryDTO dto) {
        if (inv == null || dto == null) return;
        if (dto.getQuantity() != null) {
            inv.setQuantity(dto.getQuantity());
        }
        
        if (dto.getExpiryDate() != null) {
            inv.setExpiryDate(dto.getExpiryDate());
        }
    }
}
