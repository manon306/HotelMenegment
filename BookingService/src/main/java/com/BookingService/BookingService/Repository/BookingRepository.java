package com.BookingService.BookingService.Repository;

import com.BookingService.BookingService.ENUM.BookingStatus;
import com.BookingService.BookingService.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
        List<Booking> findByUserId(Long userId);

        List<Booking> findByRoomId(Long roomId);

        @Query("SELECT SUM(b.TotalPrice) FROM Booking b WHERE b.status = 'CHECKED_OUT'")
        Double sumAllRevenue();

        long countByStatus(String status);

        @Query("SELECT COUNT(b) FROM Booking b WHERE b.status = 'CONFIRMED' AND CURRENT_DATE BETWEEN b.check_in_Date AND b.check_out_Date")
        long countCurrentActiveBookings();

        int countByStatus(BookingStatus status);

}
