package com.RoomServices.RoomService.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.RoomServices.RoomService.Entity.Wishlists;

import jakarta.transaction.Transactional;

public interface WhishListRepository extends JpaRepository<Wishlists, Long> {
    List<Wishlists> findByUserId(Long userId);

    @Transactional
    void deleteByUserId(Long userId);
    
    boolean existsByUserIdAndRoomId(Long userId, Long roomId);
}
