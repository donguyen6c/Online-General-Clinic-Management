package com.vudo.events;

import com.vudo.pojo.User;

public class BookingCreatedEvent {

    private final User patient;
    private final String doctorName;
    private final String appointmentTime;

    public BookingCreatedEvent(User patient, String doctorName, String appointmentTime) {
        this.patient = patient;
        this.doctorName = doctorName;
        this.appointmentTime = appointmentTime;
    }

    public User getPatient() {
        return patient;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getAppointmentTime() {
        return appointmentTime;
    }
}
