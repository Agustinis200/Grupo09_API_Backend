package com.uade.tpo.marketplace.controllers.user;

import com.uade.tpo.marketplace.entity.enums.Rol;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String name;
    private String email;
    private String password;
    private Rol rol;
}
