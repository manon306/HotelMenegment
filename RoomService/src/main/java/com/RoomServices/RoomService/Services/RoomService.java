package com.RoomServices.RoomService.Services;

import com.RoomServices.RoomService.DTO.CreateRoomRequest;
import com.RoomServices.RoomService.DTO.UpdateRoomRequest;
import com.RoomServices.RoomService.ENUM.RoomStatus;
import com.RoomServices.RoomService.ENUM.RoomType;
import com.RoomServices.RoomService.Entity.*;
import com.RoomServices.RoomService.Exceptions.BadRequestException;
import com.RoomServices.RoomService.Exceptions.ResourceNotFoundException;
import com.RoomServices.RoomService.Repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    private final String uploadDir = "uploads/";
    private static final int MIN_IMAGES_REQUIRED = 3;

    private Room findRoomByIdOrThrow(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("room not found"));
    }

    private void validateRoomNumberUniqueness(Integer roomNumber, Long excludeRoomId) {
        boolean exists;
        if (excludeRoomId != null) {
            exists = roomRepository.existsByRoomNumber(roomNumber) &&
                    !roomRepository.findById(excludeRoomId)
                            .map(room -> room.getRoomNumber().equals(roomNumber))
                            .orElse(false);
        } else {
            exists = roomRepository.existsByRoomNumber(roomNumber);
        }

        if (exists) {
            throw new BadRequestException("Room already exists with number: " + roomNumber);
        }
    }

    private void validateImagesCount(List<MultipartFile> images) {
        if (images == null || images.size() < MIN_IMAGES_REQUIRED) {
            throw new BadRequestException("You must upload at least " + MIN_IMAGES_REQUIRED + " images");
        }
    }

    private String saveImageFile(MultipartFile file) {
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path);
            return path.toString();
        } catch (Exception e) {
            throw new BadRequestException("Error uploading image: " + file.getOriginalFilename());
        }
    }

    private List<RoomImage> saveRoomImages(List<MultipartFile> images, Room room) {
        List<RoomImage> roomImages = new ArrayList<>();

        for (MultipartFile file : images) {
            String imagePath = saveImageFile(file);

            RoomImage image = new RoomImage();
            image.setImagePath(imagePath);
            image.setRoom(room);
            roomImages.add(image);
        }

        return roomImages;
    }

    private void updateRoomFieldIfPresent(UpdateRoomRequest request, Room room) {
        if (request.getRoomNumber() != null) {
            room.setRoomNumber(request.getRoomNumber());
        }
        if (request.getPricePerNight() != null) {
            room.setPricePerNight(request.getPricePerNight());
        }
        if (request.getDescription() != null) {
            room.setDescription(request.getDescription());
        }
        if (request.getRoomType() != null) {
            room.setRoomType(request.getRoomType());
        }
        if (request.getMaxNumberOfUsers() != null) {
            room.setMaxNumberOfUsers(request.getMaxNumberOfUsers());
        }
        if (request.getOceanView() != null) {
            room.setOceanView(request.getOceanView());
        }
        if (request.getKingBed() != null) {
            room.setKingBed(request.getKingBed());
        }
        if (request.getBalcony() != null) {
            room.setBalcony(request.getBalcony());
        }
        if (request.getMiniBar() != null) {
            room.setMiniBar(request.getMiniBar());
        }
        if (request.getWifi() != null) {
            room.setWifi(request.getWifi());
        }
        if (request.getSmartTv() != null) {
            room.setSmartTv(request.getSmartTv());
        }
    }

    public Room addRoom(CreateRoomRequest request) {
        validateRoomNumberUniqueness(request.getRoomNumber(), null);
        validateImagesCount(request.getImages());

        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .status(RoomStatus.AVAILABLE)
                .pricePerNight(request.getPricePerNight())
                .description(request.getDescription())
                .roomType(request.getRoomType())
                .maxNumberOfUsers(request.getMaxNumberOfUsers())
                .oceanView(Boolean.TRUE.equals(request.getOceanView()))
                .kingBed(Boolean.TRUE.equals(request.getKingBed()))
                .balcony(Boolean.TRUE.equals(request.getBalcony()))
                .miniBar(Boolean.TRUE.equals(request.getMiniBar()))
                .wifi(Boolean.TRUE.equals(request.getWifi()))
                .smartTv(Boolean.TRUE.equals(request.getSmartTv()))
                .build();

        List<RoomImage> images = saveRoomImages(request.getImages(), room);
        room.setImages(images);

        return roomRepository.save(room);
    }

    public Room updateRoom(Long id, UpdateRoomRequest request) {
        Room room = findRoomByIdOrThrow(id);

        if (request.getRoomNumber() != null && !request.getRoomNumber().equals(room.getRoomNumber())) {
            validateRoomNumberUniqueness(request.getRoomNumber(), id);
        }

        updateRoomFieldIfPresent(request, room);

        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        Room room = findRoomByIdOrThrow(id);
        roomRepository.delete(room);
    }

    public Room getRoomById(Long id) {
        return findRoomByIdOrThrow(id);
    }

    public Page<Room> getAllRooms(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return roomRepository.findAll(pageable);
    }

    public Room updateRoomStatus(Long id, RoomStatus status) {
        Room room = findRoomByIdOrThrow(id);
        room.setStatus(status);
        return roomRepository.save(room);
    }

    public List<Room> getRoomsByType(RoomType roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    public List<Room> getRoomsByFeature(String feature) {
        return switch (feature.toLowerCase()) {
            case "oceanview" -> roomRepository.findByOceanViewTrue();
            case "wifi" -> roomRepository.findByWifiTrue();
            case "kingbed" -> roomRepository.findByKingBedTrue();
            case "balcony" -> roomRepository.findByBalconyTrue();
            case "minibar" -> roomRepository.findByMiniBarTrue();
            case "smarttv" -> roomRepository.findBySmartTvTrue();
            default -> throw new BadRequestException("Unknown feature: " + feature);
        };
    }

    public Room markRoomUnderMaintenance(Long id) {
        Room room = findRoomByIdOrThrow(id);
        room.setStatus(RoomStatus.MAINTENANCE);
        return roomRepository.save(room);
    }

    public List<Room> getAvailableRooms() {
        return roomRepository.findByStatus(RoomStatus.AVAILABLE);
    }
}