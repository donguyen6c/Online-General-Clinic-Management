/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.Disease;
import com.vudo.repositories.DiseaseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADMIN
 */
@Repository
@Transactional
public class DiseaseRepositoryImpl implements DiseaseRepository {

    @Autowired
    private Environment env;
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Set<Disease> getAllById(Set<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return new HashSet<>();
        }

        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Disease> q = b.createQuery(Disease.class);
        Root<Disease> root = q.from(Disease.class);

        q.select(root);

        Predicate p = root.get("id").in(ids);
        q.where(p);

        List<Disease> list = session.createQuery(q).getResultList();

        return new HashSet<>(list);
    }

    @Override
    public List<Disease> getAllDiseases(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
    CriteriaBuilder b = session.getCriteriaBuilder();
    CriteriaQuery<Disease> q = b.createQuery(Disease.class);
    Root<Disease> root = q.from(Disease.class);
    q.select(root);

    if (params != null) {
        String kw = params.get("kw");
        if (kw != null && !kw.isEmpty()) {
            q.where(b.like(root.get("name"), String.format("%%%s%%", kw)));
        }
    }

    q.orderBy(b.desc(root.get("id")));
    Query<Disease> query = session.createQuery(q);

    if (params != null && !params.containsKey("all")) {
        int pageSize = this.env.getProperty("services_disease", Integer.class);
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);
    }

    return query.getResultList();
    }

    @Override
    public Disease getDiseaseById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Disease.class, id);
    }

    @Override
    public void updateDisease(Disease disease) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(disease);
    }

    @Override
    public void addDisease(Disease disease) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(disease);
    }

    @Override
    public void deleteDisease(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Disease d = this.getDiseaseById(id);
        if (d != null) {
            session.remove(d);
        }
    }

    

}
