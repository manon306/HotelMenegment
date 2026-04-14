package com.BookingService.BookingService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.BookingService.BookingService.DTO.UserDTO;

import com.BookingService.BookingService.Exceptions.BadRequestException;

@Service
public class UserClient {
    @Autowired
    private RestTemplate restTemplate;

    public UserDTO getUserFromUserService(Long userId) {
        String url = "http://USER-SERVICE/users/" + userId;
        try {
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (Exception e) {
            throw new BadRequestException("Failed to get user from User Service: " + e.getMessage());
        }
    }
}
