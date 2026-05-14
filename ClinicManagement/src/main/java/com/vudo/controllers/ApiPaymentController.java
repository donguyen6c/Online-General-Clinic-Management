/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.CreatePaymentRequestDTO;
import com.vudo.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api/secure/payments")
public class ApiPaymentController {

    @Autowired
    private Environment env;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/vnpay/create")
    public ResponseEntity<?> createPayment(@RequestBody CreatePaymentRequestDTO request, HttpServletRequest httpRequest) {
        try {
            return ResponseEntity.ok(paymentService.createVNPayPaymentUrl(request, httpRequest.getRemoteAddr()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/vnpay/callback")
    public ResponseEntity<?> paymentCallback(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();

        Enumeration<String> en = request.getParameterNames();
        while (en.hasMoreElements()) {
            String name = en.nextElement();
            params.put(name, request.getParameter(name));
        }

        Map<String, String> result = paymentService.handleVNPayCallback(params);

        String status = result.get("status");
        String paymentCode = result.get("paymentCode");
        String responseCode = result.get("responseCode");

        String redirectUrl = env.getProperty("urlFE")
                + "/payment-result?status=" + status
                + "&paymentCode=" + paymentCode
                + "&responseCode=" + responseCode;

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

    @PostMapping("/momo/create")
    public ResponseEntity<?> createMomoPayment(@RequestBody CreatePaymentRequestDTO request) {
        try {
            return ResponseEntity.ok(paymentService.createMomoPaymentUrl(request));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @PostMapping("/momo/ipn")
    public ResponseEntity<?> momoIpn(@RequestBody Map<String, Object> body) {
        return ResponseEntity.ok(paymentService.handleMomoIpn(body));
    }

    @GetMapping("/momo-return")
    public ResponseEntity<?> momoReturn(@RequestParam Map<String, String> params) {

        Map<String, String> result = paymentService.handleMomoReturn(params);

        String redirectUrl = String.format(
                "%s/payment-result?status=%s&paymentCode=%s&responseCode=%s",
                env.getProperty("urlFE"),
                result.get("status"),
                result.get("paymentCode"),
                result.get("responseCode")
        );

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }

}
