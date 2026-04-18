/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.InventoryDTO;
import com.vudo.dto.MedicineDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public interface PharmacyService {
    List<MedicineDTO> getMedicines(Map<String, String> params);
    List<InventoryDTO> getInventories(Map<String, String> params);
    void addMedicine(MedicineDTO medicineDTO);
    void updateMedicine(int id, MedicineDTO medicineDTO);
    void addInventory(int medicineId, InventoryDTO dto);
    void updateInventory(int inventoryId, InventoryDTO dto);
    void deleteMedicine(int medicineId);
    void deleteInventory(int inventoryId);
    List<InventoryDTO> getExpiringInventories();
    List<InventoryDTO> getLowStockInventories();
    void dispenseMedicine(int medicalRecordId);
}
