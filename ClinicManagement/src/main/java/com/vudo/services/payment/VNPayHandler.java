/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.payment;

import com.vudo.dto.CreatePaymentRequestDTO;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.Payment;
import com.vudo.utils.VNPayUtils;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
@PropertySource("classpath:configs.properties")
public class VNPayHandler extends PaymentHandler {

    @Autowired
    private Environment env;

    @Override
    public String getMethod() {
        return "vnpay";
    }

    @Override
    public Map<String, String> createPaymentUrl(CreatePaymentRequestDTO request, String clientIp) {
        MedicalRecord medicalRecord = this.getMedicalRecord(request);

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

        this.savePendingPayment(medicalRecord, totalAmount, this.getMethod(), txRef);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("paymentUrl", paymentUrl);
        return response;
    }

    @Override
    public Map<String, String> handleReturn(Map<String, String> vnpParams) {
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

        this.markPaymentResult(payment, "00".equals(responseCode));

        return Map.of("status", payment.getStatus(), "paymentCode", txRef, "responseCode", responseCode);
    }
}
