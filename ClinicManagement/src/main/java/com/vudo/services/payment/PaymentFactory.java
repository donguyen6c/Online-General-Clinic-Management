/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.payment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class PaymentFactory {

    private final Map<String, PaymentHandler> handlers = new HashMap<>();

    @Autowired
    public PaymentFactory(List<PaymentHandler> handlers) {
        for (PaymentHandler handler : handlers) {
            this.handlers.put(handler.getMethod().toLowerCase(), handler);
        }
    }

    public PaymentHandler getHandler(String method) {
        PaymentHandler handler = handlers.get(method.toLowerCase());
        if (handler == null) {
            throw new IllegalArgumentException("Phương thức thanh toán không hợp lệ");
        }

        return handler;
    }
}
