/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.utils;

/**
 *
 * @author ADMIN
 */

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayUtils {

    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKey);
            byte[] bytes = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (byte b : bytes) {
                hash.append(String.format("%02x", b));
            }
            return hash.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Không thể tạo chữ ký VNPay", ex);
        }
    }

    public static String buildHashData(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        params.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                if (sb.length() > 0) {
                    sb.append('&');
                }
                sb.append(key).append('=').append(URLEncoder.encode(value, StandardCharsets.US_ASCII));
            }
        });
        return sb.toString();
    }

    public static TreeMap<String, String> filterAndSortVnpParams(Map<String, String> source) {
        TreeMap<String, String> result = new TreeMap<>();
        source.forEach((key, value) -> {
            if (key.startsWith("vnp_") && !"vnp_SecureHash".equals(key) && !"vnp_SecureHashType".equals(key)) {
                result.put(key, value);
            }
        });
        return result;
    }
}
