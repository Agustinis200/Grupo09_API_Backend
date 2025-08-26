package com.uade.tpo.marketplace.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.UserResponse;
import com.uade.tpo.marketplace.enums.Rol;
import com.uade.tpo.marketplace.repository.UserRepository;

@Service
public class UserServiceImp  implements UserService{

    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findUserById(id);
    }

    public UserResponse createUser(String name, String email, String password) {
        if (userRepository.findAll().isEmpty()) {
            User admin = new User(name, email, password);
            admin.setRol(Rol.ADMIN);
            userRepository.save(admin);
        }
        User user = new User(name, email, password);
        userRepository.save(user);

        return userRepository.findUserById(user.getId());
    }

    public UserResponse updateUser(Long id, String name, String email, String password, Rol role) {
         if (!userRepository.existsById(id)) {
             return null;
         }
        User user = userRepository.findById(id).get();
        user.setNombreUsuario(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRol(role);
        userRepository.save(user);
        
        return userRepository.findUserById(user.getId());
    }

    public Void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return null;
        }
        userRepository.deleteById(id);
        return null;
    }

}
