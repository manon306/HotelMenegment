package com.BookingService.BookingService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.BookingService.BookingService.DTO.RoomDTO;
import com.BookingService.BookingService.Exceptions.BadRequestException;

@Service
public class RoomCleint {

    @Autowired
    private RestTemplate restTemplate;

    private String getTokenFromRequest() {
        ServletRequestAttributes attrs = 
            (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attrs.getRequest().getHeader("Authorization");
    }

    private HttpEntity<String> buildAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", getTokenFromRequest());
        return new HttpEntity<>(headers);
    }

    public RoomDTO getRoomFromRoomService(Long roomId) {
        String url = "http://ROOM-SERVICE/rooms/" + roomId;
        try {
            ResponseEntity<RoomDTO> response = restTemplate.exchange(
                url, HttpMethod.GET, buildAuthEntity(), RoomDTO.class
            );
            return response.getBody();
        } catch (Exception e) {
            throw new BadRequestException("Failed to get room from Room Service: " + e.getMessage());
        }
    }

    public void updateRoomStatus(Long roomId, String status) {
        String url = "http://ROOM-SERVICE/rooms/" + roomId + "/status?status=" + status;
        try {
            restTemplate.exchange(
                url, HttpMethod.PATCH, buildAuthEntity(), String.class
            );
        } catch (Exception e) {
            throw new BadRequestException("Failed to update room status: " + e.getMessage());
        }
    }

    public Integer getTotalRooms() {
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                "http://ROOM-SERVICE/rooms/count",
                HttpMethod.GET, buildAuthEntity(), Integer.class
            );
            return response.getBody();
        } catch (Exception e) {
            return 50; // Fallback
        }
    }
}