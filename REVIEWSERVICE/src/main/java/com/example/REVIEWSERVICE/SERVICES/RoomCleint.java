package com.example.REVIEWSERVICE.SERVICES;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.REVIEWSERVICE.DTO.RoomDTO;
import com.example.REVIEWSERVICE.DTO.UserDTO;


@Service
public class RoomCleint {

    @Autowired
    private RestTemplate restTemplate;

    public RoomDTO GetRoomFromRoomService(long roomId) {
        String url = "http://ROOM-SERVICE/rooms/" + roomId;
        try {
            // جيبي الـ token من الـ current request
            String token = getCurrentToken();

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", token);

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<RoomDTO> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, RoomDTO.class);
            return response.getBody();
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch room details from Room Service: " + e.getMessage());
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