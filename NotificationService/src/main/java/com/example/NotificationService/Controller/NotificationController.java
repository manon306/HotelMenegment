package com.example.NotificationService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.NotificationService.DTO.NotificationRequest;
import com.example.NotificationService.ENTITY.Notification;
import com.example.NotificationService.Services.NotificationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    // Send notification (system/admin only)
    @PostMapping("/send")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    public Notification send(@RequestBody @Valid NotificationRequest request) {
        return service.sendNotification(
                request.getUserId(),
                request.getEmail(),
                request.getMessage());
    }

    // User sees only their notifications
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE','CUSTOMER')")
    public List<Notification> getUserNotifications(@PathVariable Long userId) {
        return service.getUserNotifications(userId);
    }

    @PutMapping("/read/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE')")
    public void markAsRead(@PathVariable Long id) {
        service.markAsRead(id);
    }
}