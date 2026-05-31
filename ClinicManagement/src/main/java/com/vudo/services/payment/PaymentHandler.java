/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.payment;

import com.vudo.dto.CreatePaymentRequestDTO;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.MedicalRecordService;
import com.vudo.pojo.Payment;
import com.vudo.pojo.PrescribedMedicine;
import com.vudo.pojo.User;
import com.vudo.repositories.MedicalRecordRepository;
import com.vudo.repositories.PaymentRepository;
import com.vudo.events.PaymentCompletedEvent;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

/**
 *
 * @author ADMIN
 */
public abstract class PaymentHandler {

    @Autowired
    protected MedicalRecordRepository medicalRecordRepo;

    @Autowired
    protected PaymentRepository paymentRepo;

    @Autowired
    protected ApplicationEventPublisher eventPublisher;

    public abstract String getMethod();

    public abstract Map<String, String> createPaymentUrl(CreatePaymentRequestDTO request, String clientIp);

    public Map<String, String> handleReturn(Map<String, String> params) {
        throw new UnsupportedOperationException("Phương thức thanh toán không hỗ trợ callback này");
    }

    public Map<String, String> handleIpn(Map<String, Object> body) {
        throw new UnsupportedOperationException("Phương thức thanh toán không hỗ trợ IPN");
    }

    protected MedicalRecord getMedicalRecord(CreatePaymentRequestDTO request) {
        if (request.getMedicalRecordId() == null) {
            throw new IllegalArgumentException("Thiếu thông tin thanh toán");
        }

        MedicalRecord medicalRecord = medicalRecordRepo.getMedicalRecordById(request.getMedicalRecordId());
        if (medicalRecord == null) {
            throw new IllegalArgumentException("Không tìm thấy hồ sơ bệnh án");
        }

        Set<Payment> payments = medicalRecord.getPaymentSet();
        if (payments != null && !payments.isEmpty()) {
            for (Payment p : payments) {
                if ("paid".equals(p.getStatus())) {
                    throw new IllegalArgumentException("Đã thanh toán rồi!");
                }
            }
        }

        return medicalRecord;
    }

    protected BigDecimal calculateTotalAmount(MedicalRecord medicalRecord) {
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

    protected void savePendingPayment(MedicalRecord medicalRecord, BigDecimal totalAmount, String method, String paymentCode) {
        Payment payment = new Payment();
        payment.setMedicalRecordId(medicalRecord);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(method);
        payment.setPaymentCode(paymentCode);
        payment.setStatus("pending");
        payment.setCreatedAt(new Date());

        paymentRepo.add(payment);
    }

    protected void markPaymentResult(Payment payment, boolean success) {
        if (success) {
            payment.setStatus("paid");
            MedicalRecord mr = payment.getMedicalRecordId();
            User patient = mr.getPatientId();
            String currentTime = new Date().toString();
            eventPublisher.publishEvent(new PaymentCompletedEvent(patient, mr, currentTime));
        } else {
            payment.setStatus("failed");
        }

        paymentRepo.update(payment);
    }
}
