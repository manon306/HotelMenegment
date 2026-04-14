package com.example.REVIEWSERVICE.ENTITY;

import java.time.LocalDate;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "reviews",
        uniqueConstraints =  @UniqueConstraint(columnNames = {"user_id", "room_id"})
)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    // 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id")
    private long userId;

    @Column(name = "room_id")
    private long roomId;
    @Range(min=1, max=5)
    private int rating;
    private String comment ;
    @CreationTimestamp
    private LocalDate created_at;

}
