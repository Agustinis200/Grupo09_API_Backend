package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.enums.Rol;

import lombok.Data;

@Data

public class UserResponse {
    public UserResponse(Long id, String name, String email, Rol role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    private Long id;
    private String name;
    private String email;
    private Rol role;
}
