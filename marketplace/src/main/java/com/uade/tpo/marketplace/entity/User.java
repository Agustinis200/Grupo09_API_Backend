package com.uade.tpo.marketplace.entity;

import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplace.enums.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name = "users", indexes=@Index(name="ix_usuario_email", columnList="email"))
public class User {

    public User() {}

    public User(String nombreUsuario, String email, String password) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.rol = Rol.USER;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    @NotBlank
    private String nombreUsuario;

    @Column(nullable=false, unique=true) 
    @Email 
    private String email;

    @Column(nullable=false)
    @NotBlank
    @Size(min=8)
    private String password;

    @Enumerated(EnumType.STRING) 
    @Column(nullable=false) 
    private Rol rol;

    @OneToMany(mappedBy = "user")
    private List<Cart> carts = new ArrayList<>();
}
