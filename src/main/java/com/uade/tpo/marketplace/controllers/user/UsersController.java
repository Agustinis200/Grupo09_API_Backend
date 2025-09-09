package com.uade.tpo.marketplace.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uade.tpo.marketplace.entity.User;
import com.uade.tpo.marketplace.entity.enums.Rol;
import com.uade.tpo.marketplace.service.user.UserService;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("users")
public class UsersController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers( @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
                if (page == null && size == null) {
                    return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(0, 10)));   
                }
        return ResponseEntity.ok(userService.getAllUsers(PageRequest.of(page == null ? 0 : page, size == null ? Integer.MAX_VALUE : size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserMe(@AuthenticationPrincipal(expression = "email") String email) {
        return ResponseEntity.ok(userService.getUserMe(email));
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserRequest userRequest) {
        userService.createUser(userRequest);
        return ResponseEntity.ok("User created");
    }
    

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateUserMe(@AuthenticationPrincipal User user, @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUserMe(user, userRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody Rol rol) {
        return ResponseEntity.ok(userService.updateUser(id, rol));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
