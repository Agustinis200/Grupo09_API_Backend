package com.uade.tpo.marketplace.entity.dto;

import com.uade.tpo.marketplace.enums.Rol;

import lombok.Data;

@Data
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private Rol rol;
}
