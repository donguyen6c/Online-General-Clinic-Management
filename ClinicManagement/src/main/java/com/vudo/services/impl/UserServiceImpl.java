/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.vudo.dto.UserDTO;
import com.vudo.mapper.UserMapper;
import com.vudo.pojo.User;
import com.vudo.repositories.UserRepository;
import com.vudo.services.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ASUS
 */
@Service("userDetailsService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserByUsername(String username) {
        User u = this.userRepo.getUserByUsername(username);
        if (u == null)
            return null;
        return UserMapper.toDTO(u);
    }

    @Override
    public UserDTO addUser(Map<String, String> params, MultipartFile avatar) {
        User u = new User();

        String phoneRegex = "^\\d{9}$";
        if (params.get("phone") == null || !params.get("phone").matches(phoneRegex))
            throw new IllegalArgumentException("Số điện thoại không hợp lệ! Vui lòng nhập đúng 9 chữ số.");

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        if (params.get("email") == null || !params.get("email").matches(emailRegex))
            throw new IllegalArgumentException("Email không đúng định dạng.");

        if (params.get("fullName") == null || params.get("fullName").isBlank())
            throw new IllegalArgumentException("Họ và tên không được để trống.");

        if (params.get("username") == null || params.get("username").isBlank())
            throw new IllegalArgumentException("Tên đăng nhập không được để trống.");

        if (params.get("password") == null || params.get("password").length() < 6)
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự.");

        if (this.userRepo.getUserByUsername(params.get("username")) != null)
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại.");

        u.setFullName(params.get("fullName"));
        u.setEmail(params.get("email"));
        u.setPhone(params.get("phone"));
        u.setGender(params.get("gender"));
        u.setUsername(params.get("username"));
        u.setPassword(passwordEncoder.encode(params.get("password")));
        u.setRole("patient");
        u.setProvider("local");
        u.setActive(true);
        u.setCreatedAt(new Date());

        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(avatar.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return UserMapper.toDTO(this.userRepo.addUser(u));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepo.getUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("Không tìm thấy tài khoản: " + username);
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(), authorities);
    }

    @Override
    public boolean authenticate(String username, String password) {
        UserDTO u = this.getUserByUsername(username); 
        if (u == null) {
            return false;
        }
        return this.userRepo.authenticate(username, password);
    }

    @Override
    public UserDTO updateProfile(String username, Map<String, String> params, MultipartFile avatar) {
        User u = this.userRepo.getUserByUsername(username);
        if (u == null) throw new RuntimeException("Không tìm thấy user");

        if (params.get("fullName") != null) u.setFullName(params.get("fullName"));
        if (params.get("email") != null) u.setEmail(params.get("email"));
        if (params.get("phone") != null) u.setPhone(params.get("phone"));
        if (params.get("gender") != null) u.setGender(params.get("gender"));

        if (params.get("dateOfBirth") != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                u.setDateOfBirth(sdf.parse(params.get("dateOfBirth")));
            } catch (ParseException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (avatar != null && !avatar.isEmpty()) {
            try {
                Map res = this.cloudinary.uploader().upload(avatar.getBytes(),
                        ObjectUtils.asMap("resource_type", "auto"));
                u.setAvatar(res.get("secure_url").toString());
            } catch (IOException ex) {
                Logger.getLogger(UserServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        this.userRepo.updateUser(u);
        return UserMapper.toDTO(u);
    }
    
    @Override
    public List<UserDTO> searchPatients(String keyword) {
        return userRepo.searchByKeyword(keyword).stream() .map(UserMapper::toDTO).collect(Collectors.toList());
    }
    
    @Override
    public UserDTO findOrCreateGoogleUser(String email, String fullName, String avatar, String uid) {
        User existing = userRepo.getUserByUsername(email);
        if (existing != null) {
            return UserMapper.toDTO(existing);
        }

        User u = new User();
        u.setEmail(email);
        u.setFullName(fullName != null ? fullName : "Google User");
        u.setAvatar(avatar);
        u.setUsername(email);          
        u.setPassword(passwordEncoder.encode(uid));
        u.setRole("patient");
        u.setProvider("google");
        u.setProviderId(uid);
        u.setActive(true);
        u.setCreatedAt(new Date());

        return UserMapper.toDTO(userRepo.addUser(u));
    }

}
