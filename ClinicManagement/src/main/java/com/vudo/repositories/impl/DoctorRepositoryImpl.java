package com.vudo.repositories.impl;

import com.vudo.pojo.Doctor;
import com.vudo.pojo.User;
import com.vudo.repositories.DoctorRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@PropertySource("classpath:configs.properties")
@Transactional
public class DoctorRepositoryImpl implements DoctorRepository {

    @Autowired
    private LocalSessionFactoryBean factory;

    @Autowired
    private Environment env;

    @Override
    public Map<String, Object> getDoctors(Map<String, String> params) {
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

        boolean hasNext = false;
        int pageSize = this.env.getProperty("doctors.page_size", Integer.class, 6);

        if (params != null) {
            int page = Integer.parseInt(params.getOrDefault("page", "1"));
            int start = (page - 1) * pageSize;

            query.setMaxResults(pageSize + 1);
            query.setFirstResult(start);
        }

        List<Doctor> doctors = query.getResultList();

        if (doctors.size() > pageSize) {
            hasNext = true;
            doctors.remove(doctors.size() - 1);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("data", doctors);
        result.put("hasNext", hasNext);

        return result;
    }

    @Override
    public List<Doctor> getAllDoctors(Map<String, String> params) {
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

        return session.createQuery(q).getResultList();
    }

    @Override
    public Doctor getDoctorById(int id) {
        Session session = this.factory.getObject().getCurrentSession();
        return session.get(Doctor.class, id);
    }

    @Override
    public void addOrUpdateDoctor(Doctor doctor) {
        Session session = this.factory.getObject().getCurrentSession();

        User user = doctor.getUserId();
        if (user != null) {
            if (user.getId() == null) {
                session.persist(user);
            } else {
                session.merge(user);
            }
        }

        if (doctor.getId() != null && doctor.getId() > 0) {
            session.merge(doctor);
        } else {
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

    @Override
    public Doctor getDoctorByUserName(String username) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder b = session.getCriteriaBuilder();
        CriteriaQuery<Doctor> q = b.createQuery(Doctor.class);
        Root<Doctor> root = q.from(Doctor.class);

        q.select(root);

        Predicate p = b.equal(root.get("userId").get("username"), username);
        q.where(p);

        List<Doctor> results = session.createQuery(q).getResultList();

        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public Doctor getDoctorByUserId(int userId) {
        Session session = this.factory.getObject().getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Doctor> cq = cb.createQuery(Doctor.class);
        Root<Doctor> root = cq.from(Doctor.class);
        
        cq.select(root).where(cb.equal(root.get("userId").get("id"), userId));
        
        return session.createQuery(cq).uniqueResult();
    }
}
