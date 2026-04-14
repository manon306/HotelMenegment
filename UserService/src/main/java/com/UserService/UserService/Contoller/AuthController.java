package com.UserService.UserService.Contoller;

import com.UserService.UserService.Entity.User;
import com.UserService.UserService.Services.AuthService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService service;

    @PostMapping("/register")
    public String register(@RequestBody @Valid User user) {
        return service.register(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid Map<String, String> credentials) {
        try {
            String token = service.login(credentials.get("username"), credentials.get("password"));

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());

            return ResponseEntity.status(401).body(error);
        }
    }

    // // ADMIN ONLY TEST
    // @GetMapping("/admin/test")
    // @PreAuthorize("hasRole('ADMIN')")
    // public String adminOnly() {
    // return "Admin only";
    // }
}