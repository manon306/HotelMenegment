package com.BookingService.BookingService.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.BookingService.BookingService.DTO.RoomDTO;
import com.BookingService.BookingService.Exceptions.BadRequestException;

@Service
public class RoomCleint {
    @Autowired
    private RestTemplate restTemplate;

    public RoomDTO getRoomFromRoomService(Long roomId) {
        String url = "http://ROOM-SERVICE/rooms/" + roomId;
        try {
            return restTemplate.getForObject(url, RoomDTO.class);
        } catch (Exception e) {
            throw new BadRequestException("Failed to get room from Room Service: " + e.getMessage());
        }
    }

    public void updateRoomStatus(Long roomId, String status) {
        String url = "http://ROOM-SERVICE/rooms/" + roomId + "/status?status=" + status;
        try {
            restTemplate.patchForObject(url, null, String.class);
        } catch (Exception e) {
            throw new BadRequestException("Failed to update room status: " + e.getMessage());
        }
    }

    public Integer GetTotalRooms() {
        Integer totalRooms;
        try {
            totalRooms = restTemplate.getForObject("http://ROOM-SERVICE/api/rooms/count", Integer.class);
        } catch (Exception e) {
            totalRooms = 50; // Fallback في حالة لو الخدمة التانية وقعت
        }
        return totalRooms;
    }

}
