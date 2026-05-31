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
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.MedicalRecordService;
import com.vudo.pojo.Payment;
import com.vudo.pojo.PrescribedMedicine;
import com.vudo.pojo.User;
import com.vudo.repositories.MedicalRecordRepository;
import com.vudo.repositories.PaymentRepository;
import com.vudo.events.PaymentCompletedEvent;
import com.vudo.services.PaymentService;
import com.vudo.services.payment.PaymentFactory;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private Environment env;

    @Autowired
    private MedicalRecordRepository medicalRecordRepo;
    @Autowired
    private PaymentRepository paymentRepo;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Map<String, String> createVNPayPaymentUrl(CreatePaymentRequestDTO request, String clientIp) {
        return paymentFactory.getHandler("vnpay").createPaymentUrl(request, clientIp);
    }

    @Override
    @Transactional
    public Map<String, String> handleVNPayCallback(Map<String, String> vnpParams) {
        String secureHash = vnpParams.get("vnp_SecureHash");
        TreeMap<String, String> filtered = VNPayUtils.filterAndSortVnpParams(vnpParams);
        String hashSecret = env.getProperty("vnpay.hashSecret");
        String hashData = VNPayUtils.buildHashData(filtered);
        String expected = VNPayUtils.hmacSHA512(hashSecret, hashData);

        if (secureHash == null || !secureHash.equalsIgnoreCase(expected)) {
            return Map.of("status", "failed", "message", "Sai chữ ký");
        }

        String txRef = filtered.get("vnp_TxnRef");
        String responseCode = filtered.get("vnp_ResponseCode");
        Payment payment = paymentRepo.getByPaymentCode(txRef);

        if (payment == null) {
            return Map.of("status", "failed", "message", "Không tìm thấy giao dịch");
        }

        if ("00".equals(responseCode)) {
            payment.setStatus("paid");
            MedicalRecord mr = payment.getMedicalRecordId();
            User patient = mr.getPatientId();
            String currentTime = new java.util.Date().toString();
            eventPublisher.publishEvent(new PaymentCompletedEvent(patient, mr, currentTime));
        } else {
            payment.setStatus("failed");
        }
        paymentRepo.update(payment);

        return Map.of("status", payment.getStatus(), "paymentCode", txRef, "responseCode", responseCode);
    }

    private BigDecimal calculateTotalAmount(MedicalRecord medicalRecord) {
        BigDecimal total = BigDecimal.ZERO;

        for (MedicalRecordService item : medicalRecord.getMedicalRecordServiceSet()) {
            total = total.add(
                    item.getPriceAtTime().multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    )
            );
        }

        for (PrescribedMedicine item : medicalRecord.getPrescribedMedicineSet()) {
            total = total.add(
                    item.getPriceAtTime().multiply(
                            BigDecimal.valueOf(item.getQuantity())
                    )
            );
        }
        System.out.println(total);
        return total;
    }

    @Override
    @Transactional
    public Map<String, String> createMomoPaymentUrl(CreatePaymentRequestDTO request) {
        return paymentFactory.getHandler("momo").createPaymentUrl(request, null);
    }

    @Override
    @Transactional
    public Map<String, String> handleMomoIpn(Map<String, Object> body) {
        String orderId = String.valueOf(body.get("orderId"));
        String resultCode = String.valueOf(body.get("resultCode"));

        Payment payment = paymentRepo.getByPaymentCode(orderId);

        if (payment == null) {
            return Map.of("message", "Không tìm thấy giao dịch");
        }

        if ("0".equals(resultCode)) {
            payment.setStatus("paid");

            MedicalRecord mr = payment.getMedicalRecordId();
            User patient = mr.getPatientId();

            String currentTime = new Date().toString();

            eventPublisher.publishEvent(new PaymentCompletedEvent(patient, mr, currentTime));
        } else {
            payment.setStatus("failed");
        }

        paymentRepo.update(payment);

        return Map.of(
                "message", "success",
                "paymentCode", orderId,
                "status", payment.getStatus()
        );
    }

    @Override
    public Map<String, String> handleMomoReturn(Map<String, String> params) {
        return paymentFactory.getHandler("momo").handleReturn(params);
    }
}
