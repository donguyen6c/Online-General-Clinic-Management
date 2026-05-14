/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

import java.math.BigDecimal;

/**
 *
 * @author ADMIN
 */
public class CreatePaymentRequestDTO {
    private Integer medicalRecordId;

    /**
     * @return the medicalRecordId
     */
    public Integer getMedicalRecordId() {
        return medicalRecordId;
    }

    /**
     * @param medicalRecordId the medicalRecordId to set
     */
    public void setMedicalRecordId(Integer medicalRecordId) {
        this.medicalRecordId = medicalRecordId;
    }

    /**
     * @return the amount
     */
}
