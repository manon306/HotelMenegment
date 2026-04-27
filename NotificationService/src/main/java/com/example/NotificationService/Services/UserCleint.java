package com.example.NotificationService.Services;

import org.springframework.stereotype.Service;

import com.example.NotificationService.DTO.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.NotificationService.Exceptions.BadRequestException;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Service
public class UserCleint {

    @Autowired
    private RestTemplate restTemplate;

    public UserDTO getUserFromUserService(long userId) {
        String url = "http://USER-SERVICE/users/" + userId;
        try {
            String token = getCurrentToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, UserDTO.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user details from User Service: " + e.getMessage());
        }
    }

    private String getCurrentToken() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            return request.getHeader("Authorization");
        }
        return null;
    }
}
