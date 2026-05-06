/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

/**
 *
 * @author ADMIN
 */
import com.vudo.pojo.Payment;
import com.vudo.repositories.PaymentRepository;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PaymentRepositoryImpl implements PaymentRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public Payment add(Payment payment) {
        Session session = this.factory.getObject().getCurrentSession();
        session.persist(payment);
        return payment;
    }

    @Override
    public Payment getByPaymentCode(String paymentCode) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.createQuery("FROM Payment p WHERE p.paymentCode = :paymentCode", Payment.class)
                .setParameter("paymentCode", paymentCode)
                .uniqueResult();
    }

    @Override
    public Payment update(Payment payment) {
        Session session = this.factory.getObject().getCurrentSession();
        return (Payment) session.merge(payment);
    }
}