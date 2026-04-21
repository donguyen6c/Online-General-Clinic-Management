/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

import java.math.BigDecimal;

/**
 *
 * @author ASUS
 */
public class ServiceResponseDTO {
    private String serviceName;
    private Integer quantity;
    private BigDecimal priceAtTime;

    /**
     * @return the serviceName
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * @param serviceName the serviceName to set
     */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * @param quantity the quantity to set
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * @return the priceAtTime
     */
    public BigDecimal getPriceAtTime() {
        return priceAtTime;
    }

    /**
     * @param priceAtTime the priceAtTime to set
     */
    public void setPriceAtTime(BigDecimal priceAtTime) {
        this.priceAtTime = priceAtTime;
    }
}
