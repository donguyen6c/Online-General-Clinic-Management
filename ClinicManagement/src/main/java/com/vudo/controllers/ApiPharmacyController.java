/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.MedicineDTO;
import com.vudo.services.PharmacyService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/secure/pharmacy")
@CrossOrigin
public class ApiPharmacyController {
    @Autowired
    private PharmacyService pharmacyService;
    
    @GetMapping("/medicines")
    @PreAuthorize("hasAuthority('doctor') or hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<List<MedicineDTO>> list(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(this.pharmacyService.getMedicines(params), HttpStatus.OK);
    }

    @PostMapping("/medicines")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<String> add(@Valid @RequestBody MedicineDTO medicineDTO) {
        if (this.pharmacyService.addMedicine(medicineDTO)) {
            return new ResponseEntity<>("Thêm thuốc thành công!", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Lỗi khi thêm thuốc!", HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/medicines/{id}")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<String> update(@PathVariable("id") int id, @Valid @RequestBody MedicineDTO medicineDTO) {
        if (this.pharmacyService.updateMedicine(id, medicineDTO)) {
            return new ResponseEntity<>("Cập nhật thành công!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Lỗi khi cập nhật!", HttpStatus.BAD_REQUEST);
    } 
}
