package com.uade.tpo.marketplace.service;

import java.util.List;

import com.uade.tpo.marketplace.entity.dto.UserResponse;
import com.uade.tpo.marketplace.enums.Rol;

public interface UserService {

    public List<UserResponse> getAllUsers() ;
    
    public UserResponse getUserById(Long id);

    public UserResponse createUser(String name, String email, String password);

    public UserResponse updateUser(Long id, String name, String email, String password,Rol role);

    public Void deleteUser(Long id);
}
