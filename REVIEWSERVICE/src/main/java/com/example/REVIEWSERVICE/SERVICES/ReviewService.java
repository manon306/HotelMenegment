package com.example.REVIEWSERVICE.SERVICES;

import org.springframework.stereotype.Service;

import com.example.REVIEWSERVICE.DTO.AddRoomReview;
import com.example.REVIEWSERVICE.DTO.RoomDTO;
import com.example.REVIEWSERVICE.DTO.UserDTO;
import com.example.REVIEWSERVICE.ENTITY.Review;
import com.example.REVIEWSERVICE.Exceptions.BadRequestException;
import com.example.REVIEWSERVICE.Exceptions.ResourceNotFoundException;
import com.example.REVIEWSERVICE.REPOSITORY.ReviewRepo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepo Repo;

    @Autowired
    private UserCleint userClient;

    @Autowired
    private RoomCleint roomClient;

    private void validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            throw new BadRequestException("Rating must be between 1 and 5");
        }
    }

    private UserDTO validateAndGetUser(Long userId) {
        UserDTO userDTO = userClient.getUserFromUserService(userId);
        if (userDTO == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return userDTO;
    }

    private RoomDTO validateAndGetRoom(Long roomId) {
        RoomDTO roomDTO = roomClient.GetRoomFromRoomService(roomId);
        if (roomDTO == null) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        return roomDTO;
    }

    private void checkDuplicateReview(Long userId, Long roomId) {
        boolean alreadyReviewed = Repo.existsByUserIdAndRoomId(userId, roomId);
        if (alreadyReviewed) {
            throw new BadRequestException("You have already reviewed this room");
        }
    }

    private Review findReviewOrThrow(Long id) {
        return Repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Review not found"));
    }

    public String addReview(AddRoomReview review) {
        validateAndGetUser(review.getUser_id());
        validateAndGetRoom(review.getRoom_id());
        validateRating(review.getRating());
        checkDuplicateReview(review.getUser_id(), review.getRoom_id());

        Repo.save(review.toEntity());
        return "Review added successfully";
    }

    public List<Review> getReviewsByUserId(Long userId) {
        validateAndGetUser(userId);
        return Repo.findByUserId(userId);
    }

    public double getAvgRatingForRoom(long room_id) {
        return Repo.findAvgRatingByRoomId(room_id).orElse(0.0);
    }

    public List<Review> getAllReviewsForRoom(long room_id) {
        return Repo.findByRoomId(room_id);
    }

    public String deleteReviewById(long id) {
        if (Repo.existsById(id)) {
            Repo.deleteById(id);
            return "Review deleted successfully";
        }
        return "Review not found";
    }

    public String updateReviewById(long id, int rating, String comment) {
        validateRating(rating);
        Review review = findReviewOrThrow(id);

        review.setRating(rating);
        review.setComment(comment);
        Repo.save(review);

        return "Review updated successfully";
    }

    private Map<Integer, Long> getRatingDistribution(long room_id) {
        return Repo.findByRoomId(room_id).stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));
    }

    public List<Review> monitorReviewsForRoom(long room_id, String filter, int minRating) {
        List<Review> reviews = Repo.findByRoomId(room_id);

        if (filter != null) {
            switch (filter) {
                case "positive":
                    return reviews.stream()
                            .filter(r -> r.getRating() >= 4)
                            .collect(Collectors.toList());
                case "negative":
                    return reviews.stream()
                            .filter(r -> r.getRating() <= 2)
                            .collect(Collectors.toList());
                case "recent":
                    return reviews.stream()
                            .sorted((a, b) -> b.getCreated_at().compareTo(a.getCreated_at()))
                            .limit(10)
                            .collect(Collectors.toList());
            }
        }

        if (minRating > 0) {
            return reviews.stream()
                    .filter(r -> r.getRating() >= minRating)
                    .collect(Collectors.toList());
        }

        return reviews;
    }

    public Map<String, Object> getMonitoringStats(long room_id) {
        List<Review> reviews = Repo.findByRoomId(room_id);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total_reviews", reviews.size());
        stats.put("average_rating", getAvgRatingForRoom(room_id));
        stats.put("rating_distribution", getRatingDistribution(room_id));
        stats.put("recent_reviews", reviews.stream()
                .sorted((a, b) -> b.getCreated_at().compareTo(a.getCreated_at()))
                .limit(5)
                .collect(Collectors.toList()));
        return stats;
    }

    public List<Map<String, Object>> getAllReviewsWithRoomDetails() {
        List<Review> reviews = Repo.findAll();

        return reviews.stream().map(review -> {
            Map<String, Object> reviewWithRoom = new HashMap<>();
            reviewWithRoom.put("review", review);

            try {
                RoomDTO room = roomClient.GetRoomFromRoomService(review.getRoomId());
                reviewWithRoom.put("room", room);
            } catch (Exception e) {
                reviewWithRoom.put("room", null);
                reviewWithRoom.put("room_error", e.getMessage());
            }

            return reviewWithRoom;
        }).collect(Collectors.toList());
    }

    public Map<String, Object> getRoomReviewsWithDetails(long roomId) {
        Map<String, Object> result = new HashMap<>();

        RoomDTO room = validateAndGetRoom(roomId);
        result.put("room", room);

        List<Review> reviews = Repo.findByRoomId(roomId);
        result.put("reviews", reviews);
        result.put("average_rating", getAvgRatingForRoom(roomId));
        result.put("total_reviews", reviews.size());

        return result;
    }

    public boolean hasUserReviewedRoom(long userId, long roomId) {
        return Repo.existsByUserIdAndRoomId(userId, roomId);
    }

    public Map<String, Object> getReviewStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // حساب متوسط التقييمات من الداتابيز
        Double avgRating = Repo.getAverageRating();
        Double totalReviews = Repo.getTotalReviews();

        stats.put("avgRating", avgRating != null ? Math.round(avgRating * 10) / 10.0 : 0.0);
        stats.put("totalReviews", totalReviews != null ? totalReviews : 0);
        return stats;
    }

}