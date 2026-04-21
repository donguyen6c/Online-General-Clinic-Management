/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

import java.util.List;

/**
 *
 * @author ASUS
 */
public class AddPrescriptionRequestDTO {
    private List<PrescriptionItemDTO> medicines;

    /**
     * @return the medicines
     */
    public List<PrescriptionItemDTO> getMedicines() {
        return medicines;
    }

    /**
     * @param medicines the medicines to set
     */
    public void setMedicines(List<PrescriptionItemDTO> medicines) {
        this.medicines = medicines;
    }
}
