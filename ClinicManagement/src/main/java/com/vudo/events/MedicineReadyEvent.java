package com.vudo.events;

import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.User;

public class MedicineReadyEvent {

    private final User patient;
    private final MedicalRecord medicalRecord;
    private final String time;

    public MedicineReadyEvent(User patient, MedicalRecord medicalRecord, String time) {
        this.patient = patient;
        this.medicalRecord = medicalRecord;
        this.time = time;
    }

    public User getPatient() {
        return patient;
    }

    public MedicalRecord getMedicalRecord() {
        return medicalRecord;
    }

    public String getTime() {
        return time;
    }
}
