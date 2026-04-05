/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.DoctorSchedule;
import com.vudo.repositories.DoctorScheduleRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;
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
public class DoctorScheduleRepositoryImpl implements DoctorScheduleRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public DoctorSchedule getByDoctorIdAndWorkDate(int doctorId, LocalDate workDate) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<DoctorSchedule> cq = cb.createQuery(DoctorSchedule.class);
        Root<DoctorSchedule> root = cq.from(DoctorSchedule.class);

        cq.select(root).where(
                cb.and(
                        cb.equal(root.get("doctorId").get("id"), doctorId),
                        cb.equal(root.get("workDate"), workDate)
                )
        );

        List<DoctorSchedule> results = session.createQuery(cq).getResultList();

        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

}
