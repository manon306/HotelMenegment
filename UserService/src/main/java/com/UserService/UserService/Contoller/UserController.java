package com.UserService.UserService.Contoller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import com.UserService.UserService.Entity.*;
import com.UserService.UserService.Services.UserServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserServices services;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public User getUserById(@PathVariable Long id) {
        return services.readUserById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteUser(@PathVariable Long id) {
        services.deleteUser(id);
        return "User deleted!";
    }

    @PostMapping
    public String saveUser(@RequestBody @Valid User u) {
        services.saveUser(u);
        return "User saved!";
    }

    @PostMapping("/otp")
    public String generateOTP(@RequestBody @Valid Map<String, String> request) {
        services.generateOTP(request.get("email"));
        return "OTP sent!";
    }

    @PostMapping("/verify-otp")
    public String verifyOTP(@RequestBody Map<String, String> request) {
        services.verifyOtp(request.get("email"), request.get("otp"));
        return "OTP verified!";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody @Valid Map<String, String> request) {
        services.resetPassword(request.get("email"), request.get("newPassword"));
        return "Password reset!";
    }
}