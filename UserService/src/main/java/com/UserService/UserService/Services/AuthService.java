package com.UserService.UserService.Services;

import com.UserService.UserService.ENUMS.Role;
import com.UserService.UserService.Entity.User;
import com.UserService.UserService.Repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.UserService.UserService.Exceptions.BadRequestException;

@Service
public class AuthService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    public String register(User user) {
        // 1. تشفير كلمة السر قبل الحفظ (خطوة أساسية للأمان)
        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole(Role.CUSTOMER);
        // 2. حفظ المستخدم في MySQL
        repo.save(user);
        return "User registered successfully!";
    }

    public String login(String username, String password) {

        User user = repo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadRequestException("Invalid password");
        }

        return jwtService.generateToken(user.getUsername(), user.getRole());
    }

}