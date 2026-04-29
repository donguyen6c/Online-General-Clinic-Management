/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.vudo.controllers;

import com.vudo.dto.NotificationDTO;
import com.vudo.dto.UserDTO;
import com.vudo.services.NotificationService;
import com.vudo.services.UserService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ASUS
 */
@RestController
@RequestMapping("/api/secure/notifications")
@CrossOrigin
public class ApiNotificationController {
    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private UserService userService;

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<List<NotificationDTO>> getNotifications(@RequestParam(value = "page", defaultValue = "1") int page, Principal principal) {
    if (principal == null) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    UserDTO currentUser = this.userService.getUserByUsername(principal.getName());    
    List<NotificationDTO> notifications = notificationService.getUserNotifications(currentUser.getId(), page);
    return new ResponseEntity<>(notifications, HttpStatus.OK);
}

    @PatchMapping("/{id}/read")
    @ResponseBody
    public ResponseEntity<String> markAsRead(@PathVariable("id") int id, Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        boolean success = notificationService.markNotificationAsRead(id);      
        if (success) {
            return new ResponseEntity<>("Đã đánh dấu đọc", HttpStatus.OK);
        }
        return new ResponseEntity<>("Không tìm thấy thông báo", HttpStatus.NOT_FOUND);
    }
    
    @GetMapping("/unread-count")
    @ResponseBody
    public ResponseEntity<Long> getUnreadCount(Principal principal) {
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        UserDTO currentUser = this.userService.getUserByUsername(principal.getName());

        Long unreadCount = notificationService.countUnreadByUserId(currentUser.getId());
        return new ResponseEntity<>(unreadCount, HttpStatus.OK);
    }
}
