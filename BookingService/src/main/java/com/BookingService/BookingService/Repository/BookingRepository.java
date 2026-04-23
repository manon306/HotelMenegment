package com.BookingService.BookingService.Repository;

import com.BookingService.BookingService.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
        List<Booking> findByUserId(Long userId);

        List<Booking> findByRoomId(Long roomId);

        Double sumAllRevenue();

        long countByStatus(String status);

        // في BookingRepository
        @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CONFIRMED' AND CURRENT_DATE BETWEEN b.checkInDate AND b.checkOutDate")
        long countCurrentActiveBookings();

}
