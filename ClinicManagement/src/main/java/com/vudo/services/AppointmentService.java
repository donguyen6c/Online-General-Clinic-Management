/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.AvailableSlotsResponseDTO;

/**
 *
 * @author ADMIN
 */
public interface AppointmentService {
    AvailableSlotsResponseDTO getAvailableSlots(int doctorId, String date);
}
