/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

/**
 *
 * @author ADMIN
 */
import com.vudo.dto.CreatePaymentRequestDTO;
import com.vudo.services.PaymentService;
import com.vudo.services.payment.PaymentFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentFactory paymentFactory;

    @Override
    @Transactional
    public Map<String, String> createVNPayPaymentUrl(CreatePaymentRequestDTO request, String clientIp) {
        return paymentFactory.getHandler("vnpay").createPaymentUrl(request, clientIp);
    }

    @Override
    @Transactional
    public Map<String, String> handleVNPayCallback(Map<String, String> vnpParams) {
        return paymentFactory.getHandler("vnpay").handleReturn(vnpParams);
    }

    @Override
    @Transactional
    public Map<String, String> createMomoPaymentUrl(CreatePaymentRequestDTO request) {
        return paymentFactory.getHandler("momo").createPaymentUrl(request, null);
    }

    @Override
    @Transactional
    public Map<String, String> handleMomoIpn(Map<String, Object> body) {
        return paymentFactory.getHandler("momo").handleIpn(body);
    }

    @Override
    public Map<String, String> handleMomoReturn(Map<String, String> params) {
        return paymentFactory.getHandler("momo").handleReturn(params);
    }
}
