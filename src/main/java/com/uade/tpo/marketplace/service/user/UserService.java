package com.uade.tpo.marketplace.service.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.uade.tpo.marketplace.controllers.user.UserRequest;
import com.uade.tpo.marketplace.controllers.user.UserResponse;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.enums.Rol;

public interface UserService {

    public Page<UserResponse> getAllUsers(Pageable pageable);

    public UserResponse createUser(UserRequest userRequest);

    public UserResponse getUserById(Long id);

    public UserResponse updateUser(long id, Rol rol);

    public UserResponse updateUserMe(User user, UserRequest userRequest);

    public UserResponse getUserMe(String email);

    public void deleteUser(Long id);

    public boolean validatePassword(User user, String password);
}
