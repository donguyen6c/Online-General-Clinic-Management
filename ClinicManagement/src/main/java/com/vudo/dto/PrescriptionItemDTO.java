/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

/**
 *
 * @author ASUS
 */
public class PrescriptionItemDTO {
    private Integer medicineId;
    private Integer quantity;
    private String usageInstruction;

    /**
     * @return the medicineId
     */
    public Integer getMedicineId() {
        return medicineId;
    }

    /**
     * @param medicineId the medicineId to set
     */
    public void setMedicineId(Integer medicineId) {
        this.medicineId = medicineId;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the usageInstruction
     */
    public String getUsageInstruction() {
        return usageInstruction;
    }

    /**
     * @param usageInstruction the usageInstruction to set
     */
    public void setUsageInstruction(String usageInstruction) {
        this.usageInstruction = usageInstruction;
    }
}
