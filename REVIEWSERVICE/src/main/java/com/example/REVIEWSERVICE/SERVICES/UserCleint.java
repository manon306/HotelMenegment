package com.example.REVIEWSERVICE.SERVICES;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.example.REVIEWSERVICE.DTO.UserDTO;

@Service
public class UserCleint {
    @Autowired
    private RestTemplate restTemplate;
    public UserDTO getUserFromUserService(long userId) {
        String url = "http://USER-SERVICE/users/" + userId;
        try {
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user details from User Service: " + e.getMessage());
        }
    }
}
