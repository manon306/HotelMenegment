package com.RoomServices.RoomService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.RoomServices.RoomService.DTO.UserDTO;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Service
public class UserCleint {

    @Autowired
    private RestTemplate restTemplate;

    public Long getUserIdFromEmail(String email) {
        String url = "http://USER-SERVICE/users/by-email?email=" + email;
        try {
            String token = getCurrentToken();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);
            HttpEntity<Void> entity = new HttpEntity<>(headers);
    
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, UserDTO.class);
            return response.getBody().getId();
        } catch (Exception e) {
            return null;
        }
    }

    public void validateUserExists(Long userId) {
        String url = "http://USER-SERVICE/users/" + userId;
        try {
            String token = getCurrentToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            restTemplate.exchange(url, HttpMethod.GET, entity, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + userId);
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