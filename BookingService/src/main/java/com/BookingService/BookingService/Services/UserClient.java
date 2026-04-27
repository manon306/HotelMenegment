package com.BookingService.BookingService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.BookingService.BookingService.DTO.UserDTO;

import com.BookingService.BookingService.Exceptions.BadRequestException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class UserClient {

    @Autowired
    private RestTemplate restTemplate;

    private String getTokenFromRequest() {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest().getHeader("Authorization");
    }

    private HttpEntity<String> buildAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getTokenFromRequest());
        return new HttpEntity<>(headers);
    }

    public UserDTO getUserFromUserService(Long userId) {
        String url = "http://USER-SERVICE/users/" + userId;
        try {
            ResponseEntity<UserDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, buildAuthEntity(), UserDTO.class);
            return response.getBody();
        } catch (Exception e) {
            throw new BadRequestException("Failed to get user from User Service: " + e.getMessage());
        }
    }
}
