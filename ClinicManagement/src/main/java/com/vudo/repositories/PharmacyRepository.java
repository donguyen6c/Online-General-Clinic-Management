/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.dto.MedicineDTO;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public interface PharmacyRepository {
    List<MedicineDTO> getMedicines(Map<String, String> params);
    boolean addMedicine(MedicineDTO medicineDTO);
    boolean updateMedicine(int medicineId, MedicineDTO medicineDTO);
}
