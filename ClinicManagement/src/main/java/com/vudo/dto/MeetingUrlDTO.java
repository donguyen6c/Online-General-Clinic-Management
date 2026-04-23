/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

/**
 *
 * @author ASUS
 */
public class MeetingUrlDTO {
    private String meetingUrl;

    // Constructor mặc định
    public MeetingUrlDTO() {
    }

    public MeetingUrlDTO(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }
    
    /**
     * @return the meetingUrl
     */
    public String getMeetingUrl() {
        return meetingUrl;
    }

    /**
     * @param meetingUrl the meetingUrl to set
     */
    public void setMeetingUrl(String meetingUrl) {
        this.meetingUrl = meetingUrl;
    }
}
