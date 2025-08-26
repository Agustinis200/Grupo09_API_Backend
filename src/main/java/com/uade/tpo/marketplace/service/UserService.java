package com.uade.tpo.marketplace.service;

import java.util.List;

import com.uade.tpo.marketplace.controllers.user.UserResponse;
import com.uade.tpo.marketplace.entity.enums.Rol;

public interface UserService {

    public List<UserResponse> getAllUsers() ;
    
    public UserResponse getUserById(Long id);

    public UserResponse updateUser(Long id, String name, String email, String password,Rol role);

    public Void deleteUser(Long id);
}
