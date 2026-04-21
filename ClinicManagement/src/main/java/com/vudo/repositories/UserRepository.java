/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.vudo.repositories;

import com.vudo.pojo.User;

/**
 *
 * @author ASUS
 */
public interface UserRepository {
    User getUserByUsername(String username);
    User addUser(User u);
    boolean authenticate(String username, String password);
    User getUserById(int id);
}
