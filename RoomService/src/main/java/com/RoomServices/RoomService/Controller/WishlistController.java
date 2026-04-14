package com.RoomServices.RoomService.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.RoomServices.RoomService.DTO.WishlistRequest;
import com.RoomServices.RoomService.Entity.Wishlists;
import com.RoomServices.RoomService.Services.WishlistServices;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {
    @Autowired
    private WishlistServices wishlistServices;

    @PostMapping("/add")
    public ResponseEntity<Wishlists> addToWishlist(@RequestBody @Valid WishlistRequest request) {
        return ResponseEntity.ok(wishlistServices.addToWishlist(request.getUserId(), request.getRoomId()));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Wishlists>> getWishlist(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistServices.getWishlistByUserId(userId));
    }

    @DeleteMapping("/remove/{wishlistId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable Long wishlistId) {
        wishlistServices.removeFromWishlist(wishlistId);
        return ResponseEntity.ok("Removed from wishlist");
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<String> clearWishlist(@PathVariable Long userId) {
        wishlistServices.clearWishlist(userId);
        return ResponseEntity.ok("Wishlist cleared");
    }
}
