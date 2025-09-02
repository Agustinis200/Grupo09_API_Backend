package com.uade.tpo.marketplace.service.user;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.uade.tpo.marketplace.controllers.user.UserRequest;
import com.uade.tpo.marketplace.controllers.user.UserResponse;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.enums.Rol;
import com.uade.tpo.marketplace.mapper.UserMapper;
import com.uade.tpo.marketplace.repository.UserRepository;

@Service
public class UserServiceImp  implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toResponse).orElse(null);// exception
    }

    public UserResponse getUserMe(String email) {
        return userRepository.findByEmail(email).map(userMapper::toResponse).orElse(null);// exception
    }

    public UserResponse updateUserMe(User user, UserRequest userRequest) {
        userMapper.updateEntityMe(userRequest, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse updateUser(long id, Rol rol) {

        User user = userRepository.findById(id).orElse(null);
        userMapper.updateEntityAdmin(rol, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

}
