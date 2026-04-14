package com.RoomServices.RoomService.DTO;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.RoomServices.RoomService.ENUM.RoomType;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateRoomRequest {

    @NotNull
    private Integer roomNumber;

    @NotNull
    private RoomType roomType;

    @NotNull
    @Positive
    private Double pricePerNight;

    private String description;

    private List<MultipartFile> images;

    @NotNull
    @Min(1)
    private Integer maxNumberOfUsers;

    private Boolean oceanView = false;
    private Boolean kingBed = false;
    private Boolean balcony = false;
    private Boolean miniBar = false;
    private Boolean wifi = false;
    private Boolean smartTv = false;
}