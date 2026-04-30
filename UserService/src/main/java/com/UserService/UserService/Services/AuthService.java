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
        if (repo.findByEmail(dto.email).isPresent()) {
            throw new BadRequestException("Email is already in use!");
        }
        if (!dto.password.equals(dto.Confirmpassword)) {
            throw new BadRequestException("Passwords do not match!");
        }

        User user = new User();
        user.setUsername(dto.username);
        user.setEmail(dto.email);
        user.setPassword(encoder.encode(dto.password));

        user.setRole(dto.getRole());

        repo.save(user);
        return "User registered successfully";
    }

    public String login(String email, String password) {
        User user = repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!encoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtService.generateToken(user.getEmail(), user.getRole(), user.getId());
    }
}