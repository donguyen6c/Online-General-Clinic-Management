/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.DoctorWorkingPatternDTO;
import com.vudo.mapper.DoctorWorkingPatternMapper;
import com.vudo.pojo.Doctor;
import com.vudo.pojo.DoctorWorkingPattern;
import com.vudo.repositories.DoctorRepository;
import com.vudo.repositories.DoctorWorkingPatternRepository;
import com.vudo.services.DoctorWorkingPatternService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 *
 * @author ADMIN
 */
@Service
public class DoctorWorkingPatternServiceImpl implements DoctorWorkingPatternService {

    @Autowired
    DoctorWorkingPatternRepository repo;
    @Autowired
    DoctorRepository doctorRepo;

    @Override
    public void addOrUpdate(DoctorWorkingPatternDTO dto) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String username = auth.getName();

        Doctor doctor = doctorRepo.getDoctorByUserName(username);

        if (doctor == null) {
            throw new IllegalArgumentException("Doctor không tồn tại");
        }

        DoctorWorkingPattern pattern
                = DoctorWorkingPatternMapper.toEntity(dto);

        if (pattern.getEndTime().isBefore(pattern.getStartTime())) {
            throw new IllegalArgumentException(
                    "Giờ kết thúc phải sau giờ bắt đầu"
            );
        }

        DoctorWorkingPattern existing
                = repo.getByDoctorIdAndDayOfWeek(doctor.getId(), dto.getDayOfWeek());

        if (existing != null) {
            existing.setStartTime(pattern.getStartTime());
            existing.setEndTime(pattern.getEndTime());

            repo.addOrUpdate(existing);
            return;
        }

        pattern.setDoctorId(doctor);

        repo.addOrUpdate(pattern);
    }

    @Override
    public void deleteMyWorkingPattern(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Doctor doctor = doctorRepo.getDoctorByUserName(username);

        if (doctor == null) {
            throw new IllegalArgumentException("Không tìm thấy bác sĩ");
        }

        DoctorWorkingPattern pattern = repo.getById(id);

        if (pattern == null) {
            throw new IllegalArgumentException("Không tìm thấy lịch làm việc");
        }

        if (!pattern.getDoctorId().getId().equals(doctor.getId())) {
            throw new AccessDeniedException("Bạn không có quyền xóa lịch làm việc này");
        }

        this.repo.delete(id);
    }

    @Override
    public List<DoctorWorkingPatternDTO> getMyWorkingPatterns() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        Doctor doctor = doctorRepo.getDoctorByUserName(username);

        if (doctor == null) {
            throw new IllegalArgumentException("Không tìm thấy bác sĩ");
        }

        List<DoctorWorkingPattern> patterns = this.repo.getByDoctorId(doctor.getId());

        return patterns.stream()
                .map(DoctorWorkingPatternMapper::toDTO)
                .collect(Collectors.toList());
    }

}
