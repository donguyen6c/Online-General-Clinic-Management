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
import java.util.Map;
import java.util.Set;
import org.hibernate.query.Query;
import org.springframework.core.env.Environment;
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
    private Environment env;
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Service> getAllServices(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Service> q = b.createQuery(Service.class);
        Root<Service> root = q.from(Service.class);
        q.select(root);

        if (params != null) {
            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                q.where(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }
        }

        q.orderBy(b.desc(root.get("id")));
        Query<Service> query = session.createQuery(q);

        // Phân trang
        if (params != null && !params.containsKey("all")) {
            int pageSize = this.env.getProperty("services_disease", Integer.class);
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            query.setMaxResults(pageSize);
            query.setFirstResult((page - 1) * pageSize);
        }

        return query.getResultList();
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

    @Override
    public void deleteService(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Service d = this.getServiceById(id);
        if (d != null) {
            session.remove(d);
        }
    }
}
