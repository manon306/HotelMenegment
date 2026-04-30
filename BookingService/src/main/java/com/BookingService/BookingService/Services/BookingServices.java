package com.BookingService.BookingService.Services;

import com.BookingService.BookingService.DTO.RoomDTO;
import com.BookingService.BookingService.DTO.UserDTO;
import com.BookingService.BookingService.ENUM.BookingStatus;
import com.BookingService.BookingService.Entity.Booking;
import com.BookingService.BookingService.Exceptions.BadRequestException;
import com.BookingService.BookingService.Exceptions.ResourceNotFoundException;
import com.BookingService.BookingService.Repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingServices {

    @Autowired
    private BookingRepository repo;

    @Autowired
    private UserClient userClient;

    @Autowired
    private RoomCleint roomClient;

    private void validateBookingId(Long id) {
        if (id == null || id <= 0) {
            throw new BadRequestException("Invalid booking ID: " + id);
        }
    }

    private Booking findBookingOrThrow(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
    }

    private void validateUserExists(Long userId) {
        UserDTO user = userClient.getUserFromUserService(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
    }

    private RoomDTO validateAndGetAvailableRoom(Long roomId) {
        RoomDTO room = roomClient.getRoomFromRoomService(roomId);
        if (room == null) {
            throw new ResourceNotFoundException("Room not found with id: " + roomId);
        }
        if (!"AVAILABLE".equals(room.getStatus())) {
            throw new BadRequestException("Room is not available. Current status: " + room.getStatus());
        }
        return room;
    }

    private void releaseRoom(Long roomId) {
        roomClient.updateRoomStatus(roomId, "AVAILABLE");
    }

    private void bookRoom(Long roomId) {
        roomClient.updateRoomStatus(roomId, "Booked");
    }

    // private void occupyRoom(Long roomId) {
    // roomClient.updateRoomStatus(roomId, "OCCUPIED");
    // }

    public Booking createBooking(Booking booking) {
        validateUserExists(booking.getUserId());
        RoomDTO room = validateAndGetAvailableRoom(booking.getRoomId());

        double totalPrice = calculateTotalPrice(booking, room);
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = repo.save(booking);
        bookRoom(booking.getRoomId());

        return savedBooking;
    }

    public Booking modifyBooking(Long id, Booking booking) {
        validateBookingId(id);
        Booking existingBooking = findBookingOrThrow(id);

        if (!existingBooking.getRoomId().equals(booking.getRoomId())) {
            RoomDTO newRoom = validateAndGetAvailableRoom(booking.getRoomId());
            releaseRoom(existingBooking.getRoomId());
            bookRoom(booking.getRoomId());
        }

        existingBooking.setCheck_in_Date(booking.getCheck_in_Date());
        existingBooking.setCheck_out_Date(booking.getCheck_out_Date());
        existingBooking.setRoomId(booking.getRoomId());

        RoomDTO room = roomClient.getRoomFromRoomService(booking.getRoomId());
        double totalPrice = calculateTotalPrice(existingBooking, room);
        existingBooking.setTotalPrice(totalPrice);

        return repo.save(existingBooking);
    }

    public void cancelBooking(Long bookingId) {
        validateBookingId(bookingId);
        Booking booking = findBookingOrThrow(bookingId);

        booking.setStatus(BookingStatus.CANCELLED);
        repo.save(booking);
        releaseRoom(booking.getRoomId());
    }

    public List<Booking> viewBookingHistory(Long userId) {
        validateUserExists(userId);
        return repo.findByUserId(userId);
    }

    public List<Booking> viewAllBookings() {
        return repo.findAll();
    }

    public int countPendingBookingRequests() {
        return repo.countByStatus(BookingStatus.PENDING);
    }

    public int countConfirmedBookings() {
        return repo.countByStatus(BookingStatus.ACCEPTED);
    }

    public Booking acceptBooking(Long bookingId) {
        validateBookingId(bookingId);
        Booking booking = findBookingOrThrow(bookingId);
        booking.setStatus(BookingStatus.ACCEPTED);
        return repo.save(booking);
    }

    public Double countTotalRevenue() {
        Double totalRevenue = repo.sumAllRevenue();
        return totalRevenue != null ? totalRevenue : 0;
    }

    public void rejectBooking(Long bookingId) {
        validateBookingId(bookingId);
        Booking booking = findBookingOrThrow(bookingId);
        booking.setStatus(BookingStatus.REJECTED);
        repo.save(booking);
        releaseRoom(booking.getRoomId());
    }

    public void checkIn(Long bookingId) {
        Booking booking = findBookingOrThrow(bookingId);

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BadRequestException("Cannot check-in a cancelled booking");
        }

        booking.setStatus(BookingStatus.CHECKED_IN);
        repo.save(booking);
    }

    public void checkOut(Long bookingId) {
        Booking booking = findBookingOrThrow(bookingId);
        booking.setStatus(BookingStatus.COMPLETED);
        repo.save(booking);
        releaseRoom(booking.getRoomId());
    }

    public void updateStatus(Long bookingId, String status) {
        Booking booking = findBookingOrThrow(bookingId);

        switch (status) {
            case "CONFIRMED":
                booking.setStatus(BookingStatus.ACCEPTED);
                break;
            case "CANCELLED":
                booking.setStatus(BookingStatus.CANCELLED);
                releaseRoom(booking.getRoomId());
                break;
            default:
                throw new BadRequestException("Invalid status");
        }

        repo.save(booking);
    }

    private double calculateTotalPrice(Booking booking, RoomDTO room) {
        long days = ChronoUnit.DAYS.between(booking.getCheck_in_Date(), booking.getCheck_out_Date());
        if (days <= 0) {
            throw new BadRequestException("Check-out date must be after check-in date");
        }
        double total = room.getPricePerNight().doubleValue() * days;
        if (days > 7) {
            total = total * 0.9;
        }
        return total;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        Double totalRevenue = repo.sumAllRevenue();
        stats.put("totalRevenue", totalRevenue != null ? totalRevenue : 0.0);

        stats.put("totalBookings", repo.count());

        stats.put("occupancyRate", calculateOccupancyPercent());

        return stats;
    }

    private double calculateOccupancyPercent() {
        long activeBookings = repo.countCurrentActiveBookings();

        var totalRooms = roomClient.getTotalRooms();

        if (totalRooms == null || totalRooms == 0)
            return 0.0;

        double rate = ((double) activeBookings / totalRooms) * 100;
        return Math.round(rate * 10) / 10.0;
    }
}