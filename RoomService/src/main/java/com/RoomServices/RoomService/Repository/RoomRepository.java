package com.RoomServices.RoomService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RoomServices.RoomService.ENUM.RoomType;
import com.RoomServices.RoomService.Entity.Room;
import com.RoomServices.RoomService.ENUM.RoomStatus;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // فلترة بالنوع
    List<Room> findByRoomType(RoomType roomType);

    // فلترة بالمميزات (بسيطة وسريعة)
    boolean existsByRoomNumber(Integer roomNumber);

    List<Room> findByOceanViewTrue();

    List<Room> findByWifiTrue();

    List<Room> findByKingBedTrue();

    List<Room> findByBalconyTrue();

    List<Room> findByMiniBarTrue();

    List<Room> findBySmartTvTrue();

    List<Room> findByStatus(RoomStatus status);

    int countByStatus(RoomStatus status);

    // @Query("SELECT r FROM Room r WHERE r.status = 'AVAILABLE' AND r.id NOT IN " +
    // "(SELECT b.room.id FROM Booking b WHERE b.checkIn < :checkOut AND b.checkOut
    // > :checkIn)")
    // List<Room> findAvailableRooms(LocalDate checkIn, LocalDate checkOut);
}