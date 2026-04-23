package com.UserService.UserService.Services;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.UserService.UserService.Entity.User;
import com.UserService.UserService.Exceptions.ResourceNotFoundException;
import com.UserService.UserService.Exceptions.BadRequestException;
import com.UserService.UserService.Repository.UserRepo;
import com.UserService.UserService.DTO.UserDTO;
import java.util.List;

@Service
public class UserServices {

    @Autowired
    private UserRepo repo;

    @Autowired
    private EmailService emailService;

    private static final int OTP_EXPIRY_MINUTES = 5;
    private static final int OTP_RANDOM_BOUND = 900000;
    private static final int OTP_MIN_VALUE = 100000;

    private User findUserByEmailOrThrow(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private User findUserByIdOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private String generateOtp() {
        return String.valueOf(new Random().nextInt(OTP_RANDOM_BOUND) + OTP_MIN_VALUE);
    }

    public void saveUser(User u) {
        repo.save(u);
    }

    public User readUserById(Long id) {
        return findUserByIdOrThrow(id);
    }

    public void deleteUser(Long id) {
        if (!repo.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        repo.deleteById(id);
    }
    public List<UserDTO> getAllUsers() {
        return repo.findAll().stream()
                .map(user -> UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .created_at(user.getCreated_at())
                .build())
                .collect(Collectors.toList());
    }
    @Transactional
    public void generateOTP(String email) {
        User user = findUserByEmailOrThrow(email);

        String otp = generateOtp();
        user.setOtp(otp);
        user.setOtpExpiryDate(LocalDateTime.now().plusMinutes(OTP_EXPIRY_MINUTES));

        repo.save(user);
        emailService.sendOTP(email, otp);
    }

    public void verifyOtp(String email, String otp) {
        User user = findUserByEmailOrThrow(email);

        if (!user.getOtp().equals(otp) ||
                user.getOtpExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invalid or expired OTP");
        }
    }

    public void resetPassword(String email, String newPassword) {
        User user = findUserByEmailOrThrow(email);
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        repo.save(user);
    }

    public Map<String, Object> getUserStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalUsers", repo.count());
        
        return stats;
    }
}