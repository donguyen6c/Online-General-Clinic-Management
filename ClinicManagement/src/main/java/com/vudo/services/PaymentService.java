/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.CreatePaymentRequestDTO;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface PaymentService {
    Map<String, String> createVNPayPaymentUrl(CreatePaymentRequestDTO request, String clientIp);
    Map<String, String> handleVNPayCallback(Map<String, String> vnpParams);
    Map<String, String> createMomoPaymentUrl(CreatePaymentRequestDTO request);
    Map<String, String> handleMomoIpn(Map<String, Object> body);
    Map<String, String> handleMomoReturn(Map<String, String> params);
}
