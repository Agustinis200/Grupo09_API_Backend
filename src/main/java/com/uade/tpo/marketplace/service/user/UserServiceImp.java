package com.uade.tpo.marketplace.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.uade.tpo.marketplace.controllers.user.UserRequest;
import com.uade.tpo.marketplace.controllers.user.UserResponse;
import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.enums.Rol;
import com.uade.tpo.marketplace.exception.user.UserNotFoundException;
import com.uade.tpo.marketplace.mapper.UserMapper;
import com.uade.tpo.marketplace.repository.UserRepository;

@Service
public class UserServiceImp  implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Transactional(readOnly = true)
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toResponse).orElseThrow(() -> new UserNotFoundException("User not found: " + id));
    }

    @Transactional(readOnly = true)
    public UserResponse getUserMe(String email) {
        return userRepository.findByEmail(email).map(userMapper::toResponse).orElseThrow(() -> new UserNotFoundException("User not found: " + email));
    }

    @Transactional
    public UserResponse updateUserMe(User user, UserRequest userRequest) {
        userMapper.updateEntityMe(userRequest, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateUser(long id, Rol rol) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found: " + id));
        userMapper.updateEntityAdmin(rol, user);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        userRepository.save(user);
        return userMapper.toResponse(user);
    }

}
