package com.BookingService.BookingService.DTO;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookingRequest {
    private Long userId;
    private Long roomId;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private int totalGuests;
}