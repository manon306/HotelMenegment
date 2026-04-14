package com.BookingService.BookingService.Repository;

import com.BookingService.BookingService.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
        List<Booking> findByUserId(Long userId);
        List<Booking> findByRoomId(Long roomId);

}
