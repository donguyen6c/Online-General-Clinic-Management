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
import java.util.Set;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
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

}
