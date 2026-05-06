/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.Payment;

/**
 *
 * @author ADMIN
 */
public interface PaymentRepository {
    Payment add(Payment payment);
    Payment getByPaymentCode(String paymentCode);
    Payment update(Payment payment);
}