package com.BookingService.BookingService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.BookingService.BookingService.Services.BookingServices;

import jakarta.validation.Valid;
import com.BookingService.BookingService.Entity.Booking;

@RestController
@RequestMapping("/Booking")
public class BookingController {

    @Autowired
    private BookingServices bookingServices;

    @PostMapping("/bookings")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Booking> createBooking(@RequestBody @Valid Booking request) {
        return ResponseEntity.ok(this.bookingServices.createBooking(request));
    }

    @PutMapping("/bookings/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE')")
    public ResponseEntity<?> modifyBooking(@PathVariable Long id,
            @RequestBody @Valid Booking request) {
        this.bookingServices.modifyBooking(id, request);
        return ResponseEntity.ok("Booking updated successfully");
    }

    @DeleteMapping("/bookings/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE')")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        this.bookingServices.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }

    @GetMapping("/bookings/history/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<List<Booking>> viewBookingHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(this.bookingServices.viewBookingHistory(userId));
    }

    @PostMapping("/bookings/{id}/accept")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Booking> acceptBooking(@PathVariable Long id) {
        return ResponseEntity.ok(this.bookingServices.acceptBooking(id));
    }

    @PostMapping("/bookings/{id}/reject")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> rejectBooking(@PathVariable Long id) {
        this.bookingServices.rejectBooking(id);
        return ResponseEntity.ok("Booking rejected successfully");
    }

    // CHECK-IN → EMPLOYEE only
    @PostMapping("/bookings/{id}/checkin")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> checkIn(@PathVariable Long id) {
        this.bookingServices.checkIn(id);
        return ResponseEntity.ok("Booking checked in successfully");
    }

    // CHECK-OUT → EMPLOYEE only
    @PostMapping("/bookings/{id}/checkout")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> checkOut(@PathVariable Long id) {
        this.bookingServices.checkOut(id);
        return ResponseEntity.ok("Booking checked out successfully");
    }

    @PutMapping("/bookings/{id}/status")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id,
            @RequestParam String status) {
        bookingServices.updateStatus(id, status);
        return ResponseEntity.ok("Booking status updated");
    }
}