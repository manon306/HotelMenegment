package com.BookingService.BookingService.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlyRevenueDTO {
    private int month;
    private int year;
    private Double totalRevenue;
}