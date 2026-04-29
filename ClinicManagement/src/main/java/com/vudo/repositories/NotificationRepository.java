/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.Notification;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public interface NotificationRepository {
    Notification add(Notification notification);
    List<Notification> getNotificationsByUserId(int userId, int page);
    boolean markAsRead(int notificationId);
    Long countUnreadByUserId(int userId);
}
