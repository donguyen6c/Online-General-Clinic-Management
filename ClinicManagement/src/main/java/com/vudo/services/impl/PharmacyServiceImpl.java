/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.InventoryDTO;
import com.vudo.dto.MedicineDTO;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.Notification;
import com.vudo.pojo.User;
import com.vudo.repositories.MedicalRecordRepository;
import com.vudo.repositories.PharmacyRepository;
import com.vudo.services.PharmacyService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Service
public class PharmacyServiceImpl implements PharmacyService{
    @Autowired
    private PharmacyRepository pharmacyRepo;

    @Autowired
    private MedicalRecordRepository medicalRecordRepo;
    
    @Override
    public List<MedicineDTO> getMedicines(Map<String, String> params) {
        return pharmacyRepo.getMedicines(params);
    }

    @Override
    public void addMedicine(MedicineDTO dto) {
        pharmacyRepo.addMedicine(dto);
    }

    @Override
    public void updateMedicine(int id, MedicineDTO dto) {
        pharmacyRepo.updateMedicine(id, dto);
    }

    @Override
    public void deleteMedicine(int id) {
        pharmacyRepo.deleteMedicine(id);
    }
    
    @Override
    public void addInventory(int medicineId, InventoryDTO dto) {
        pharmacyRepo.addInventory(medicineId, dto);
    }

    @Override
    public void updateInventory(int inventoryId, InventoryDTO dto) {
        pharmacyRepo.updateInventory(inventoryId, dto);
    }

    @Override
    public void deleteInventory(int inventoryId) {
        pharmacyRepo.deleteInventory(inventoryId);
    }

    @Override
    public List<InventoryDTO> getInventories(Map<String, String> params) {
        return pharmacyRepo.getInventories(params);
    }
    
    @Override
    public List<InventoryDTO> getExpiringInventories() {
        return pharmacyRepo.getExpiringInventories();
    }

    @Override
    public List<InventoryDTO> getLowStockInventories() {
        return pharmacyRepo.getLowStockInventories();
    }

    @Override
    @Transactional
    public void dispenseMedicine(int medicalRecordId) {
        pharmacyRepo.dispenseMedicine(medicalRecordId);

        MedicalRecord mr = medicalRecordRepo.getMedicalRecordById(medicalRecordId);

        User patient = mr.getPatientId(); 

        Notification n = new Notification();
        n.setUserId(patient);
        n.setTitle("Đơn thuốc");
        n.setMessage("Thuốc của đơn hàng #" + medicalRecordId + " đã chuẩn bị xong.");
        n.setType("Medicines");
        n.setCreatedAt(new Date());
        n.setIsRead(false);

        pharmacyRepo.addNotification(n);
    }

}
