/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.payment;

import com.vudo.dto.CreatePaymentRequestDTO;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.Payment;
import com.vudo.utils.MomoUtils;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author ADMIN
 */
@Service
@PropertySource("classpath:configs.properties")
public class MomoHandler extends PaymentHandler {

    @Autowired
    private Environment env;

    @Override
    public String getMethod() {
        return "momo";
    }

    @Override
    public Map<String, String> createPaymentUrl(CreatePaymentRequestDTO request, String clientIp) {
        MedicalRecord medicalRecord = this.getMedicalRecord(request);
        BigDecimal totalAmount = this.calculateTotalAmount(medicalRecord);

        String partnerCode = env.getProperty("momo.partnerCode");
        String accessKey = env.getProperty("momo.accessKey");
        String secretKey = env.getProperty("momo.secretKey");
        String endpoint = env.getProperty("momo.endpoint");
        String requestType = env.getProperty("momo.requestType");
        String redirectUrl = env.getProperty("momo.redirectUrl");
        String ipnUrl = env.getProperty("momo.ipnUrl");

        String requestId = UUID.randomUUID().toString();
        String orderId = UUID.randomUUID().toString();
        String orderInfo = "Thanh toan hoa don " + medicalRecord.getId();
        String amount = totalAmount.toBigInteger().toString();
        String extraData = "";

        String rawSignature
                = "accessKey=" + accessKey
                + "&amount=" + amount
                + "&extraData=" + extraData
                + "&ipnUrl=" + ipnUrl
                + "&orderId=" + orderId
                + "&orderInfo=" + orderInfo
                + "&partnerCode=" + partnerCode
                + "&redirectUrl=" + redirectUrl
                + "&requestId=" + requestId
                + "&requestType=" + requestType;

        String signature = MomoUtils.hmacSHA256(secretKey, rawSignature);

        Map<String, Object> body = new LinkedHashMap<>();

        body.put("partnerCode", partnerCode);
        body.put("partnerName", "Momo Payment");
        body.put("storeId", "MomoStore");
        body.put("requestId", requestId);
        body.put("amount", amount);
        body.put("orderId", orderId);
        body.put("orderInfo", orderInfo);
        body.put("redirectUrl", redirectUrl);
        body.put("ipnUrl", ipnUrl);
        body.put("lang", "vi");
        body.put("requestType", requestType);
        body.put("autoCapture", true);
        body.put("extraData", extraData);
        body.put("signature", signature);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Map> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, Map.class);

        Map<String, Object> momoResponse = response.getBody();

        if (momoResponse == null || momoResponse.get("payUrl") == null) {
            throw new IllegalStateException("Không tạo được link thanh toán MoMo");
        }

        this.savePendingPayment(medicalRecord, totalAmount, this.getMethod(), orderId);

        Map<String, String> result = new LinkedHashMap<>();
        result.put("paymentUrl", momoResponse.get("payUrl").toString());

        return result;
    }

    @Override
    public Map<String, String> handleIpn(Map<String, Object> body) {
        String orderId = String.valueOf(body.get("orderId"));
        String resultCode = String.valueOf(body.get("resultCode"));

        Payment payment = paymentRepo.getByPaymentCode(orderId);

        if (payment == null) {
            return Map.of("message", "Không tìm thấy giao dịch");
        }

        this.markPaymentResult(payment, "0".equals(resultCode));

        return Map.of(
                "message", "success",
                "paymentCode", orderId,
                "status", payment.getStatus()
        );
    }

    @Override
    public Map<String, String> handleReturn(Map<String, String> params) {
        String orderId = params.get("orderId");
        String resultCode = params.get("resultCode");

        if (orderId == null || resultCode == null) {
            return Map.of(
                    "status", "failed",
                    "paymentCode", "",
                    "responseCode", "99"
            );
        }

        String status = "0".equals(resultCode) ? "paid" : "failed";
        String responseCode = "0".equals(resultCode) ? "00" : resultCode;

        return Map.of(
                "status", status,
                "paymentCode", orderId,
                "responseCode", responseCode
        );
    }
}
