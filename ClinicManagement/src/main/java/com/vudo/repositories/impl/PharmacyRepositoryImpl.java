/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.repositories.impl;

import com.vudo.dto.InventoryDTO;
import com.vudo.dto.MedicineDTO;
import com.vudo.mapper.InventoryMapper;
import com.vudo.mapper.MedicineMapper;
import com.vudo.pojo.Inventory;
import com.vudo.pojo.Medicine;
import com.vudo.pojo.Notification;
import com.vudo.pojo.PrescribedMedicine;
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
import java.util.Calendar;
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
        
        CriteriaQuery<Medicine> q = b.createQuery(Medicine.class);
        Root<Medicine> root = q.from(Medicine.class);
        
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String kw = params.get("kw");
            
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("name"), String.format("%%%s%%", kw)));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }
        }

        Query<Medicine> query = session.createQuery(q);
        List<Medicine> results = query.getResultList();

        List<MedicineDTO> dtos = new ArrayList<>();
        for (Medicine m : results) {
            dtos.add(MedicineMapper.toDTO(m));
        }

        return dtos;
    }

    @Override
    public List<InventoryDTO> getInventories(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Inventory> q = b.createQuery(Inventory.class);
        Root<Inventory> root = q.from(Inventory.class);
        
        root.fetch("medicineId", JoinType.INNER); 
        
        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();
            String kw = params.get("kw");
            
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("medicineId").get("name"), String.format("%%%s%%", kw)));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(new Predicate[0]));
            }
        }

        q.orderBy(b.asc(root.get("expiryDate")));

        List<Inventory> results = session.createQuery(q).getResultList();

        List<InventoryDTO> dtos = new ArrayList<>();
        for (Inventory inv : results) {
            InventoryDTO invDto = InventoryMapper.toDTO(inv); 
            if (invDto != null) {
                invDto.setCreatedAt(inv.getCreatedAt());
                dtos.add(invDto);
            }
        }
        return dtos;
    }
    
    @Override
    public void addMedicine(MedicineDTO dto) {
        Session session = this.factory.getObject().getCurrentSession();
        Medicine m = MedicineMapper.toMedicine(dto);
        session.persist(m);
    }

    @Override
    public void updateMedicine(int medicineId, MedicineDTO dto) {
        Session session = this.factory.getObject().getCurrentSession();
        Medicine m = session.get(Medicine.class, medicineId);
        if (m != null) {
            MedicineMapper.updateMedicineFromDTO(m, dto);
            session.merge(m);
        }
    }

    @Override
    public void deleteMedicine(int medicineId) {
        Session session = this.factory.getObject().getCurrentSession();
        Medicine m = session.get(Medicine.class, medicineId);
        if (m != null) {
            session.remove(m);
        }
    }  

    @Override
    public void addInventory(int medicineId, InventoryDTO dto) {
        Session session = this.factory.getObject().getCurrentSession();
        Medicine m = session.get(Medicine.class, medicineId);
        if (m != null) {
            Inventory inv = InventoryMapper.toInventory(dto, m);
            session.persist(inv);
        }
    }

    @Override
    public void updateInventory(int inventoryId, InventoryDTO dto) {
        Session session = this.factory.getObject().getCurrentSession();
        Inventory inv = session.get(Inventory.class, inventoryId);
        if (inv != null) {
            InventoryMapper.updateInventoryFromDTO(inv, dto);
            session.merge(inv);
        }
    }
    
    @Override
    public void deleteInventory(int inventoryId) {
        Session session = this.factory.getObject().getCurrentSession();
        Inventory inv = session.get(Inventory.class, inventoryId);
        if (inv != null) {
            session.remove(inv);
        }
    }

    @Override
    public List<InventoryDTO> getExpiringInventories() {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Inventory> q = b.createQuery(Inventory.class);
        Root<Inventory> root = q.from(Inventory.class);
        
        root.fetch("medicineId", JoinType.INNER);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, 6);
        Date deadline = cal.getTime();

        q.where(b.lessThanOrEqualTo(root.get("expiryDate"), deadline));
        
        q.orderBy(b.asc(root.get("expiryDate")));

        List<Inventory> results = session.createQuery(q).getResultList();

        List<InventoryDTO> dtos = new ArrayList<>();
        for (Inventory inv : results) {
            InventoryDTO invDto = InventoryMapper.toDTO(inv);
            if (invDto != null) {
                invDto.setCreatedAt(inv.getCreatedAt());
                dtos.add(invDto);
            }
        }
        return dtos;
    }

    @Override
    public List<InventoryDTO> getLowStockInventories() {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Inventory> q = b.createQuery(Inventory.class);
        Root<Inventory> root = q.from(Inventory.class);
        
        root.fetch("medicineId", JoinType.INNER);

        q.where(b.lessThan(root.get("quantity"), 100));
        
        q.orderBy(b.asc(root.get("quantity")));

        List<Inventory> results = session.createQuery(q).getResultList();

        List<InventoryDTO> dtos = new ArrayList<>();
        for (Inventory inv : results) {
            InventoryDTO invDto = InventoryMapper.toDTO(inv);
            if (invDto != null) {
                invDto.setCreatedAt(inv.getCreatedAt());
                dtos.add(invDto);
            }
        }
        return dtos;
    }

    @Override
    @Transactional
    public void dispenseMedicine(int medicalRecordId) {
        Session session = this.factory.getObject().getCurrentSession();

        // 1. Lấy danh sách thuốc từ bảng prescribed_medicine dựa theo medical_record_id
        Query<PrescribedMedicine> q = session.createQuery(
            "FROM PrescribedMedicine pm WHERE pm.medicalRecordId.id = :mrId", PrescribedMedicine.class);
        q.setParameter("mrId", medicalRecordId);
        List<PrescribedMedicine> prescribedList = q.getResultList();

        if (prescribedList.isEmpty()) {
            throw new RuntimeException("Không tìm thấy thuốc nào được kê cho phiếu khám số " + medicalRecordId);
        }

        // 2. Duyệt từng loại thuốc trong đơn
        for (PrescribedMedicine pm : prescribedList) {
            int requiredQty = pm.getQuantity();
            Medicine medicine = pm.getMedicineId();

            // Tìm các lô hàng (inventory) còn hàng, ưu tiên lô hết hạn trước (FEFO)
            Query<Inventory> invQuery = session.createQuery(
                "FROM Inventory WHERE medicineId.id = :mId AND quantity > 0 ORDER BY expiryDate ASC", Inventory.class);
            invQuery.setParameter("mId", medicine.getId());
            List<Inventory> lots = invQuery.getResultList();

            for (Inventory lot : lots) {
                if (requiredQty <= 0) break;

                int currentLotQty = lot.getQuantity();
                if (currentLotQty >= requiredQty) {
                    lot.setQuantity(currentLotQty - requiredQty);
                    requiredQty = 0;
                } else {
                    requiredQty -= currentLotQty;
                    lot.setQuantity(0);
                }
                session.merge(lot);
            }

            if (requiredQty > 0) {
                throw new RuntimeException("Cảnh báo: Thuốc '" + medicine.getName() + "' trong kho không đủ (thiếu " + requiredQty + ")");
            }
        }    
    }
}
    

