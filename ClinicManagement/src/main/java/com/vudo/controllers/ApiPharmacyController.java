/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.InventoryDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
        return new ResponseEntity<>(pharmacyService.getMedicines(params), HttpStatus.OK);
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasAuthority('doctor') or hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<List<InventoryDTO>> getInventories(@RequestParam Map<String, String> params) {
        return new ResponseEntity<>(pharmacyService.getInventories(params), HttpStatus.OK);
    }
    
    
    @PostMapping("/medicines")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    public void createMedicine(@Valid @RequestBody MedicineDTO dto) {
        pharmacyService.addMedicine(dto);
    }

    @PutMapping("/medicines/{id}")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<String> updateMedicine(@PathVariable("id") int id, @Valid @RequestBody MedicineDTO dto) {
        pharmacyService.updateMedicine(id, dto);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
    
    @DeleteMapping("/medicines/{id}")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMedicine(@PathVariable("id") int id) {
        pharmacyService.deleteMedicine(id);
    }
    
    @PostMapping("/medicines/{medicineId}/inventory")
    public void addInventoryToMedicine(@PathVariable("medicineId") int medicineId, @Valid @RequestBody InventoryDTO dto) { 
        pharmacyService.addInventory(medicineId, dto);
    }
    
    @PutMapping("/inventory/{inventoryId}")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<String> updateInventory(@PathVariable("inventoryId") int invId, @Valid @RequestBody InventoryDTO dto) {
        pharmacyService.updateInventory(invId, dto);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @DeleteMapping("/inventory/{inventoryId}")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInventory(@PathVariable("inventoryId") int invId) {
        pharmacyService.deleteInventory(invId);
    }
    
    @GetMapping("/inventory/expiring")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<List<InventoryDTO>> getExpiringInventory() {
        return new ResponseEntity<>(pharmacyService.getExpiringInventories(), HttpStatus.OK);
    }
    
    @GetMapping("/inventory/low-stock")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    public ResponseEntity<List<InventoryDTO>> getLowStockInventory() {
        return new ResponseEntity<>(pharmacyService.getLowStockInventories(), HttpStatus.OK);
    }
    
    @PostMapping("/medical-records/{id}/dispense")
    @PreAuthorize("hasAuthority('pharmacist') or hasAuthority('admin')")
    @ResponseStatus(HttpStatus.OK)
    public void dispenseMedicine(@PathVariable("id") int medicalRecordId) {
        pharmacyService.dispenseMedicine(medicalRecordId);
    }
}
