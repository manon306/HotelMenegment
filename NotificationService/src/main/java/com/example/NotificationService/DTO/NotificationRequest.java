package com.example.NotificationService.DTO;

import lombok.Data;

@Data
public class NotificationRequest {

    private Long userId;
    private String email;
    private String message;
}
