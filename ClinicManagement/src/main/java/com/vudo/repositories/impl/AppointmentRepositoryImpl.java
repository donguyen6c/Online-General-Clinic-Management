/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.Appointment;
import com.vudo.repositories.AppointmentRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.time.LocalDate;
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
public class AppointmentRepositoryImpl implements AppointmentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Appointment> getByDoctorIdAndDate(int doctorId, LocalDate appointmentDate) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        cq.select(root).where(
                cb.and(
                        cb.equal(root.get("doctorId").get("id"), doctorId),
                        cb.equal(root.get("appointmentDate"), appointmentDate),
                        cb.notEqual(root.get("status"), "cancelled")
                )
        );

        return session.createQuery(cq).getResultList();
    }

    @Override
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Appointment> cq = cb.createQuery(Appointment.class);
        Root<Appointment> root = cq.from(Appointment.class);

        cq.where(cb.equal(root.get("patientId").get("id"), patientId));

        cq.orderBy(cb.desc(root.get("appointmentDate")), cb.desc(root.get("startTime")));

        Query<Appointment> query = session.createQuery(cq);
        return query.getResultList();
    }

    @Override
    public Appointment add(Appointment appointment) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(appointment);
        return appointment;
    }

}
