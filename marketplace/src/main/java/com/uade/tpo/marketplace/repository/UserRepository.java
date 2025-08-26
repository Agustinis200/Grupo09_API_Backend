package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.dto.UserResponse;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT new com.uade.tpo.marketplace.entity.dto.UserResponse(u.id, u.nombreUsuario, u.email, u.rol) FROM User u")
    List<UserResponse> findAllUsers();

    @Query("SELECT new com.uade.tpo.marketplace.entity.dto.UserResponse(u.id, u.nombreUsuario, u.email, u.rol) FROM User u WHERE u.id = ?1")
    UserResponse findUserById(Long id);
}
