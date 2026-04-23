package com.APIGateway.APIGateway;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.APIGateway.APIGateway.DTO.BookingStatsDTO;
import com.APIGateway.APIGateway.DTO.ReviewStatsDTO;
import com.APIGateway.APIGateway.DTO.UserStatsDTO;

@RestController
@RequestMapping("/api/admin")

public class DashboardController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ExecutorService executor;

    @GetMapping("/dashboard")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getDashboardData() {

        CompletableFuture<UserStatsDTO> userTask = CompletableFuture.supplyAsync(() -> callUserService(), executor);

        CompletableFuture<BookingStatsDTO> bookingTask = CompletableFuture.supplyAsync(() -> callBookingService(),
                executor);

        CompletableFuture<ReviewStatsDTO> reviewTask = CompletableFuture.supplyAsync(() -> callReviewService(),
                executor);

        CompletableFuture.allOf(userTask, bookingTask, reviewTask).join();

        Map<String, Object> response = new HashMap<>();
        response.put("users", userTask.join());
        response.put("bookings", bookingTask.join());
        response.put("reviews", reviewTask.join());

        return ResponseEntity.ok(response);
    }

    // ================= SERVICES =================

    private UserStatsDTO callUserService() {
        try {
            return restTemplate.getForObject(
                    "http://USER-SERVICE/User/stats",
                    UserStatsDTO.class);
        } catch (Exception e) {
            return fallbackUser();
        }
    }

    private BookingStatsDTO callBookingService() {
        try {
            return restTemplate.getForObject(
                    "http://BOOKING-SERVICE/bookings/stats",
                    BookingStatsDTO.class);
        } catch (Exception e) {
            return fallbackBooking();
        }
    }

    private ReviewStatsDTO callReviewService() {
        try {
            return restTemplate.getForObject(
                    "http://REVIEW-SERVICE/reviews/stats",
                    ReviewStatsDTO.class);
        } catch (Exception e) {
            return fallbackReview();
        }
    }

    // ================= FALLBACK =================

    private UserStatsDTO fallbackUser() {
        UserStatsDTO dto = new UserStatsDTO();
        dto.totalUsers = 0;
        return dto;
    }

    private BookingStatsDTO fallbackBooking() {
        BookingStatsDTO dto = new BookingStatsDTO();
        dto.totalRevenue = 0;
        dto.totalBookings = 0;
        dto.occupancyRate = 0.0;
        return dto;
    }

    private ReviewStatsDTO fallbackReview() {
        ReviewStatsDTO dto = new ReviewStatsDTO();
        dto.totalReviews = 0;
        dto.averageRating = 0.0;
        return dto;
    }
}