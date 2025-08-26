package com.uade.tpo.marketplace.controllers.user;

import com.uade.tpo.marketplace.entity.enums.Rol;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private Rol role;
}
