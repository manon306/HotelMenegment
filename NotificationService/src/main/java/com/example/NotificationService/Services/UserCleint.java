package com.example.NotificationService.Services;

import org.springframework.stereotype.Service;

import com.example.NotificationService.DTO.UserDTO;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import com.example.NotificationService.Exceptions.BadRequestException;

@Service
public class UserCleint {

    @Autowired
    private RestTemplate restTemplate;

    public UserDTO getUserFromUserService(Long userId) {
        String url = "http://USER-SERVICE/api/users/" + userId;
        try {
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (Exception e) {
            throw new BadRequestException("Failed to fetch user from User Service: " + e.getMessage());
        }
    }
}
