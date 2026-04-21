/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.User;

/**
 *
 * @author ADMIN
 */
public interface NotificationService {
    void createBookingNotification(User user, String doctorName, String time);
    void createMedicinesNotification(User user, MedicalRecord id, String time);
}
