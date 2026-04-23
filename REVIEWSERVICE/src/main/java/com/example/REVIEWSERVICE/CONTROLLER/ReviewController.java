package com.example.REVIEWSERVICE.CONTROLLER;

import com.example.REVIEWSERVICE.DTO.AddRoomReview;
import com.example.REVIEWSERVICE.ENTITY.Review;
import com.example.REVIEWSERVICE.SERVICES.ReviewService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService service;

    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> addReview(@RequestBody @Valid AddRoomReview reviewDto) {
        String result = service.addReview(reviewDto);
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getReviewsByUserId(userId));
    }

    @GetMapping("/average/{room_id}")
    public ResponseEntity<Double> getAvgRatingForRoom(@PathVariable long room_id) {
        return ResponseEntity.ok(service.getAvgRatingForRoom(room_id));
    }

    @GetMapping("/room/{room_id}")
    public ResponseEntity<List<Review>> getAllReviewsForRoom(@PathVariable long room_id) {
        return ResponseEntity.ok(service.getAllReviewsForRoom(room_id));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<String> deleteReview(@PathVariable long id) {
        String result = service.deleteReviewById(id);
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.status(404).body(result);
    }

    @GetMapping("/with-room-details")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAllReviewsWithRoomDetails() {
        return ResponseEntity.ok(service.getAllReviewsWithRoomDetails());
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<String> updateReview(
            @PathVariable long id,
            @RequestParam int rating,
            @RequestParam String comment) {

        String result = service.updateReviewById(id, rating, comment);
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        }
        return ResponseEntity.badRequest().body(result);
    }

    @GetMapping("/room-with-details/{room_id}")
    public ResponseEntity<Map<String, Object>> getRoomReviewsWithDetails(@PathVariable long room_id) {
        return ResponseEntity.ok(service.getRoomReviewsWithDetails(room_id));
    }

    @GetMapping("/admin/monitor/{room_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Review>> monitorReviews(
            @PathVariable long room_id,
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int minRating) {

        return ResponseEntity.ok(service.monitorReviewsForRoom(room_id, filter, minRating));
    }

    // ADMIN analytics
    @GetMapping("/admin/stats/{room_id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getMonitoringStats(@PathVariable long room_id) {
        return ResponseEntity.ok(service.getMonitoringStats(room_id));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(service.getReviewStatistics());
    }
}