package com.example.REVIEWSERVICE.REPOSITORY;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.REVIEWSERVICE.ENTITY.Review;

public interface ReviewRepo extends JpaRepository<Review, Long> {
    List<Review> findByRoomId(long room_id);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.roomId = :roomId")
    Optional<Double> findAvgRatingByRoomId(@Param("roomId") long roomId);

    boolean existsByUserIdAndRoomId(long userId, long roomId);

    List<Review> findByUserId(long userId);

    @Query("SELECT AVG(r.rating) FROM Review r")
    Double getAverageRating();

    @Query("SELECT COUNT(r) FROM Review r")
    Double getTotalReviews();
}
