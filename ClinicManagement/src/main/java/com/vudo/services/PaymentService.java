/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services;

import com.vudo.dto.VNPayCreatePaymentRequestDTO;
import java.util.Map;

/**
 *
 * @author ADMIN
 */
public interface PaymentService {
    Map<String, String> createVNPayPaymentUrl(VNPayCreatePaymentRequestDTO request, String clientIp);
    Map<String, String> handleVNPayCallback(Map<String, String> vnpParams);
}