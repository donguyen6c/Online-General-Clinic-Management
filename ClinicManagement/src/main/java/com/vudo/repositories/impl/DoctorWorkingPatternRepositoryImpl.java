/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.DoctorWorkingPattern;
import com.vudo.repositories.DoctorWorkingPatternRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
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
public class DoctorWorkingPatternRepositoryImpl implements DoctorWorkingPatternRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<DoctorWorkingPattern> getByDoctorId(int doctorId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<DoctorWorkingPattern> cq = cb.createQuery(DoctorWorkingPattern.class);
        Root<DoctorWorkingPattern> root = cq.from(DoctorWorkingPattern.class);

        cq.select(root).where(cb.equal(root.get("doctorId").get("id"), doctorId))
                .orderBy(cb.asc(root.get("dayOfWeek")));

        Query<DoctorWorkingPattern> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public DoctorWorkingPattern getByDoctorIdAndDayOfWeek(int doctorId, int dayOfWeek) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<DoctorWorkingPattern> cq = cb.createQuery(DoctorWorkingPattern.class);
        Root<DoctorWorkingPattern> root = cq.from(DoctorWorkingPattern.class);

        cq.select(root).where(
                cb.and(
                        cb.equal(root.get("doctorId").get("id"), doctorId),
                        cb.equal(root.get("dayOfWeek"), dayOfWeek)
                )
        );

        Query<DoctorWorkingPattern> query = session.createQuery(cq);
        List<DoctorWorkingPattern> results = query.getResultList();

        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    @Override
    public DoctorWorkingPattern getById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(DoctorWorkingPattern.class, id);
    }

    @Override
    public void add(DoctorWorkingPattern pattern) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(pattern);
    }

    @Override
    public void update(DoctorWorkingPattern pattern) {
        Session session = this.factory.getObject().getCurrentSession();
        session.merge(pattern);
    }

    @Override
    public void delete(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        DoctorWorkingPattern pattern = session.get(DoctorWorkingPattern.class, id);

        if (pattern != null) {
            session.remove(pattern);
        }
    }

}
