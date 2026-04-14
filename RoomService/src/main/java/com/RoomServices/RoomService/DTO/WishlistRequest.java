package com.RoomServices.RoomService.DTO;

import lombok.Data;

@Data
public class WishlistRequest {
    private Long userId;
    private Long roomId;
}
