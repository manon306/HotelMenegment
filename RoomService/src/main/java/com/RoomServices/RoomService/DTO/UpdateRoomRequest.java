package com.RoomServices.RoomService.DTO;

import com.RoomServices.RoomService.ENUM.RoomType;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UpdateRoomRequest {

    private Integer roomNumber;

    private RoomType roomType;

    @Positive
    private Double pricePerNight;

    private String description;

    @Min(1)
    private Integer maxNumberOfUsers;

    private Boolean oceanView;
    private Boolean kingBed;
    private Boolean balcony;
    private Boolean miniBar;
    private Boolean wifi;
    private Boolean smartTv;
}