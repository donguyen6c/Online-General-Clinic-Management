/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.Doctor;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.Specialty;
import com.vudo.pojo.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import com.vudo.repositories.StatisticRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author ADMIN
 */
@Repository
public class StatisticRepositoryImpl implements StatisticRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    private Session getCurrentSession() {
        return factory.getObject().getCurrentSession();
    }

    @Override
    public List<Object[]> countPatientsByGender() {
        Session session = getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<User> root = cq.from(User.class);

        cq.multiselect(
                root.get("gender"),
                cb.count(root.get("id"))
        );

        cq.where(
                cb.equal(root.get("role"), "PATIENT")
        );

        cq.groupBy(root.get("gender"));

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<Object[]> countPatientsByAgeGroup() {
        Session session = factory.getObject().getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<User> root = cq.from(User.class);

        Expression<Integer> age = cb.function(
                "TIMESTAMPDIFF",
                Integer.class,
                cb.literal("YEAR"),
                root.get("dateOfBirth"),
                cb.currentDate()
        );

        Expression<String> ageGroup = cb.<String>selectCase()
                .when(cb.lessThan(age, 18), "Dưới 18 tuổi")
                .when(cb.between(age, 18, 30), "18 - 30 tuổi")
                .when(cb.between(age, 31, 45), "31 - 45 tuổi")
                .when(cb.between(age, 46, 60), "46 - 60 tuổi")
                .otherwise("Trên 60 tuổi");

        cq.multiselect(
                ageGroup,
                cb.count(root.get("id"))
        );

        cq.where(
                cb.equal(root.get("role"), "PATIENT"),
                cb.isNotNull(root.get("dateOfBirth"))
        );

        cq.groupBy(ageGroup);

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<Object[]> countPatientsBySpecialty() {
        Session session = getCurrentSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Object[]> cq = cb.createQuery(Object[].class);

        Root<MedicalRecord> root = cq.from(MedicalRecord.class);

        Join<MedicalRecord, User> patientJoin = root.join("patient", JoinType.INNER);
        Join<MedicalRecord, Doctor> doctorJoin = root.join("doctor", JoinType.INNER);
        Join<Doctor, Specialty> specialtyJoin = doctorJoin.join("specialty", JoinType.LEFT);

        cq.multiselect(
                specialtyJoin.get("name"),
                cb.countDistinct(patientJoin.get("id"))
        );

        cq.where(
                cb.equal(patientJoin.get("role"), "PATIENT")
        );

        cq.groupBy(specialtyJoin.get("name"));

        return session.createQuery(cq).getResultList();
    }
}
