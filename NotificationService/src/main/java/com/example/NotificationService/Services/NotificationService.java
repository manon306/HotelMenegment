package com.example.NotificationService.Services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.NotificationService.Repository.NotificationRepository;

import com.example.NotificationService.DTO.UserDTO;
import com.example.NotificationService.ENTITY.Notification;
import com.example.NotificationService.Exceptions.ResourceNotFoundException;
import com.example.NotificationService.Exceptions.BadRequestException;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository repository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserCleint userClient;

    public Notification sendNotification(Long userId, String email, String message) {

        UserDTO user = userClient.getUserFromUserService(userId);

        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        // 1. Save in DB
        Notification notification = Notification.builder()
                .userId(userId)
                .message(message)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        if (notification == null) {
            throw new BadRequestException("Failed to create notification");
        }
        repository.save(notification);

        // 2. Send Email Notification
        emailService.sendEmail(email, "Notification", message);

        return notification;
    }

    public List<Notification> getUserNotifications(Long userId) {
        userClient.getUserFromUserService(userId);
        return repository.findByUserId(userId);
    }

    public List<Notification> getUnreadNotifications(Long userId) {
        userClient.getUserFromUserService(userId);
        return repository.findByUserIdAndIsRead(userId, false);
    }

    public long getUnreadCount(Long userId) {
        userClient.getUserFromUserService(userId);
        return repository.countByUserIdAndIsRead(userId, false);
    }

    public void markAllAsRead(Long userId) {
        userClient.getUserFromUserService(userId);
        List<Notification> notifications = repository.findByUserIdAndIsRead(userId, false);
        notifications.forEach(n -> n.setRead(true));
        repository.saveAll(notifications);
    }

    public void deleteNotification(Long id) {
        repository.deleteById(id);
    }

    public void deleteAllUserNotifications(Long userId) {
        repository.deleteByUserId(userId);
    }

    public void markAsRead(Long id) {
        if (id == null) {
            throw new BadRequestException("Notification ID cannot be null");
        }
        Notification n = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found"));

        n.setRead(true);
        repository.save(n);
    }
}
