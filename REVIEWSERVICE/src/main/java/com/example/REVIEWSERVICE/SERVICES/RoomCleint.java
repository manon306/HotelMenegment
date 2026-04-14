package com.example.REVIEWSERVICE.SERVICES;

import com.example.REVIEWSERVICE.DTO.RoomDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
@Service
public class RoomCleint {
    @Autowired
    private RestTemplate restTemplate;

    public RoomDTO GetRoomFromRoomService(long roomId) {
        String url = "http://ROOM-SERVICE/rooms/" + roomId;
        try {
            return restTemplate.getForObject(url, RoomDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch room details from Room Service: " + e.getMessage());
        }
    }
}
