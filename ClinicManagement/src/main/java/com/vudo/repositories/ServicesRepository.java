/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.Service;
import java.util.List;
import java.util.Set;

/**
 *
 * @author ASUS
 */
public interface ServicesRepository {
    List<Service> getAllByIds(Set<Integer> ids);
    List<Service> getAllServices();
    Service getServiceById(int id);
    void updateService(Service service);
    void addService(Service service);
}
