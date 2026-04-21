/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.vudo.dto.AddPrescriptionRequestDTO;
import com.vudo.dto.AddServiceRequestDTO;
import com.vudo.dto.MedicalRecordRequestDTO;
import com.vudo.dto.MedicalRecordResponseDTO;
import com.vudo.dto.PrescriptionItemDTO;
import com.vudo.dto.ServiceItemDTO;
import com.vudo.mapper.MedicalRecordMapper;
import com.vudo.pojo.Appointment;
import com.vudo.pojo.Disease;
import com.vudo.pojo.Doctor;
import com.vudo.pojo.MedicalRecord;
import com.vudo.pojo.Medicine;
import com.vudo.pojo.PrescribedMedicine;
import com.vudo.pojo.User;
import com.vudo.repositories.AppointmentRepository;
import com.vudo.repositories.DiseaseRepository;
import com.vudo.repositories.DoctorRepository;
import com.vudo.repositories.MedicalRecordRepository;
import com.vudo.repositories.PharmacyRepository;
import com.vudo.repositories.ServicesRepository;
import com.vudo.repositories.UserRepository;
import com.vudo.services.MedicalRecordService;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ASUS
 */
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AppointmentRepository appointmentRepo;

    @Autowired
    private DiseaseRepository diseaseRepo;
    
    @Autowired
    private PharmacyRepository pharmacyRepo;
    
    @Autowired
    private ServicesRepository servicesRepo;

    @Override
    @Transactional
    public List<MedicalRecordResponseDTO> getPatientHistory(int patientId) {
        List<MedicalRecord> records = medicalRecordRepo.getByPatientId(patientId);
        return records.stream().map(record -> MedicalRecordMapper.toDTO(record)).collect(Collectors.toList());
    }

    @Override
    public MedicalRecordResponseDTO create(MedicalRecordRequestDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        Doctor doctor = doctorRepo.getDoctorByUserName(username);
        if (doctor == null) {
            throw new RuntimeException("Doctor not found");
        }
        Appointment appointment = null;
        User patient = null;
        if (request.getAppointmentId() == null && request.getPatientId() == null) {
            throw new RuntimeException("Phải truyền appointmentId hoặc patientId");
        }
        if (request.getAppointmentId() != null) {
            appointment = appointmentRepo.getById(request.getAppointmentId());
            if (appointment == null) {
                throw new RuntimeException("Appointment not found");
            }

            patient = appointment.getPatientId();
            if (request.getPatientId() != null
                    && !patient.getId().equals(request.getPatientId())) {
                throw new RuntimeException("patientId không khớp với appointment");
            }
        }
        else {
            patient = userRepo.getUserById(request.getPatientId());
            if (patient == null) {
                throw new RuntimeException("Patient not found");
            }
        }

        Set<Disease> diseases = new HashSet<>();
        if (request.getDiseaseIds() != null && !request.getDiseaseIds().isEmpty()) {
            diseases = new HashSet<>(diseaseRepo.getAllById(request.getDiseaseIds()));
        }
        System.out.println(diseases.size());
        MedicalRecord record = MedicalRecordMapper.toEntity(
                request, doctor, patient, appointment, diseases
        );

        record = medicalRecordRepo.create(record);

        return MedicalRecordMapper.toDTO(record);
    }

    @Override
    @Transactional
    public MedicalRecordResponseDTO getMedicalRecordDetail(int patientId, int recordId) {
        MedicalRecord record = medicalRecordRepo.getMedicalRecordById(recordId);

        if (record == null || record.getPatientId().getId() != patientId) {
            return null;
        }

        return MedicalRecordMapper.toDTO(record);
    }

    @Override
    @Transactional
    public void addPrescriptionsToRecord(Integer medicalRecordId, AddPrescriptionRequestDTO request) {
        MedicalRecord record = medicalRecordRepo.getMedicalRecordById(medicalRecordId);
        if (record == null) {
            throw new RuntimeException("Không tìm thấy hồ sơ bệnh án với ID: " + medicalRecordId);
        }

        Set<Integer> medicineIds = request.getMedicines().stream()
                .map(PrescriptionItemDTO::getMedicineId)
                .collect(Collectors.toSet());

        var medicines = pharmacyRepo.getAllMedicinesByIds(medicineIds);

        Map<Integer, Medicine> medicineMap = medicines.stream()
                .collect(Collectors.toMap(m -> m.getId(), m -> m));

        for (PrescriptionItemDTO item : request.getMedicines()) {
            
            Medicine medicine = medicineMap.get(item.getMedicineId());
            
            if (medicine == null) {
                throw new RuntimeException("Không tìm thấy thuốc có ID: " + item.getMedicineId());
            }

            PrescribedMedicine prescribedMedicine = new PrescribedMedicine();
            
            prescribedMedicine.setMedicalRecordId(record); 
            prescribedMedicine.setMedicineId(medicine);    
            
            prescribedMedicine.setQuantity(item.getQuantity());
            prescribedMedicine.setUsageInstruction(item.getUsageInstruction());
            prescribedMedicine.setPriceAtTime(medicine.getPrice()); 

            medicalRecordRepo.addPrescriptionsToRecord(prescribedMedicine);
        }
    }

    @Override
    public void addServicesToRecord(Integer medicalRecordId, AddServiceRequestDTO request) {
       MedicalRecord record = medicalRecordRepo.getMedicalRecordById(medicalRecordId);
        if (record == null) {
            throw new RuntimeException("Không tìm thấy hồ sơ bệnh án với ID: " + medicalRecordId);
        }

        Set<Integer> serviceIds = request.getServices().stream()
                .map(ServiceItemDTO::getServiceId)
                .collect(Collectors.toSet());

        List<com.vudo.pojo.Service> clinicServices = servicesRepo.getAllByIds(serviceIds);

        Map<Integer, com.vudo.pojo.Service> serviceMap = clinicServices.stream()
                .collect(Collectors.toMap(s -> s.getId(), s -> s));

        for (ServiceItemDTO item : request.getServices()) {
            
            com.vudo.pojo.Service clinicService = serviceMap.get(item.getServiceId());
            
            if (clinicService == null) {
                throw new RuntimeException("Không tìm thấy dịch vụ với ID: " + item.getServiceId());
            }

            com.vudo.pojo.MedicalRecordService recordService = new com.vudo.pojo.MedicalRecordService();
            recordService.setMedicalRecordId(record);
            recordService.setServiceId(clinicService);
            
            int quantity = (item.getQuantity() != null && item.getQuantity() > 0) ? item.getQuantity() : 1;
            recordService.setQuantity(quantity);
            recordService.setPriceAtTime(clinicService.getPrice()); 

            medicalRecordRepo.addServicesToRecord(recordService); 
        }
    }
}


