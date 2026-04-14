package com.example.REVIEWSERVICE.DTO;

import lombok.Data;

@Data
public class RoomDTO {
    private Long id;
    private Integer roomNumber;
    private String status;
    private String roomType;
    private Double pricePerNight;
    private boolean oceanView;
    private boolean kingBed;
    private boolean balcony;
    private boolean miniBar;
    private boolean wifi;
    private boolean smartTv;
}
