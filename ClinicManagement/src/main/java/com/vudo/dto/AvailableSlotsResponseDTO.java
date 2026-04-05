/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

import java.util.List;

/**
 *
 * @author ADMIN
 */
public class AvailableSlotsResponseDTO {
    private Integer doctorId;
    private String date;
    private boolean available;
    private WorkingTimeDTO workingTime;
    private List<TimeSlotDTO> availableSlots;

    /**
     * @return the doctorId
     */
    public Integer getDoctorId() {
        return doctorId;
    }

    /**
     * @param doctorId the doctorId to set
     */
    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
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
     * @return the available
     */
    public boolean isAvailable() {
        return available;
    }

    /**
     * @param available the available to set
     */
    public void setAvailable(boolean available) {
        this.available = available;
    }

    /**
     * @return the workingTime
     */
    public WorkingTimeDTO getWorkingTime() {
        return workingTime;
    }

    /**
     * @param workingTime the workingTime to set
     */
    public void setWorkingTime(WorkingTimeDTO workingTime) {
        this.workingTime = workingTime;
    }

    /**
     * @return the availableSlots
     */
    public List<TimeSlotDTO> getAvailableSlots() {
        return availableSlots;
    }

    /**
     * @param availableSlots the availableSlots to set
     */
    public void setAvailableSlots(List<TimeSlotDTO> availableSlots) {
        this.availableSlots = availableSlots;
    }
    
    
}
