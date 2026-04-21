/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.Service;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import com.vudo.repositories.ServicesRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Repository
@Transactional
public class ServicesRepositoryImpl implements ServicesRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Service> getAllServices() {
        Session session = this.factory.getObject().getCurrentSession();
        return session.createQuery("FROM Service", Service.class).getResultList();
    }

    @Override
    public Service getServiceById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Service.class, id);
    }

    @Override
    public void updateService(Service service) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(service);
    }

    @Override
    public void addService(Service service) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(service);
    }

    @Override
    public List<Service> getAllByIds(Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
        return new java.util.ArrayList<>();}

        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<com.vudo.pojo.Service> q = b.createQuery(Service.class);
        Root<Service> root = q.from(Service.class);

        q.select(root).where(root.get("id").in(ids));

        return session.createQuery(q).getResultList();
    }

}
