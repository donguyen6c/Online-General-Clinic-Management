package com.vudo.listeners;

import com.vudo.events.BookingCreatedEvent;
import com.vudo.events.MedicalRecordCompletedEvent;
import com.vudo.events.MedicineReadyEvent;
import com.vudo.events.PaymentCompletedEvent;
import com.vudo.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    @Autowired
    private NotificationService notificationService;

    @EventListener
    public void handleBookingCreated(BookingCreatedEvent event) {
        notificationService.createBookingNotification(event.getPatient(), event.getDoctorName(), event.getAppointmentTime());
    }

    @EventListener
    public void handleMedicalRecordCompleted(MedicalRecordCompletedEvent event) {
        notificationService.createMedicalRecordNotification(event.getPatient(), event.getMedicalRecord(), event.getTime());
    }

    @EventListener
    public void handleMedicineReady(MedicineReadyEvent event) {
        notificationService.createMedicinesNotification(event.getPatient(), event.getMedicalRecord(), event.getTime());
    }

    @EventListener
    public void handlePaymentCompleted(PaymentCompletedEvent event) {
        notificationService.createPaymentNotification( event.getPatient(), event.getMedicalRecord(), event.getTime());
    }
}
