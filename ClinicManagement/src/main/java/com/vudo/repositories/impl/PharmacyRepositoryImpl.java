/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.dto.MedicineDTO;
import com.vudo.mapper.MedicineMapper;
import com.vudo.pojo.Inventory;
import com.vudo.pojo.Medicine;
import com.vudo.repositories.PharmacyRepository;
import com.vudo.repositories.UserRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class PharmacyRepositoryImpl implements PharmacyRepository{
    
    @Autowired
    private LocalSessionFactoryBean factory;
    
    @Autowired
    private UserRepository userRepo;

    @Override
    public List<MedicineDTO> getMedicines(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        
        CriteriaQuery<Object[]> q = b.createQuery(Object[].class);             
        Root<Inventory> root = q.from(Inventory.class);       
        Join<Inventory, Medicine> join = root.join("medicineId", JoinType.INNER);
        
        q.multiselect(
                join.get("id"),
                join.get("name"),
                join.get("unit"),
                join.get("price"),
                root.get("id"), 
                root.get("quantity"),
                root.get("expiryDate")
        );
        
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String kw = params.get("kw");
            
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(join.get("name"), String.format("%%%s%%", kw)));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }
        }

        Query<Object[]> query = session.createQuery(q);
        List<Object[]> results = query.getResultList();

        // 6. Map mảng Object[] sang MedicineDTO
        List<MedicineDTO> dtos = new ArrayList<>();
        for (Object[] row : results) {
            MedicineDTO dto = new MedicineDTO();
            dto.setId((Integer) row[0]);
            dto.setName((String) row[1]);
            dto.setUnit((String) row[2]);
            dto.setPrice((BigDecimal) row[3]);
            dto.setInventoryId((Integer) row[4]);
            dto.setQuantity((Integer) row[5]);
            dto.setExpiryDate((Date) row[6]);
            
            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public boolean addMedicine(MedicineDTO dto) {
        Session session = this.factory.getObject().getCurrentSession();
        try {
            Medicine m = MedicineMapper.toMedicine(dto);
            session.persist(m);

            Inventory inv = MedicineMapper.toInventory(dto, m);
            session.persist(inv);

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateMedicine(int medicineId, MedicineDTO dto) {
        Session session = this.factory.getObject().getCurrentSession();
        try {
            Medicine m = session.get(Medicine.class, medicineId);
            if (m != null) {
                MedicineMapper.updateMedicineFromDTO(m, dto);
                session.merge(m);
                
                if (dto.getInventoryId() != null) {
                    Inventory inv = session.get(Inventory.class, dto.getInventoryId());
                    if (inv != null) {
                        MedicineMapper.updateInventoryFromDTO(inv, dto);
                        session.merge(inv);
                    }
                }
                return true;
            }
            return false;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
    
}
