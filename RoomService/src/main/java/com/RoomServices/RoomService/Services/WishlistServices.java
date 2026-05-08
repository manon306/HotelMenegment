package com.RoomServices.RoomService.Services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.RoomServices.RoomService.Entity.Room;
import com.RoomServices.RoomService.Entity.Wishlists;
import com.RoomServices.RoomService.Repository.RoomRepository;
import com.RoomServices.RoomService.Repository.WhishListRepository;

@Service
public class WishlistServices {

    @Autowired
    private WhishListRepository whishListRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserCleint userClient;

    private Room findRoomOrThrow(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
    }

    private void checkDuplicateWishlist(Long userId, Long roomId) {
        List<Wishlists> existing = whishListRepository.findByUserId(userId);
        boolean alreadyExists = existing.stream()
                .anyMatch(w -> w.getRoom().getId().equals(roomId));

        if (alreadyExists) {
            throw new RuntimeException("Room already in wishlist");
        }
    }

    public Wishlists addToWishlist(Long roomId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Long userId = userClient.getUserIdFromEmail(email);
        // userClient.validateUserExists(userId);
        Room room = findRoomOrThrow(roomId);
        checkDuplicateWishlist(userId, roomId);

        Wishlists wishlist = Wishlists.builder()
                .userId(userId)
                .room(room)
                .addedDate(LocalDate.now())
                .build();

        return whishListRepository.save(wishlist);
    }

    public List<Wishlists> getWishlistByUserId(Long userId) {
        userClient.validateUserExists(userId);
        return whishListRepository.findByUserId(userId);
    }

    public void removeFromWishlist(Long wishlistId) {
        if (!whishListRepository.existsById(wishlistId)) {
            throw new RuntimeException("Wishlist item not found with id: " + wishlistId);
        }
        whishListRepository.deleteById(wishlistId);
    }

    public void clearWishlist(Long userId) {
        userClient.validateUserExists(userId);
        whishListRepository.deleteByUserId(userId);
    }

    public boolean isRoomInWishlist(Long userId, Long roomId) {
        List<Wishlists> wishlist = whishListRepository.findByUserId(userId);
        return wishlist.stream()
                .anyMatch(w -> w.getRoom().getId().equals(roomId));
    }
}