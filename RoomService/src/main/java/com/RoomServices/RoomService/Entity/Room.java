package com.RoomServices.RoomService.Entity;

import java.util.ArrayList;
import java.util.List;
import com.RoomServices.RoomService.ENUM.RoomStatus;
import com.RoomServices.RoomService.ENUM.RoomType;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import jakarta.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private Double pricePerNight;

    private String description;

    private Integer maxNumberOfUsers;

    @Builder.Default
    private boolean oceanView = Boolean.FALSE;
    @Builder.Default
    private boolean kingBed = Boolean.FALSE;
    @Builder.Default
    private boolean balcony = Boolean.FALSE;
    @Builder.Default
    private boolean miniBar = Boolean.FALSE;
    @Builder.Default
    private boolean wifi = Boolean.FALSE;
    @Builder.Default
    private boolean smartTv = Boolean.FALSE;

    private long bookingId;

    @Builder.Default
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Wishlists> wishlists = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    @JsonManagedReference
    @ToString.Exclude
    private List<RoomImage> images;
}