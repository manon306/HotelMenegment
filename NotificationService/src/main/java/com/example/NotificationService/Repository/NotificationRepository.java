package com.example.NotificationService.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.NotificationService.ENTITY.Notification;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserId(Long userId);
    
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Notification> findByUserIdAndIsRead(Long userId, Boolean isRead);
    void deleteByUserId(Long userId);
    long countByUserIdAndIsRead(Long userId, Boolean isRead);
}
