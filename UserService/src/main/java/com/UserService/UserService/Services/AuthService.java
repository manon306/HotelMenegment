package com.UserService.UserService.Services;

import com.UserService.UserService.DTO.RegisterDTO;
import com.UserService.UserService.Entity.User;
import com.UserService.UserService.Repository.UserRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.UserService.UserService.Exceptions.BadRequestException;

@Service
public class AuthService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtService jwtService;

    @Transactional
    public String register(RegisterDTO dto) {
        // التأكد إن الباسوردين زي بعض
        if (!dto.password.equals(dto.Confirmpassword)) {
            throw new BadRequestException("Passwords do not match!");
        }

        // تشفير الباسورد وحفظ اليوزر
        User user = new User();
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setPassword(encoder.encode(dto.password)); // تشفير!
        user.setRole(dto.getRole());

        repo.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password, String role) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        if (!user.getRole().equals(role)) {
            throw new RuntimeException("Unauthorized: Role mismatch");
        }
        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtService.generateToken(user.getEmail(), user.getRole());
    }
}