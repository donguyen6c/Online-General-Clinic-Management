/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.pojo.Specialty;
import com.vudo.repositories.SpecialtyRepository;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Repository
@Transactional
public class SpecialtyRepositoryImpl implements SpecialtyRepository{
    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Specialty> getSpec() {
        Session session = this.factory.getObject().getCurrentSession();
        Query query = session.createQuery("FROM Specialty", Specialty.class);
        return query.getResultList();
    }
    
}
