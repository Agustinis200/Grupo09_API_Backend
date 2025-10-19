package com.uade.tpo.marketplace.controllers.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PasswordValidationResponse {
    private boolean valid;
    private String message;
}
