/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.NotificationDTO;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.Notification;
import com.vudo.pojo.User;
import com.vudo.repositories.NotificationRepository;
import com.vudo.services.NotificationService;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Service
@Transactional
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

    @Override
    public void createMedicinesNotification(User user, MedicalRecord record, String time) {
        Notification n = new Notification();
        n.setTitle("Đơn thuốc đã sẵn sàng");
        n.setMessage("Thuốc của hồ sơ bệnh án #" + record.getId() + " đã được chuẩn bị xong lúc " + time + ". Vui lòng đến quầy nhận thuốc.");
        n.setType("MEDICINE"); 
        n.setIsRead(false);
        n.setCreatedAt(new Date());
        n.setUserId(user);

        notificationRepository.add(n);
    }

    @Override
    public List<NotificationDTO> getUserNotifications(int userId, int page) {
        List<Notification> notifications = notificationRepository.getNotificationsByUserId(userId, page);

        return notifications.stream().map(n -> {
            NotificationDTO dto = new NotificationDTO();
            dto.setId(n.getId());
            dto.setTitle(n.getTitle());
            dto.setMessage(n.getMessage());
            dto.setType(n.getType());
            dto.setIsRead(n.getIsRead());
            dto.setCreatedAt(n.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean markNotificationAsRead(int notificationId) {
        return notificationRepository.markAsRead(notificationId);
    }    

    @Override
    public Long countUnreadByUserId(int userId) {
       return notificationRepository.countUnreadByUserId(userId);
    }
}
