/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.Notification;
import com.vudo.repositories.NotificationRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ADMIN
 */

@Repository
public class NotificationRepositoryImpl implements NotificationRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Notification add(Notification notification) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(notification);
        return notification;
    }
    
}
