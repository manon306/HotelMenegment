package com.UserService.UserService.Entity;

import jakarta.persistence.*; // هذا السطر يغني عن استيراد Id و Entity بشكل منفرد
import jakarta.validation.constraints.Email;
import lombok.*;

import java.time.LocalDateTime;

import com.UserService.UserService.ENUMS.Role;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Email(message = "Email should be valid")
    private String email;

    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    private String phone;
    private LocalDateTime created_at;
    private String otp;
    private LocalDateTime otpExpiryDate;

    @PrePersist
    public void prePersist() {
        this.created_at = LocalDateTime.now();
    }
}