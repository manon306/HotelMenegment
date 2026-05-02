package com.UserService.UserService.Contoller;

import com.UserService.UserService.DTO.LoginRequest;
import com.UserService.UserService.DTO.RegisterDTO;
import com.UserService.UserService.Entity.User;
import com.UserService.UserService.Services.AuthService;

import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
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
    public ResponseEntity<Map<String, String>> register(@RequestBody @Valid RegisterDTO user) {
        try {
            service.register(user);
            return ResponseEntity.ok(Map.of("message", "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Valid LoginRequest request) {
        try {
            // هنا بتستخدم request.getEmail() و request.getRole()
            String token = service.login(request.getEmail(), request.getPassword());

            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/approve/{userId}")
    public ResponseEntity<String> approveEmployee(@PathVariable Long userId) {
        return ResponseEntity.ok(service.approveEmployee(userId));
    }

    @PostMapping("/reject/{userId}")
    public ResponseEntity<String> rejectEmployee(@PathVariable Long userId) {
        return ResponseEntity.ok(service.rejectEmployee(userId));
    }

    @GetMapping("/pending-employees")
    public ResponseEntity<List<User>> getPendingEmployees() {
        return ResponseEntity.ok(service.getPendingEmployees());
    }

}