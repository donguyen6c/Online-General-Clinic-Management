/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

/**
 *
 * @author ADMIN
 */
import com.vudo.dto.VNPayCreatePaymentRequestDTO;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.MedicalRecordService;
import com.vudo.pojo.Payment;
import com.vudo.pojo.PrescribedMedicine;
import com.vudo.repositories.MedicalRecordRepository;
import com.vudo.repositories.PaymentRepository;
import com.vudo.services.PaymentService;
import com.vudo.utils.VNPayUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    @Override
    @Transactional
    public Map<String, String> createVNPayPaymentUrl(VNPayCreatePaymentRequestDTO request, String clientIp) {
        if (request.getMedicalRecordId() == null) {
            throw new IllegalArgumentException("Thiếu thông tin thanh toán");
        }

        MedicalRecord medicalRecord = medicalRecordRepo.getMedicalRecordById(request.getMedicalRecordId());
        if (medicalRecord == null) {
            throw new IllegalArgumentException("Không tìm thấy hồ sơ bệnh án");
        }

        String tmnCode = env.getProperty("vnpay.tmnCode");
        String hashSecret = env.getProperty("vnpay.hashSecret");
        String payUrl = env.getProperty("vnpay.payUrl");
        String returnUrl = env.getProperty("vnpay.returnUrl");

        String txRef = UUID.randomUUID().toString().replace("-", "").substring(0, 20);
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        String createDate = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String expireDate = now.plusMinutes(15).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        BigDecimal totalAmount = this.calculateTotalAmount(medicalRecord);

        TreeMap<String, String> params = new TreeMap<>();
        params.put("vnp_Version", "2.1.0");
        params.put("vnp_Command", "pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", totalAmount.multiply(BigDecimal.valueOf(100)).toBigInteger().toString());
        params.put("vnp_CurrCode", "VND");
        params.put("vnp_TxnRef", txRef);
        params.put("vnp_OrderInfo", "Thanh toan hoa don" + medicalRecord.getId());
        params.put("vnp_OrderType", "other");
        params.put("vnp_Locale", "vn");
        params.put("vnp_IpAddr", (clientIp == null || clientIp.isBlank()) ? "127.0.0.1" : clientIp);
        params.put("vnp_CreateDate", createDate);
        params.put("vnp_ExpireDate", expireDate);
        params.put("vnp_ReturnUrl", returnUrl);

        String hashData = VNPayUtils.buildHashData(params);
        String secureHash = VNPayUtils.hmacSHA512(hashSecret, hashData);
        String paymentUrl = payUrl + "?" + hashData + "&vnp_SecureHash=" + secureHash;

        Payment payment = new Payment();
        payment.setMedicalRecordId(medicalRecord);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod("vnpay");
        payment.setPaymentCode(txRef);
        payment.setStatus("pending");
        payment.setCreatedAt(new Date());
        paymentRepo.add(payment);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("paymentUrl", paymentUrl);
        response.put("paymentCode", txRef);
        return response;
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
}
