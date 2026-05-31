package com.vudo.events;

import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.User;

public class PaymentCompletedEvent {

    private final User patient;
    private final MedicalRecord medicalRecord;
    private final String time;

    public PaymentCompletedEvent(User patient, MedicalRecord medicalRecord, String time) {
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
