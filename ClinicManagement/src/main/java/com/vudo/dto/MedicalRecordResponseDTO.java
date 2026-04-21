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
public class MedicalRecordResponseDTO {
    private int recordId;
    private String date;
    private String doctorName;
    private String diagnosis;
    private List<TestResultDTO> testResults;
    private List<PrescriptionDTO> prescriptions;
    private List<ServiceResponseDTO> services;

    /**
     * @return the recordId
     */
    public int getRecordId() {
        return recordId;
    }

    /**
     * @param recordId the recordId to set
     */
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the doctorName
     */
    public String getDoctorName() {
        return doctorName;
    }

    /**
     * @param doctorName the doctorName to set
     */
    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
     * @return the testResults
     */
    public List<TestResultDTO> getTestResults() {
        return testResults;
    }

    /**
     * @param testResults the testResults to set
     */
    public void setTestResults(List<TestResultDTO> testResults) {
        this.testResults = testResults;
    }

    /**
     * @return the prescriptions
     */
    public List<PrescriptionDTO> getPrescriptions() {
        return prescriptions;
    }

    /**
     * @param prescriptions the prescriptions to set
     */
    public void setPrescriptions(List<PrescriptionDTO> prescriptions) {
        this.prescriptions = prescriptions;
    }

    /**
     * @return the services
     */
    public List<ServiceResponseDTO> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<ServiceResponseDTO> services) {
        this.services = services;
    }
}
