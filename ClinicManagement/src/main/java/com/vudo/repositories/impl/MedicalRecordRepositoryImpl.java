/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.MedicalRecord;
import com.vudo.repositories.MedicalRecordRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Repository
@Transactional
public class MedicalRecordRepositoryImpl implements MedicalRecordRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<MedicalRecord> getByPatientId(int patientId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<MedicalRecord> cq = cb.createQuery(MedicalRecord.class);
        Root<MedicalRecord> root = cq.from(MedicalRecord.class);
        
        cq.select(root).where(cb.equal(root.get("patientId").get("id"), patientId));
        
        cq.orderBy(cb.desc(root.get("createdAt")));
        return session.createQuery(cq).getResultList();
    }

    @Override
    public MedicalRecord getMedicalRecordById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(MedicalRecord.class, id);
    }

    @Override
    public MedicalRecord create(MedicalRecord medicalRecord) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(medicalRecord);
        return medicalRecord;
    }
    
}
