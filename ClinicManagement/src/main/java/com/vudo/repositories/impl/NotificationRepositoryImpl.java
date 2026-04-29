/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.Notification;
import com.vudo.repositories.NotificationRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ADMIN
 */

@Repository
public class NotificationRepositoryImpl implements NotificationRepository{
    
    @Autowired
    private Environment env;
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Notification add(Notification notification) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(notification);
        return notification;
    }

    @Override
    public List<Notification> getNotificationsByUserId(int userId, int page) {
    Session session = this.factory.getObject().getCurrentSession();
        Query<Notification> query = session.createQuery(
            "FROM Notification n WHERE n.userId.id = :userId ORDER BY n.createdAt DESC", Notification.class);
        query.setParameter("userId", userId);

        int start = (page - 1) * this.env.getProperty("notifications_page_size", Integer.class);
        query.setFirstResult(start);
        query.setMaxResults(this.env.getProperty("notifications_page_size", Integer.class));

        return query.getResultList();
}

    @Override
    public boolean markAsRead(int notificationId) {
        Session session = this.factory.getObject().getCurrentSession();
        Notification n = session.get(Notification.class, notificationId);
        if (n != null) {
            n.setIsRead(true);
            session.merge(n);
            return true;
        }
        return false;
    }

    @Override
    public Long countUnreadByUserId(int userId) {
        Session session = this.factory.getObject().getCurrentSession();
        Query<Long> query = session.createQuery(
            "SELECT COUNT(n) FROM Notification n WHERE n.userId.id = :userId AND n.isRead = false", Long.class);
        query.setParameter("userId", userId);
        return query.uniqueResult();
    }
}
