/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

import java.util.List;
import java.util.Set;

/**
 *
 * @author ADMIN
 */
public class MedicalRecordRequestDTO {
    private String symptoms;
    private String diagnosis;
    private String prescriptionNotes;

    private Integer patientId;
    private Integer appointmentId;

    private Set<Integer> diseaseIds;

    /**
     * @return the symptoms
     */
    public String getSymptoms() {
        return symptoms;
    }

    /**
     * @param symptoms the symptoms to set
     */
    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    /**
     * @return the diagnosis
     */
    public String getDiagnosis() {
        return diagnosis;
    }

    /**
     * @param diagnosis the diagnosis to set
     */
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    /**
     * @return the prescriptionNotes
     */
    public String getPrescriptionNotes() {
        return prescriptionNotes;
    }

    /**
     * @param prescriptionNotes the prescriptionNotes to set
     */
    public void setPrescriptionNotes(String prescriptionNotes) {
        this.prescriptionNotes = prescriptionNotes;
    }

    /**
     * @return the patientId
     */
    public Integer getPatientId() {
        return patientId;
    }

    /**
     * @param patientId the patientId to set
     */
    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    /**
     * @return the appointmentId
     */
    public Integer getAppointmentId() {
        return appointmentId;
    }

    /**
     * @param appointmentId the appointmentId to set
     */
    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * @return the diseaseIds
     */
    public Set<Integer> getDiseaseIds() {
        return diseaseIds;
    }

    /**
     * @param diseaseIds the diseaseIds to set
     */
    public void setDiseaseIds(Set<Integer> diseaseIds) {
        this.diseaseIds = diseaseIds;
    }
}
