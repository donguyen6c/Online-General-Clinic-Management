/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.Notification;
import com.vudo.repositories.NotificationRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Notification> cq = cb.createQuery(Notification.class);
        Root<Notification> root = cq.from(Notification.class);
        cq.select(root);

        cq.where( cb.equal(root.get("userId").get("id"), userId));
        cq.orderBy( cb.desc(root.get("createdAt")));

        Query<Notification> query = session.createQuery(cq);

        int pageSize = this.env.getProperty("notifications_page_size", Integer.class);
        int start = (page - 1) * pageSize;

        query.setFirstResult(start);
        query.setMaxResults(pageSize);

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
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Notification> root = cq.from(Notification.class);

        cq.select(cb.count(root));

        cq.where( cb.and( cb.equal(root.get("userId").get("id"), userId), cb.isFalse(root.get("isRead"))));
        return session.createQuery(cq).getSingleResult();
    }
}
