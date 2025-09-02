package com.uade.tpo.marketplace.mapper;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplace.controllers.user.UserRequest;
import com.uade.tpo.marketplace.controllers.user.UserResponse;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.enums.Rol;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .rol(user.getRol()) 
                .build();
    }

    public User toEntity(UserRequest request) {
        if (request == null) return null;
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .rol(request.getRol())
                .build();
        return user;
    }


    public void updateEntityMe(UserRequest request, User entity) {
        if (request == null || entity == null) return;

        if (request.getName() != null)   entity.setName(request.getName());
        if (request.getEmail() != null)  entity.setEmail(request.getEmail());
        if (request.getPassword() != null) {
            entity.setPassword(request.getPassword());
        }
    }

    public void updateEntityAdmin(Rol request, User entity) {
        if (request == null || entity == null) return;
        entity.setRol(request);
    }

    public List<UserResponse> toResponseList(List<User> users) {
        if (users == null) return List.of();
        return users.stream().map(this::toResponse).collect(Collectors.toList());
    }
}
