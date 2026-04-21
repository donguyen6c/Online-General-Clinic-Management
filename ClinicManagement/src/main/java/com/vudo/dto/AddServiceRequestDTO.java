/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.dto;

import java.util.List;

/**
 *
 * @author ASUS
 */
public class AddServiceRequestDTO {
    private List<ServiceItemDTO> services;

    /**
     * @return the services
     */
    public List<ServiceItemDTO> getServices() {
        return services;
    }

    /**
     * @param services the services to set
     */
    public void setServices(List<ServiceItemDTO> services) {
        this.services = services;
    }
}
