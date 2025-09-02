package com.uade.tpo.marketplace.service.user;

import java.util.List;

import com.uade.tpo.marketplace.controllers.user.UserRequest;
import com.uade.tpo.marketplace.controllers.user.UserResponse;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.enums.Rol;

public interface UserService {

    public List<UserResponse> getAllUsers();

    public UserResponse createUser(UserRequest userRequest);

    public UserResponse getUserById(Long id);

    public UserResponse updateUser(long id, Rol rol);

    public UserResponse updateUserMe(User user, UserRequest userRequest);

    public UserResponse getUserMe(String email);

    public void deleteUser(Long id);
}
