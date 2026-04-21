/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.dto.InventoryDTO;
import com.vudo.dto.MedicineDTO;
import com.vudo.pojo.Notification;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public interface PharmacyRepository {
    List<MedicineDTO> getMedicines(Map<String, String> params);
    List<InventoryDTO> getInventories(Map<String, String> params);
    void addMedicine(MedicineDTO medicineDTO);
    void addInventory(int medicineId, InventoryDTO dto);
    void updateMedicine(int medicineId, MedicineDTO dto);
    void updateInventory(int inventoryId, InventoryDTO dto);
    void deleteMedicine(int medicineId);
    void deleteInventory(int inventoryId);
    List<InventoryDTO> getExpiringInventories();
    List<InventoryDTO> getLowStockInventories();
    void dispenseMedicine(int prescriptionId);
}
