package com.uade.tpo.marketplace.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class UsersController {

    @GetMapping
    public String getAllUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "users/detail";
    }

    @PostMapping
    public String createUser(@ModelAttribute UserRequest userRequest) {
        userService.create(userRequest);
        return "redirect:/users";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserRequest userRequest) {
        userService.update(id, userRequest);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return "redirect:/users";
    }
}
