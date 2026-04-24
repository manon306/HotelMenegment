package com.RoomServices.RoomService.Controller;

import com.RoomServices.RoomService.DTO.CreateRoomRequest;
import com.RoomServices.RoomService.DTO.UpdateRoomRequest;
import com.RoomServices.RoomService.ENUM.RoomStatus;
import com.RoomServices.RoomService.ENUM.RoomType;
import com.RoomServices.RoomService.Entity.Room;
import com.RoomServices.RoomService.Services.*;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Create
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> createRoom(@ModelAttribute @Valid CreateRoomRequest request) {
        return ResponseEntity.ok(roomService.addRoom(request));
    }

    // Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id,
            @RequestBody UpdateRoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    // Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok("Room deleted successfully");
    }

    // Get by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER', 'EMPLOYEE')")
    public ResponseEntity<Room> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    // Pagination
    @GetMapping
    public ResponseEntity<Page<Room>> getAllRooms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(roomService.getAllRooms(page, size));
    }

    @GetMapping("/available")
    @PreAuthorize("hasRole('CUSTOMER')")
    public List<Room> getAvailableRooms() {
        return roomService.getAvailableRooms();
    }

    // Update Status
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Room> updateStatus(@PathVariable Long id,
            @RequestParam RoomStatus status) {
        return ResponseEntity.ok(roomService.updateRoomStatus(id, status));
    }

    // Mark Maintenance
    @PatchMapping("/{id}/maintenance")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Room> markMaintenance(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.markRoomUnderMaintenance(id));
    }

    // Filter by Type
    @GetMapping("/type")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Room>> getByType(@RequestParam RoomType type) {
        return ResponseEntity.ok(roomService.getRoomsByType(type));
    }

    // Filter by Feature
    @GetMapping("/feature")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<Room>> getByFeature(@RequestParam String feature) {
        return ResponseEntity.ok(roomService.getRoomsByFeature(feature));
    }

    @GetMapping("/images/{filename}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) throws IOException {

        Path filePath = Paths.get("app/uploads/")
                .resolve(filename);

        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
}