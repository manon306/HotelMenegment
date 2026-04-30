package com.BookingService.BookingService.Entity;

import java.time.LocalDate;

import com.BookingService.BookingService.ENUM.BookingStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import jakarta.persistence.EnumType;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long userId;
    private Long roomId;
    private LocalDate check_in_Date;
    private LocalDate check_out_Date;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    public double TotalPrice;
    private LocalDate created_at;
    private String phone;
    private int totalGuests;
    private String paymentIntentId;
    private String paymentStatus;
}
