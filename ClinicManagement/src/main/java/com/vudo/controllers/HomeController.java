/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.pojo.Doctor;
import com.vudo.pojo.User;
import com.vudo.services.DoctorService;
import com.vudo.services.SpecialtyService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADMIN
 */

@Controller
public class HomeController {
    @Autowired
    private SpecialtyService specService; 
    
    @Autowired
    private DoctorService docService;
    
    @RequestMapping("/")
    public String index(Model model, @RequestParam Map<String,String> params){
        model.addAttribute("specialty", this.specService.getSpecEntity());
        model.addAttribute("doctors", this.docService.getDoctorEntities(params));
        return "index";
    }
    
    @GetMapping("/doctor/{doctorId}")
    public String updateView(Model model,@PathVariable(value="doctorId") int id){
        model.addAttribute("doctor", this.docService.getDoctorEntityById(id));
        return "doctors";
    }
    
    @GetMapping("/doctor/add")
    public String addDoctorView(Model model) {
        Doctor newDoctor = new Doctor();
        newDoctor.setUserId(new User());
        
        model.addAttribute("doctor", newDoctor);
        model.addAttribute("specialty", this.specService.getSpecEntity());
        return "doctor";
    }

    @GetMapping("/doctor/edit/{doctorId}")
    public String editDoctorView(Model model, @PathVariable(value="doctorId") int id) {
        model.addAttribute("doctor", this.docService.getDoctorEntityById(id));
        model.addAttribute("specialty", this.specService.getSpecEntity());
        return "doctor";
    }

    @PostMapping("/doctor/save")
    public String saveDoctor(@ModelAttribute(value = "doctor") Doctor doctor, Model model) {
        try {
            this.docService.addOrUpdateDoctorEntity(doctor);
            return "redirect:/";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errMsg", ex.getMessage());
            model.addAttribute("specialty", this.specService.getSpecEntity());
            return "doctor"; 
        } catch (Exception ex) {
            model.addAttribute("errMsg", "Đã có lỗi hệ thống xảy ra: " + ex.getMessage());
            return "doctor";
        }
    }
}
