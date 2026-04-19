/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.pojo.Notification;
import com.vudo.pojo.User;
import com.vudo.repositories.NotificationRepository;
import com.vudo.services.NotificationService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class NotificationServiceImpl implements NotificationService{
    
    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void createBookingNotification(User user, String doctorName, String time) {
        Notification n = new Notification();
        n.setTitle("Đặt lịch thành công");
        n.setMessage("Bạn đã đặt lịch với bác sĩ " + doctorName + " lúc " + time);
        n.setType("BOOKING");
        n.setIsRead(false);
        n.setCreatedAt(new Date());
        n.setUserId(user);

        notificationRepository.add(n);
    }
    
}
