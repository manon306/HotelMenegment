package com.RoomServices.RoomService.Services;
import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserCleint {
    @Autowired
    private RestTemplate restTemplate;

    public void validateUserExists(Long userId) {
        String url = "http://USER-SERVICE/api/users/" + userId;
        try {
            restTemplate.getForObject(url, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("User not found with id: " + userId);
        }
    }
}
