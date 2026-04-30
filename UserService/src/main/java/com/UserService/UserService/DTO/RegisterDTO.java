package com.UserService.UserService.DTO;

import com.UserService.UserService.ENUMS.Role;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NonNull
    @NotBlank
    public String username;
    @Email(message = "Email should be valid")
    @NonNull
    @NotBlank
    public String email;
    @NonNull
    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W_]{6,}$", message = "Password must contain at least one letter and one number")
    public String password;
    @NonNull
    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d\\W_]{6,}$", message = "Password must contain at least one letter and one number")
    public String Confirmpassword;
    @Enumerated(EnumType.STRING)
    private Role role;
}
