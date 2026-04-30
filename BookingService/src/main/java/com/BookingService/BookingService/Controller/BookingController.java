package com.BookingService.BookingService.Controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.BookingService.BookingService.Services.BookingServices;

import jakarta.validation.Valid;
import com.BookingService.BookingService.Entity.Booking;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingServices bookingServices;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Booking> createBooking(@RequestBody @Valid Booking request) {
        return ResponseEntity.ok(this.bookingServices.createBooking(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE')")
    public ResponseEntity<?> modifyBooking(@PathVariable Long id,
            @RequestBody @Valid Booking request) {
        this.bookingServices.modifyBooking(id, request);
        return ResponseEntity.ok("Booking updated successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER','EMPLOYEE')")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id) {
        this.bookingServices.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully");
    }

    @GetMapping("/history/{userId}")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<List<Booking>> viewBookingHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(this.bookingServices.viewBookingHistory(userId));
    }

    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<Booking> acceptBooking(@PathVariable Long id) {
        return ResponseEntity.ok(this.bookingServices.acceptBooking(id));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> rejectBooking(@PathVariable Long id) {
        this.bookingServices.rejectBooking(id);
        return ResponseEntity.ok("Booking rejected successfully");
    }

    // CHECK-IN → EMPLOYEE only
    @PostMapping("/{id}/checkin")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> checkIn(@PathVariable Long id) {
        this.bookingServices.checkIn(id);
        return ResponseEntity.ok("Booking checked in successfully");
    }

    // CHECK-OUT → EMPLOYEE only
    @PostMapping("/{id}/checkout")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> checkOut(@PathVariable Long id) {
        this.bookingServices.checkOut(id);
        return ResponseEntity.ok("Booking checked out successfully");
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id,
            @RequestParam String status,
            @RequestParam(required = false) String PaymentIntentId) {
        bookingServices.updateStatus(id, status, PaymentIntentId);
        return ResponseEntity.ok("Booking status updated");
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(bookingServices.getDashboardStats());
    }

    @GetMapping("/AllBookings")
    @PreAuthorize("hasAnyRole('CUSTOMER','ADMIN')")
    public ResponseEntity<List<Booking>> getAllBooking() {
        return ResponseEntity.ok(bookingServices.viewAllBookings());
    }

    @GetMapping("/pending/count")
    public ResponseEntity<Integer> countPendingBookingRequests() {
        return ResponseEntity.ok(bookingServices.countPendingBookingRequests());
    }

    @GetMapping("/confirmed/count")
    public ResponseEntity<Integer> countConfirmedBookings() {
        return ResponseEntity.ok(bookingServices.countConfirmedBookings());
    }

    @GetMapping("/revenue/total")
    public ResponseEntity<Double> countTotalRevenue() {
        return ResponseEntity.ok(bookingServices.countTotalRevenue());
    }

}