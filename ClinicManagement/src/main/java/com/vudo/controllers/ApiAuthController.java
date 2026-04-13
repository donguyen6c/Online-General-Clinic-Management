/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.UserDTO;
import com.vudo.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/auth")
public class ApiAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping(path = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<?> register(
            @RequestParam Map<String, String> params,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {

        try {

            if (userService.getUserByUsername(params.get("username")) != null) {

                return new ResponseEntity<>("Tên đăng nhập đã tồn tại!", HttpStatus.BAD_REQUEST);

            }

            UserDTO newUser = userService.addUser(params, avatar);

            return new ResponseEntity<>(newUser, HttpStatus.CREATED);

        } catch (Exception e) {

            return new ResponseEntity<>("Lỗi hệ thống: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        try {
            // 1. Dùng hàm loadUserByUsername bạn đã viết để lấy thông tin bảo mật (chứa pass băm)
            org.springframework.security.core.userdetails.UserDetails userDetails
                    = userService.loadUserByUsername(username);

            // 2. So sánh mật khẩu nguyên bản với mật khẩu đã băm
            if (passwordEncoder.matches(password, userDetails.getPassword())) {

                // 3. Nếu đúng, lấy cục DTO sạch sẽ (không chứa pass) để trả về cho ReactJS
                UserDTO userDTO = userService.getUserByUsername(username);
                return new ResponseEntity<>(userDTO, HttpStatus.OK);

            } else {
                return new ResponseEntity<>("Sai mật khẩu!", HttpStatus.UNAUTHORIZED); // Lỗi 401
            }

        } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
            return new ResponseEntity<>("Tài khoản không tồn tại!", HttpStatus.UNAUTHORIZED); // Lỗi 401
        }
    }
}
