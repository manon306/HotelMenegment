package com.UserService.UserService.DTO;

import java.time.LocalDateTime;

import com.UserService.UserService.ENUMS.Role;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    public Long id;

    public String username;

    @Email(message = "Email should be valid")
    public String email;
    public String phone;
    @Enumerated(EnumType.STRING)
    public Role role;
    public LocalDateTime created_at;

}
