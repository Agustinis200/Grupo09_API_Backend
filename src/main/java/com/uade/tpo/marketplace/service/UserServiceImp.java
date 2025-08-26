package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.controllers.user.UserResponse;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.enums.Rol;
import com.uade.tpo.marketplace.repository.UserRepository;

@Service
public class UserServiceImp  implements UserService{

    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(user.getId(), user.getNombreUsuario(), user.getEmail(), user.getRol()))
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id).map(user -> new UserResponse(user.getId(), user.getNombreUsuario(), user.getEmail(), user.getRol())).orElse(null);
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

        return userRepository.findById(user.getId()).map(updatedUser -> new UserResponse(updatedUser.getId(), updatedUser.getNombreUsuario(), updatedUser.getEmail(), updatedUser.getRol())).orElse(null);
    }

    public Void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            return null;
        }
        userRepository.deleteById(id);
        return null;
    }

}
