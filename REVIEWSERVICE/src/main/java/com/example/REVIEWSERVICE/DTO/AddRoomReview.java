package com.example.REVIEWSERVICE.DTO;

import com.example.REVIEWSERVICE.ENTITY.Review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class AddRoomReview {
    public long user_id;
    public long room_id;
    public int rating;
    public String comment;

    public Review toEntity() {
        return Review.builder()
                .userId(this.user_id)
                .roomId(this.room_id)
                .rating(this.rating)
                .comment(this.comment)
                .build();
    }
}
