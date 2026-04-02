package com.vudo.repositories.impl;

import com.vudo.pojo.Doctor;
import com.vudo.pojo.User;
import com.vudo.repositories.DoctorRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DoctorRepositoryImpl implements DoctorRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Override
    public List<Doctor> getDoctors(Map<String, String> params) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Doctor> q = b.createQuery(Doctor.class);
        Root<Doctor> root = q.from(Doctor.class);
        q.select(root);

        if (params != null) {
            List<Predicate> predicates = new ArrayList<>();

            String kw = params.get("kw");
            if (kw != null && !kw.isEmpty()) {
                predicates.add(b.like(root.get("userId").get("fullName"), String.format("%%%s%%", kw)));
            }

            String specId = params.get("specialtyId");
            if (specId != null && !specId.isEmpty()) {
                predicates.add(b.equal(root.get("specialtyId").get("id"), Integer.parseInt(specId)));
            }

            if (!predicates.isEmpty()) {
                q.where(predicates.toArray(Predicate[]::new));
            }
        }

        Query<Doctor> query = session.createQuery(q);
        return query.getResultList();
    }

    @Override
    public Doctor getDoctorById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Doctor.class, id);
    }

    @Override
    public void addOrUpdateDoctor(Doctor doctor) {
        Session session = this.factory.getObject().getCurrentSession();

        // Nếu doctor có ID hợp lệ -> Cập nhật (Update)
        if (doctor.getId() != null && doctor.getId() > 0) {
            session.merge(doctor);
        } else {
            // Ngược lại chưa có ID -> Thêm mới (Add)
            session.persist(doctor);
        }
    }

    @Override
    public void deleteDoctor(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        Doctor d = this.getDoctorById(id);
        if (d != null) {
            User u = d.getUserId();

            session.remove(d);
            if (u != null) {
                session.remove(u);
            }
        }
    }
}
