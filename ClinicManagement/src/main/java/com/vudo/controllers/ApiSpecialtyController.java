/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.SpecialtyDTO;
import com.vudo.services.SpecialtyService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ADMIN
 */
@RestController
@RequestMapping("/api")
public class ApiSpecialtyController {
    @Autowired
    private SpecialtyService specService;
            
    @GetMapping("/specialties")
    public ResponseEntity<List<SpecialtyDTO>> list(){
        return new ResponseEntity<>(this.specService.getSpec(),HttpStatus.OK);
    }
}
